CREATE OR REPLACE PACKAGE OMS_SOS_WS_INVOKER AS
---
--- Following CONSTANT numeric variables have been defined for WS that will be invoked.
--- The constant values MUST MATCH with the values of the Web Service Id in the OMS table
--- OMS_WEBSERVICE_URI_DETAIL
---

   TYPE  rec_seibel_status_head IS RECORD(v_application_id        OMS_CUST_ORD_HEAD.APPLICATION_ID%TYPE,
                                          v_cust_order_no         OMS_SOSTATUS_DESC.CUSTOMER_ORDER_NO%TYPE,
                                          v_sub_cust_order_no     OMS_CUST_ORD_HEAD.SUB_CUST_ORDER_NO%TYPE,
                                          v_oms_cust_ord_no       OMS_CUST_ORD_HEAD.OMS_CUST_ORD_NO%TYPE,
                                          v_cust_id               OMS_CUST_ORD_HEAD.CUST_ID%TYPE,
                                          v_consumer_dly_time     VARCHAR2(50),
                                          v_close_datetime        VARCHAR2(50)
                                         );


   TYPE  rec_seibel_status_item IS RECORD(v_item_id               OMS_SOSTATUS_DTL.ITEM_ID%TYPE,
                                          v_line_no               OMS_CUST_ORD_ITEM.LINE_NO%TYPE,
                                          v_unit_qty              OMS_SOSTATUS_DTL.UNIT_QTY%TYPE,
                                          v_event_id              OMS_CUST_ORD_LOG.EVENT_ID%TYPE,
                                          v_event_comments        OMS_CUST_ORD_LOG.EVENT_COMMENTS%TYPE,
                                          v_dlv_conf_id           OMS_SOSTATUS_DESC.DISTRO_NBR%TYPE,
                                          v_s_loc_type            OMS_SOSTATUS_DESC.LOC_TYPE%TYPE,
                                          v_s_loc                 OMS_SOSTATUS_DESC.DC_DEST_ID%TYPE,
                                          v_d_loc_type            OMS_SOSTATUS_DESC.LOC_TYPE%TYPE,
                                          v_d_loc                 OMS_SOSTATUS_DESC.DC_DEST_ID%TYPE,
                                          v_updated_dttime        VARCHAR2(50),
                                          v_remarks               OMS_RTLOG_PUBLISH_LOG.ERROR_MESSAGE%TYPE
                                         );

   TYPE t_head_table IS TABLE OF rec_seibel_status_head;
   TYPE t_item_table IS TABLE OF rec_seibel_status_item;

   v_seibel_head    rec_seibel_status_head;
   v_seibel_item    rec_seibel_status_item;
   L_table_head     t_head_table;
   L_table_item     t_item_table;

--v_seibel_head    rec_seibel_status_head;
--v_seibel_item    rec_seibel_status_item;
   lsv_WS_Name CONSTANT OMS_WEBSERVICE_URI_DETAIL.WEB_SERVICE_NAME%TYPE := 'SOA_CO_STATUS_UPDATE';

----
--- Function : f_REC_Siebel_COStatusUpdate
--- Inputs   : v_cust_ord_no - External Customer Order No
--- Return   : returns message from the invoked Siebel WS
----
FUNCTION f_REC_Siebel_COStatusUpdate(v_seibel_head IN rec_seibel_status_head,
                                     v_seibel_item IN t_item_table)
RETURN VARCHAR2;

FUNCTION generic_oms_soap_call(p_payload        IN CLOB,
                               p_target_url     IN OMS_WEBSERVICE_URI_DETAIL.URL%TYPE,
                               p_soap_action    IN VARCHAR2 DEFAULT 'process',
							   p_soap_envelope  IN OUT CLOB
                              )
RETURN XMLTYPE;


FUNCTION get_WS_URL_Details(p_WS_Name IN OMS_WEBSERVICE_URI_DETAIL.WEB_SERVICE_NAME%TYPE)
RETURN OMS_WEBSERVICE_URI_DETAIL%ROWTYPE;

FUNCTION get_Header_XML_COStatusUpdate (v_seibel_head IN rec_seibel_status_head, v_target_namespace IN OMS_WEBSERVICE_URI_DETAIL.TARGET_NAMESPACE%TYPE)
RETURN CLOB;

FUNCTION get_Details_XML_COStatusUpdate (v_seibel_item IN rec_seibel_status_item)
RETURN CLOB;


END OMS_SOS_WS_INVOKER;
/


CREATE OR REPLACE PACKAGE BODY OMS_SOS_WS_INVOKER
AS

FUNCTION generic_oms_soap_call(p_payload        IN CLOB,
                               p_target_url     IN OMS_WEBSERVICE_URI_DETAIL.URL%TYPE,
                               p_soap_action    IN VARCHAR2 DEFAULT 'process',
							   p_soap_envelope  IN OUT CLOB
                              )
RETURN XMLTYPE IS
-----------------------------------------------------------------------------------------------------------------------
--
-- This is a generic function and should not be touched.
-- This function is the Client function getting the XML data from the web services.
-- The calling function should pass the inputs and have the logic to extract the values from the XML document returned
--     by this function.
--
-- Inputs: The function takes in the target URL, the Payload (content inside the soap envelope request) and soap action
-- Output: It returns the XML document received in response to the w/s call.
--
-- Reference : https://technology.amis.nl/2008/06/16/rapid-plsql-web-service-client-development-using-soapui-and-utl_http/
--
-----------------------------------------------------------------------------------------------------------------------

   c_soap_envelope CLOB := '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:oms="http://www.extra.com/Services/OmsStatus" xmlns:ext="http://schemas.datacontract.org/2004/07/eXtra.Services.Oms">
                            <soap:Body>**payload**</soap:Body>
                            </soap:Envelope>';
   l_soap_request  CLOB;
   l_soap_response CLOB;
   l_fault_soap_response   CLOB;
   http_req utl_http.req;
   http_resp utl_http.resp;
   http_failure EXCEPTION;
   pragma       EXCEPTION_INIT(http_failure, -29273);
   L_fail_cnt   NUMBER := 0;
   L_target_url  OMS_WEBSERVICE_URI_DETAIL.URL%TYPE;
    cursor soa_url is 
       SELECT  SUBSTR(p_target_url, 1, LENGTH(p_target_url))  
    FROM DUAL;

BEGIN
    ---
	---Fault response to be returned for the failed process
   l_fault_soap_response := '<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
                                 <S:Body>
                                    <S:Fault xmlns:ns4="http://www.w3.org/2003/05/soap-envelope">
                                       <faultcode>S:Server</faultcode>
									   <faultstring>OMS_SOS_WS_INVOKER.generic_oms_soap_call function Processing Failed</faultstring>
                                    </S:Fault>
                                 </S:Body>
                              </S:Envelope>';

    --- If the INPUT payload is ALREADY wrapped in soap:Envelope then directly use the payload.
    --- Else wrap the payload with the soap:Envelope element.
    ---
   IF INSTR(p_payload,':Envelop')=0 THEN
      l_soap_request := replace(c_soap_envelope, '**payload**', p_payload);
   ELSE
      l_soap_request := p_payload;
   END IF;

   p_soap_envelope := replace(c_soap_envelope, '**payload**', p_payload);

          open soa_url; 
          fetch soa_url into L_target_url;
          close soa_url;
   http_req:= utl_http.begin_request(L_target_url,
                                     'POST',
                                     'HTTP/1.1'
                                    );
   -- Include CHARSET to avoid error if using Arabhi(ISO-8859-6) characters
   utl_http.set_header(http_req, 'Content-Type', 'text/xml');
   utl_http.set_header(http_req, 'Content-Length', length(l_soap_request));
   utl_http.set_header(http_req, 'SOAPAction', 'http://www.extra.com/Services/OmsStatus/IStatus/UpdateOrderStatus');
   utl_http.write_text(http_req, l_soap_request);
   utl_http.set_transfer_timeout(100000);
    -- the actual call to the service is made here
   http_resp:= utl_http.get_response(http_req);
   utl_http.read_text(http_resp, l_soap_response);
   utl_http.end_response(http_resp);


   /* If the target Webservice is down sometimes response contains HTML tags
	   XMLType.createXML cannot handle the HTML response hence check if the response contains HTML tags,
	   if yes then return the fault response else return the XML response */
	select dbms_lob.instr(l_soap_response,'<HTML>')
	  into L_fail_cnt
	  from dual;
	if L_fail_cnt > 0 then
	   return XMLType.createXML(l_fault_soap_response).extract( '/S:Envelope/S:Body/child::node()'
                        , 'xmlns:S="http://schemas.xmlsoap.org/soap/envelope/"'
                        );
	else
       -- only return the payload from the soap response - that is: the content of the body element in the SOAP envelope
       return XMLType.createXML(l_soap_response).extract( '/S:Envelope/S:Body/child::node()'
                        , 'xmlns:S="http://schemas.xmlsoap.org/soap/envelope/"'
                        );
    end if;

EXCEPTION
   WHEN UTL_HTTP.transfer_timeout THEN
      DBMS_OUTPUT.PUT_LINE('Transfer Timeout Processing');
      return XMLType.createXML(l_fault_soap_response).extract( '/S:Envelope/S:Body/child::node()'
                        , 'xmlns:S="http://schemas.xmlsoap.org/soap/envelope/"'
                        );
   WHEN http_failure THEN
      DBMS_OUTPUT.PUT_LINE('HTTP Request Processing');
      return XMLType.createXML(l_fault_soap_response).extract( '/S:Envelope/S:Body/child::node()'
                        , 'xmlns:S="http://schemas.xmlsoap.org/soap/envelope/"'
                        );
	WHEN utl_http.end_of_body THEN
		utl_http.end_response(http_resp);	
		return XMLType.createXML(l_fault_soap_response).extract( '/S:Envelope/S:Body/child::node()'
                        , 'xmlns:S="http://schemas.xmlsoap.org/soap/envelope/"'
                        );
   WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('ERROR AT GENERIC SOAP CALL');
      DBMS_OUTPUT.PUT_LINE('ERROR CODE'||SQLCODE);
      DBMS_OUTPUT.PUT_LINE('ERROR MESSAGE'||SQLERRM);
      -- Include procedure call to insert error message
      P_INS_ERROR_DTL('OMS_SOS_WS_INVOKER','generic_oms_soap_call',null,null,null,null,null,null,SQLCODE,SQLERRM,sysdate);
      commit;
      return XMLType.createXML(l_fault_soap_response).extract( '/S:Envelope/S:Body/child::node()'
                        , 'xmlns:S="http://schemas.xmlsoap.org/soap/envelope/"'
                        );
END generic_oms_soap_call;


FUNCTION get_WS_URL_Details(p_WS_Name IN OMS_WEBSERVICE_URI_DETAIL.WEB_SERVICE_NAME%TYPE)
RETURN OMS_WEBSERVICE_URI_DETAIL%ROWTYPE AS
/***********************************************************************************************************************************
  CREATED BY  : Pradeep
  CREATED ON  : 24/03/2015
  MODIFIED BY : Pradeep
  MODIFIED ON : 24/03/2015
  LOGIC       : The function is a generic funtion that returns the Web Service Details for the Web Service id passed as input.
***********************************************************************************************************************************/
   theReturnData OMS_WEBSERVICE_URI_DETAIL%ROWTYPE;
   oms_error_message       VARCHAR2(3000);
BEGIN
   SELECT *
     INTO theReturnData
     FROM OMS_WEBSERVICE_URI_DETAIL
    WHERE web_service_name = p_WS_name;

   IF SQL%ROWCOUNT>1 THEN
      oms_error_message:='More than one record found for web service id '||p_WS_Name||' in table OMS_WEBSERVICE_URI_DETAIL.';
      ---OMS_UTIL.ERROR_REPORTING('get_WS_URL_Details',oms_error_message);
      RAISE TOO_MANY_ROWS;
   END IF;

   IF SQL%ROWCOUNT<1 THEN
      oms_error_message:='No record found for web service id '||p_WS_Name||' in table OMS_WEBSERVICE_URI_DETAIL.';
      ---OMS_UTIL.ERROR_REPORTING('get_WS_URL_Details',oms_error_message);
      RAISE NO_DATA_FOUND;
   END IF;

   RETURN theReturnData;
EXCEPTION
   WHEN OTHERS THEN
      oms_error_message:='Encountered ERROR processing Web Service Id :'|| p_WS_Name||'. The ERROR is :'||SUBSTR(SQLERRM(),500);
      ---OMS_UTIL.ERROR_REPORTING('get_WS_URL_Details',oms_error_message);
END get_WS_URL_Details;


----
--- Function : f_REC_Siebel_COStatusUpdate
--- Inputs   : v_message_id - External Customer Order No
--- Return   : returns message from the invoked Siebel WS
----
FUNCTION f_REC_Siebel_COStatusUpdate(v_seibel_head IN rec_seibel_status_head,
                                     v_seibel_item IN t_item_table)
RETURN VARCHAR2 AS
/***********************************************************************************************************************************
  CREATED BY  : Gowtham
  CREATED ON  : 26/06/2015
  LOGIC       : This function gets the details of the RECEIPT transaction from relevant OMS tables based on the External Cust Order passed as input
                The data collected is then formatted into and XML document as specified by the WSDL pertaining to the WS invoked
                All the connection details pertaining to the WS invoked is also collected from the OMS_WEBSERVICE_URI_DETAIL
                XML Document prepared is then passed on to the WS.
                The response received from the WS is then returned to the calling object.
***********************************************************************************************************************************/
   l_response_payload       XMLType;
   l_payload                CLOB ;--VARCHAR2(20000); -- Increase the size depending on the payload length
   l_payload_detail         CLOB; --VARCHAR2(10000);
   l_payload_detail_tmp     CLOB; --VARCHAR2(10000);
   theOutputMessage         VARCHAR2(4000);
   l_tran_seq               NUMBER(10);
   l_Test_response          VARCHAR2(3000);  -- Used to output the response message. The code is commented out.
   l_count                  NUMBER       :=1;
   oms_error_message        VARCHAR2(4000);
   l_payload_msg            CLOB;
    l_ins_payload                CLOB ;
    
   theWSConnDetail OMS_WEBSERVICE_URI_DETAIL%ROWTYPE;

   lsv_Request_parent_element VARCHAR2(300) :='UpdateOrderStatus';
   lsv_Response_parent_element VARCHAR2(300) :='UpdateOrderStatusResponse';

   L_application_id         OMS_CUST_ORD_HEAD.APPLICATION_ID%TYPE;
   L_cust_order_no          OMS_CUST_ORD_HEAD.CUST_ORDER_NO%TYPE;

   c_soap_envelope CLOB := '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:oms="http://www.extra.com/Services/OmsStatus" xmlns:ext="http://schemas.datacontract.org/2004/07/eXtra.Services.Oms">
                            <soap:Body>**payload**</soap:Body>
                            </soap:Envelope>'; 

   cursor c_get_repub_ins_data IS
      SELECT ocoh.application_id,
             ocoh.cust_order_no
        FROM oms_cust_ord_head ocoh
       WHERE ocoh.oms_cust_ord_no = v_seibel_head.v_oms_cust_ord_no
       and ocoh.ORDER_REQUESTOR_ID IN ('19010','20010','30010');

BEGIN

	open c_get_repub_ins_data;
	loop
	fetch c_get_repub_ins_data into L_application_id, L_cust_order_no;
	EXIT WHEN c_get_repub_ins_data%NOTFOUND;

	IF (L_application_id<>'ORPOS') then 
    --- Get the WS URL Connection Details for ASNOUT SIEBEL CO Statuts Update Web Service from the database.
   theWSConnDetail := get_WS_URL_Details(lsv_WS_Name);

   l_payload := get_Header_XML_COStatusUpdate(v_seibel_head,theWSConnDetail.Target_Namespace);
    ---
    --- Loop through the item table to get associated item records
    ---
   FOR i IN v_seibel_item.first .. v_seibel_item.last
   LOOP
      l_payload_detail_tmp := get_Details_XML_COStatusUpdate(v_seibel_item(i));
      l_payload_detail := l_payload_detail || l_payload_detail_tmp;
   END LOOP;

    ---
    --- Check if the payload or payload_detail is NULL. If it is null then we cannot proceed and an exception has to be raised.
    --- Header and atleast on detail record has to exist in to call. Error will be logged into the OMS_ERROR_LOG table.
    ---

   IF (l_payload IS NOT NULL AND l_payload_detail IS NOT NULL) THEN
      ---
      --- If payload and payload detail exist, concatenate both into payload.
      ---
      l_payload:=l_payload||'<ext:OrderDetailStatuses>'||l_payload_detail||'</ext:OrderDetailStatuses>'||'</oms:orderStatus>'||'</oms:'||lsv_Request_parent_element||'>';
   ELSE
      oms_error_message := 'No data found in OMS_CUST_ORD_HEAD OR OMS_CUST_ORD_ITEM Table for OMS_CUST_ORDER_NO : ';
      ---OMS_UTIL.ERROR_REPORTING('OMS_ASNOUT_SIEBEL_WS_INVOKER',oms_error_message);
      RAISE NO_DATA_FOUND;
   END IF;
    --dbms_output.put_line('Final Payload  : '||l_payload);
    ---
    --- The payload will be send to the generic soap_call function with appropriate inputs.
    ---

--   l_response_payload := generic_oms_soap_call(l_payload, theWSConnDetail.URL,null,l_payload_msg); -- theWSConnDetail.Target_NameSpace||theWSConnDetail.Parent_Element);


    --- Get(extract) each of the values into a variable of the Record Type
    --- Once All the values are populated return the record. Uncomment the l_Test_Response below to see the XML Data
	--l_Test_response := l_response_payload.getStringVal();

    -- Fetching the sequence value from the sequence
   SELECT oms_republish_dataseq.nextval
     INTO l_tran_seq
     FROM DUAL;

     l_ins_payload := replace(c_soap_envelope, '**payload**', l_payload);

   INSERT INTO OMS_PUBLISH_WS_DATA(SEQ_NO,
                                  APPLICATION_ID,
                                  FIRST_ATTEMPT_DATETIME,
                                  TRANSACTION_KEY,
                                  XML_MSG,
                                  WEB_SERVICE_ID,
                                  ATTEMPT_CNT,
                                  ERROR_MSG,
                                  REPUBLISH_STATUS,
                                  LAST_ATTEMPT_TIME)
                           VALUES(l_tran_seq,
                                  L_application_id,
                                  sysdate,
                                  L_cust_order_no,
                                  l_ins_payload,
                                  theWSConnDetail.web_service_id,
                                  0,
                                  null,
                                  'N',
                                  sysdate); 

    ---
    --- Check if the Response contains a SOAP Fault message
    ---
--   IF l_response_payload.existsNode('/S:Fault', 'xmlns:S="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns4="http://www.w3.org/2003/05/soap-envelope"')=1 THEN
--      theOutputMessage := 'SOAP Error :'|| (l_response_payload.extract('/S:Fault/faultstring/text()', 'xmlns:S="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns4="http://www.w3.org/2003/05/soap-envelope"').getStringVal());

   ---
   --   Calling the procedure to insert the faliure message into REPUBLISH DATA table
   ---
	  --OPEN c_get_repub_ins_data;
      --FETCH c_get_repub_ins_data INTO L_application_id,  L_cust_order_no;
      --CLOSE c_get_repub_ins_data;
--      P_INS_REPUB_DATA(l_tran_seq,
--                       L_application_id,
--                       sysdate,
--                       L_cust_order_no,
--                       l_payload_msg,
--                       theWSConnDetail.web_service_id,
--                       1,
--                       theOutputMessage,
--                       'F',
--                       sysdate);
--
--   ELSE
--    -
--    - If No SOAP Fault is raised then proceed with the successful processing of the response message.
--    -
--      WHILE l_response_payload.existsNode('/'||lsv_Response_parent_element||'[' || TO_CHAR(l_count) || ']', 'xmlns="'|| theWSConnDetail.Target_Namespace||'"') = 1
--      LOOP
--         theOutputMessage := (l_response_payload.extract('/'||lsv_Response_parent_element||'/UpdateOrderStatusResult[' || TO_CHAR(l_count) || ']/text()','xmlns="'|| theWSConnDetail.Target_Namespace||'"').getStringVal());
--         l_count          := l_count + 1;
--      END LOOP;
--   END IF;
    ---
    --- Return the message back
    ---
	END IF; 

   END LOOP;
   close c_get_repub_ins_data;

   RETURN theOutputMessage;

EXCEPTION
   WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('ERROR OCCURED');
      DBMS_OUTPUT.PUT_LINE('ERROR CODE'||SQLCODE);
      DBMS_OUTPUT.PUT_LINE('ERROR MESSAGE'||SQLERRM);
      return SQLERRM;

END f_REC_Siebel_COStatusUpdate;

FUNCTION get_Header_XML_COStatusUpdate (v_seibel_head IN rec_seibel_status_head, v_target_namespace IN OMS_WEBSERVICE_URI_DETAIL.TARGET_NAMESPACE%TYPE)
RETURN CLOB AS
   lsv_Return_XML CLOB;
BEGIN

   lsv_Return_XML:='<oms:UpdateOrderStatus>
					<oms:orderStatus>
					<ext:EnitityId>eXtra</ext:EnitityId>
                    <ext:ApplicationId>'||v_seibel_head.v_application_id|| '</ext:ApplicationId>
                    <ext:OrderId>'||v_seibel_head.v_cust_order_no||'</ext:OrderId>
                    <ext:SubOrderId>'||v_seibel_head.v_sub_cust_order_no||'</ext:SubOrderId>
                    <ext:OmsOrderId>'||v_seibel_head.v_oms_cust_ord_no||'</ext:OmsOrderId>
                    <ext:DeliveryDate>'||v_seibel_head.v_consumer_dly_time||'</ext:DeliveryDate>
                    <ext:UpdateDate>'||v_seibel_head.v_close_datetime||'</ext:UpdateDate>';

   RETURN lsv_Return_XML;
EXCEPTION
   WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('ERROR OCCURED');
      DBMS_OUTPUT.PUT_LINE('ERROR CODE'||SQLCODE);
      DBMS_OUTPUT.PUT_LINE('ERROR MESSAGE'||SQLERRM);
	  return TO_CLOB(SQLERRM);
END get_Header_XML_COStatusUpdate;

FUNCTION get_Details_XML_COStatusUpdate (v_seibel_item IN rec_seibel_status_item)
RETURN CLOB AS

   lsv_Return_XML CLOB; ---VARCHAR2(5000);

BEGIN

   lsv_Return_XML:= lsv_Return_XML||'
                    '||'<ext:OrderDetailStatus>
						  <ext:OrderDetailId>'||v_seibel_item.v_line_no||'</ext:OrderDetailId>
						  <ext:ProductSku>'||v_seibel_item.v_item_id||'</ext:ProductSku>
						  <ext:Quantity>'||v_seibel_item.v_unit_qty||'</ext:Quantity>
						  <ext:SourceType>'||v_seibel_item.v_s_loc_type||'</ext:SourceType>
						  <ext:SourceId>'||v_seibel_item.v_s_loc||'</ext:SourceId>
						  <ext:FulfillType>'||v_seibel_item.v_d_loc_type||'</ext:FulfillType>
						  <ext:FulfillId>'||v_seibel_item.v_d_loc||' </ext:FulfillId>
						  <ext:EventId>'||v_seibel_item.v_event_id||'</ext:EventId>
						  <ext:EventComment>'||v_seibel_item.v_event_comments||'</ext:EventComment>
                          <ext:EventReferenceId>'||v_seibel_item.v_dlv_conf_id||'</ext:EventReferenceId>
						  <ext:UpdateDate>'||v_seibel_item.v_updated_dttime||'</ext:UpdateDate>
						  </ext:OrderDetailStatus>';
   RETURN lsv_Return_XML;

EXCEPTION
   WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('ERROR OCCURED AT FINAL');
      DBMS_OUTPUT.PUT_LINE('ERROR CODE'||SQLCODE);
      DBMS_OUTPUT.PUT_LINE('ERROR MESSAGE'||SQLERRM);
	  return TO_CLOB(SQLERRM);
END get_Details_XML_COStatusUpdate;

END OMS_SOS_WS_INVOKER;
/
