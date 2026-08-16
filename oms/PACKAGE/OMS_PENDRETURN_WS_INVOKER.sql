CREATE OR REPLACE PACKAGE OMS_PENDRETURN_WS_INVOKER AS
---
--- Following CONSTANT numeric variables have been defined for WS that will be invoked.
--- The constant values MUST MATCH with the values of the Web Service Id in the OMS table
--- OMS_WEBSERVICE_URI_DETAIL
---

   lsv_WS_Name CONSTANT OMS_WEBSERVICE_URI_DETAIL.WEB_SERVICE_NAME%TYPE := 'SOA_CO_STATUS_UPDATE';

----
--- Function : f_REC_Siebel_COStatusUpdate
--- Inputs   : v_cust_ord_no - External Customer Order No
--- Return   : returns message from the invoked Siebel WS
----
FUNCTION f_REC_Siebel_COStatusUpdate(v_message_id IN OMS_RMA_RCV_DTL.message_id%TYPE)
RETURN VARCHAR2;

FUNCTION generic_oms_soap_call(p_payload    IN CLOB,
                               p_target_url IN OMS_WEBSERVICE_URI_DETAIL.URL%TYPE,
                               p_soap_action IN VARCHAR2 DEFAULT 'process',
							   p_soap_envelope  IN OUT CLOB
                              )
RETURN XMLTYPE;


FUNCTION get_WS_URL_Details(p_WS_Name IN OMS_WEBSERVICE_URI_DETAIL.WEB_SERVICE_NAME%TYPE)
RETURN OMS_WEBSERVICE_URI_DETAIL%ROWTYPE;

FUNCTION get_Header_XML_COStatusUpdate (v_message_id IN OMS_RMA_RCV_DTL.message_id%TYPE, v_target_namespace IN OMS_WEBSERVICE_URI_DETAIL.TARGET_NAMESPACE%TYPE)
RETURN CLOB;

FUNCTION get_Details_XML_COStatusUpdate (v_message_id IN OMS_RMA_RCV_DTL.message_id%TYPE)
RETURN CLOB;

END OMS_PENDRETURN_WS_INVOKER;
/


CREATE OR REPLACE PACKAGE BODY OMS_PENDRETURN_WS_INVOKER
AS
FUNCTION generic_oms_soap_call(p_payload    IN CLOB,
                               p_target_url IN OMS_WEBSERVICE_URI_DETAIL.URL%TYPE,
                               p_soap_action IN VARCHAR2 DEFAULT 'process',
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
   l_soap_request  CLOB; --varchar2(30000);
   l_soap_response CLOB; --varchar2(30000);
   l_fault_soap_response   CLOB;
   http_req utl_http.req;
   http_resp utl_http.resp;
   http_failure EXCEPTION;
   pragma       EXCEPTION_INIT(http_failure, -29273);
   L_fail_cnt   NUMBER := 0;
   L_target_url  OMS_WEBSERVICE_URI_DETAIL.URL%TYPE;
   cursor soa_url is 
      SELECT  SUBSTR(p_target_url, 1, LENGTH(p_target_url)-5)  
    FROM DUAL;

BEGIN
    ---
	---Fault response to be returned for the failed process
   l_fault_soap_response := '<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
                                 <S:Body>
                                    <S:Fault xmlns:ns4="http://www.w3.org/2003/05/soap-envelope">
                                       <faultcode>S:Server</faultcode>
									   <faultstring>OMS_PENDRETURN_WS_INVOKER.generic_oms_soap_call function Processing Failed</faultstring>
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
   ---dbms_output.put_line('SOAP REQUEST : '||l_soap_request);

   p_soap_envelope := replace(c_soap_envelope, '**payload**', p_payload);
          open soa_url; 
          fetch soa_url into L_target_url;
          close soa_url;
   http_req:= utl_http.begin_request(L_target_url,
                                     'POST',
                                     'HTTP/1.1'
                                    );
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

	WHEN utl_http.end_of_body  THEN
		utl_http.end_response(http_resp);
		DBMS_OUTPUT.PUT_LINE('HTTP Request Processing');
      return XMLType.createXML(l_fault_soap_response).extract( '/S:Envelope/S:Body/child::node()'
                        , 'xmlns:S="http://schemas.xmlsoap.org/soap/envelope/"'
                        );
   WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('ERROR AT GENERIC SOAP CALL');
      DBMS_OUTPUT.PUT_LINE('ERROR CODE'||SQLCODE);
      DBMS_OUTPUT.PUT_LINE('ERROR MESSAGE'||SQLERRM);
      -- Include procedure call to insert error message
      P_INS_ERROR_DTL('OMS_PENDRETURN_WS_INVOKER','generic_oms_soap_call',null,null,null,null,null,null,SQLCODE,SQLERRM,sysdate);
      commit;
      return XMLType.createXML(l_fault_soap_response).extract( '/S:Envelope/S:Body/child::node()'
                        , 'xmlns:S="http://schemas.xmlsoap.org/soap/envelope/"'
                        );
END generic_oms_soap_call;


FUNCTION get_WS_URL_Details(p_WS_Name IN OMS_WEBSERVICE_URI_DETAIL.WEB_SERVICE_NAME%TYPE)
  RETURN OMS_WEBSERVICE_URI_DETAIL%ROWTYPE AS
 /***********************************************************************************************************************************
  CREATED BY  : Shylesh
  CREATED ON  : 24/03/2015
  MODIFIED BY : Shylesh
  MODIFIED ON : 24/03/2015
  LOGIC       : The function is a generic funtion that returns the Web Service Details for the Web Service id passed as input.
 ***********************************************************************************************************************************/
   theReturnData OMS_WEBSERVICE_URI_DETAIL%ROWTYPE;
  oms_error_message       VARCHAR2(3000);
  BEGIN
      SELECT * INTO theReturnData
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
FUNCTION f_REC_Siebel_COStatusUpdate(v_message_id IN OMS_RMA_RCV_DTL.MESSAGE_ID%TYPE)
RETURN VARCHAR2 AS
  /***********************************************************************************************************************************
  CREATED BY  : Shylesh
  CREATED ON  : 26/06/2015
  LOGIC       : This function gets the details of the Pend Return transaction from relevant OMS tables based on the External Cust Order passed as input
                The data collected is then formatted into and XML document as specified by the WSDL pertaining to the WS invoked
                All the connection details pertaining to the WS invoked is also collected from the OMS_WEBSERVICE_URI_DETAIL
                XML Document prepared is then passed on to the WS.
                The response received from the WS is then returned to the calling object.
 ***********************************************************************************************************************************/
   l_response_payload       XMLType;
   l_payload                CLOB ;--VARCHAR2(20000); -- Increase the size depending on the payload length
   l_payload_detail         CLOB; --VARCHAR2(10000);
   l_parent_element         VARCHAR2(50);
   theOutputMessage         VARCHAR2(3000);
   l_tran_seq               NUMBER(10);
   l_payload_msg            CLOB;
    l_ins_payload                CLOB ;

   l_Test_response      VARCHAR2(5000);  -- Used to output the response message. The code is commented out.
   l_count              NUMBER       :=1;

   oms_error_message       VARCHAR2(1000);

   theWSConnDetail OMS_WEBSERVICE_URI_DETAIL%ROWTYPE;

   lsv_Request_parent_element VARCHAR2(300) :='UpdateOrderStatus';
   lsv_Response_parent_element VARCHAR2(300) :='UpdateOrderStatusResponse';

   L_application_id         OMS_CUST_ORD_HEAD.APPLICATION_ID%TYPE;
   L_cust_order_no          OMS_CUST_ORD_HEAD.CUST_ORDER_NO%TYPE;

   c_soap_envelope CLOB := '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:oms="http://www.extra.com/Services/OmsStatus" xmlns:ext="http://schemas.datacontract.org/2004/07/eXtra.Services.Oms">
                            <soap:Body>**payload**</soap:Body>
                            </soap:Envelope>';

   CURSOR c_get_repub_ins_data IS
      SELECT distinct ocoh.application_id,
             ocoh.cust_order_no
        FROM oms_cust_ord_head ocoh,
             oms_rma_rcv_dtl orr
       WHERE orr.message_id =v_message_id
         AND (ocoh.cust_order_no = orr.cust_order_no
              OR
              (ocoh.cust_order_no = SUBSTR(orr.cust_order_no,1,INSTR(orr.cust_order_no,'-') - 1)
              AND ocoh.sub_cust_order_no = SUBSTR(orr.cust_order_no,INSTR(orr.cust_order_no,'-') + 1, LENGTH(orr.cust_order_no)))
             )
         AND ocoh.status = 'S'
         AND ocoh.ORDER_REQUESTOR_ID IN ('19010','20010','30010');

BEGIN

	open c_get_repub_ins_data ;
	loop 
	fetch c_get_repub_ins_data into L_application_id , L_cust_order_no;
	EXIT when c_get_repub_ins_data%notfound;


	if(L_application_id<>'ORPOS') then 

    --- Get the WS URL Connection Details for PENDING RETURN SIEBEL CO Statuts Update Web Service from the database.
   theWSConnDetail := get_WS_URL_Details(lsv_WS_Name);

   l_payload := get_Header_XML_COStatusUpdate(v_message_id,theWSConnDetail.Target_Namespace);

   ---dbms_output.put_line('Payload Header  : '||l_payload);
   ---
   --- Loop through the item table to get associated item records
   ---
   l_payload_detail := get_Details_XML_COStatusUpdate(v_message_id);
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
      oms_error_message := 'No data found in OMS_CUST_ORD_HEAD OR OMS_CUST_ORD_ITEM Table for OMS_CUST_ORDER_NO : '||v_message_id;
      ---OMS_UTIL.ERROR_REPORTING('OMS_ASNOUT_SIEBEL_WS_INVOKER',oms_error_message);
      RAISE NO_DATA_FOUND;
   END IF;
    ---
    --- The payload will be send to the generic soap_call function with appropriate inputs.
    ---
--   l_response_payload := generic_oms_soap_call(l_payload, theWSConnDetail.URL,null,l_payload_msg); -- theWSConnDetail.Target_NameSpace||theWSConnDetail.Parent_Element);

     l_ins_payload := replace(c_soap_envelope, '**payload**', l_payload);

    --- Get(extract) each of the values into a variable of the Record Type
    --- Once All the values are populated return the record. Uncomment the l_Test_Response below to see the XML Data
    -- l_Test_response := l_response_payload.getStringVal();


    ---
    --- Check if the Response contains a SOAP Fault message
    ---
    
   SELECT oms_republish_dataseq.nextval
     INTO l_tran_seq
     FROM DUAL;
     
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
    
--   IF l_response_payload.existsNode('/S:Fault', 'xmlns:S="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns4="http://www.w3.org/2003/05/soap-envelope"')=1 THEN
--      theOutputMessage := 'SOAP Error :'|| (l_response_payload.extract('/S:Fault/faultstring/text()', 'xmlns:S="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns4="http://www.w3.org/2003/05/soap-envelope"').getStringVal());

   ---
   --   Calling the procedure to insert the faliure message into REPUBLISH DATA table
   ---

   -- Fetching the sequence value from the sequence


      --OPEN c_get_repub_ins_data;
      --FETCH c_get_repub_ins_data INTO L_application_id,L_cust_order_no;
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

--   ELSE
    ---
    --- If No SOAP Fault is raised then proceed with the successful processing of the response message.
    ---
--      WHILE l_response_payload.existsNode('/'||lsv_Response_parent_element||'[' || TO_CHAR(l_count) || ']', 'xmlns="'|| theWSConnDetail.Target_Namespace||'"') = 1
--      LOOP
--         theOutputMessage := (l_response_payload.extract('/'||lsv_Response_parent_element||'/UpdateOrderStatusResult[' || TO_CHAR(l_count) || ']/text()','xmlns="'|| theWSConnDetail.Target_Namespace||'"').getStringVal());
--         l_count          := l_count + 1;
        --dbms_output.put_line('The Output Message :'||theOutputMessage);
--      END LOOP;
--   END IF;
    ---
    --- Return the message back
    ---
   END  IF; 
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

FUNCTION get_Header_XML_COStatusUpdate (v_message_id IN OMS_RMA_RCV_DTL.message_id%TYPE, v_target_namespace IN OMS_WEBSERVICE_URI_DETAIL.TARGET_NAMESPACE%TYPE)
RETURN CLOB AS
   lsv_Return_XML CLOB;
   CURSOR  c_header(v_message_id IN OMS_RMA_RCV_DTL.MESSAGE_ID%TYPE) IS
      SELECT ocoh.application_id application_id,
             ocoh.cust_order_no      CUST_ORDER_NO,
             ocoh.SUB_CUST_ORDER_NO  SUB_CUST_ORDER_NO,
             ocoh.OMS_CUST_ORD_NO    OMS_CUST_ORD_NO,
             ocoh.CUST_ID            CUST_ID,
             to_char(SYSDATE , 'YYYY-MM-DD')||'T'||to_char(SYSDATE , 'HH24:MI:SS')  consumer_dly_time,
             to_char(SYSDATE , 'YYYY-MM-DD')||'T'||to_char(SYSDATE , 'HH24:MI:SS')  close_datetime
        FROM oms_cust_ord_head ocoh,
             oms_rma_rcv_dtl orr
       WHERE orr.message_id =v_message_id
         AND (ocoh.cust_order_no = orr.cust_order_no
              OR
              ocoh.cust_order_no = SUBSTR(orr.cust_order_no,1,INSTR(orr.cust_order_no,'-') - 1)
             )
         AND ocoh.status = 'S'
		 AND ocoh.ORDER_REQUESTOR_ID IN ('19010','20010','30010');
BEGIN
   FOR c1 IN c_header(v_message_id)
   LOOP
      lsv_Return_XML:= '<oms:UpdateOrderStatus>
						<oms:orderStatus> 
                        <ext:EnitityId>eXtra</ext:EnitityId>
                        <ext:ApplicationId>'||c1.application_id||'</ext:ApplicationId>
                        <ext:OrderId>'||c1.cust_order_no||'</ext:OrderId>
                        <ext:SubOrderId>'||c1.sub_cust_order_no||'</ext:SubOrderId>
                        <ext:OmsOrderId>'||c1.oms_cust_ord_no||'</ext:OmsOrderId>
                        <ext:DeliveryDate>'||c1.consumer_dly_time||'</ext:DeliveryDate>
                        <ext:UpdateDate>'||c1.close_datetime||'</ext:UpdateDate>';

   END LOOP;   --- End Header Loop
   RETURN lsv_Return_XML;
EXCEPTION
   WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('ERROR OCCURED');
      DBMS_OUTPUT.PUT_LINE('ERROR CODE'||SQLCODE);
      DBMS_OUTPUT.PUT_LINE('ERROR MESSAGE'||SQLERRM);
      IF c_header%ISOPEN THEN
         CLOSE c_header;
      END IF;
	  return TO_CLOB(SQLERRM);
END get_Header_XML_COStatusUpdate;

FUNCTION get_Details_XML_COStatusUpdate (v_message_id in OMS_RMA_RCV_DTL.MESSAGE_ID%TYPE)
RETURN CLOB AS

   lsv_Return_XML CLOB; ---VARCHAR2(5000);
   CURSOR c_item(v_message_id in OMS_RMA_RCV_DTL.MESSAGE_ID%TYPE) IS
      SELECT orr.item                 ITEM,
             orr.line_item_nbr        LINE_ITEM_NO,
             SUM(orr.received_qty)    QTY,
             'RTN'                    EVENT_ID,
             'RETURNED'               EVENT_COMMENTS,
             '1'                     OMS_DLV_CONF_ID,
             'S'                     SOURCE_LOC_TYPE,
             '19010'                     SOURCE_LOC,
             'WH'                     FULFILL_LOC_TYPE,
             opd.physical_wh          FULFILL_LOC,
             to_char(SYSDATE , 'YYYY-MM-DD')||'T'||to_char(SYSDATE , 'HH24:MI:SS')  update_datetime,
             'RETURNED'              REMARKS
         FROM oms_rma_rcv_dtl orr,
			        OMS_PENDRT_DESC opd
        WHERE orr.message_id = v_message_id
          AND orr.rma_id=opd.rma_nbr
          AND orr.oms_cust_ord_no IN (SELECT orr.oms_cust_ord_no
                                        FROM  oms_rma_rcv_dtl orr,
                                              oms_cust_ord_head ocoh
                                        WHERE (ocoh.cust_order_no = orr.cust_order_no
                                               OR
                                               ocoh.cust_order_no = SUBSTR(orr.cust_order_no,1,INSTR(orr.cust_order_no,'-') - 1)
                                              )
                                        AND ocoh.status = 'S'
										AND ocoh.ORDER_REQUESTOR_ID IN ('19010','20010','30010')
                                        AND orr.message_id =v_message_id)
    GROUP BY orr.item               ,
             orr.line_item_nbr      ,
             'RT'                   ,
             'RETURNED'             ,
             null                   ,
             null                   ,
             null                   ,
             'WH'                   ,
             opd.physical_wh        ,
             sysdate                ,
             'RETURNED'
    ORDER BY orr.item;

BEGIN
  FOR C2 IN c_item(v_message_id)
      LOOP
     lsv_Return_XML:= lsv_Return_XML||'
     '||'<ext:OrderDetailStatus>
            <ext:OrderDetailId>'||c2.line_item_no||'</ext:OrderDetailId>
            <ext:ProductSku>'||c2.item||'</ext:ProductSku>
            <ext:Quantity>'||c2.qty||'</ext:Quantity>
            <ext:SourceType>'||c2.source_loc_type||'</ext:SourceType>
            <ext:SourceId>'||c2.source_loc||'</ext:SourceId>
            <ext:FulfillType>'||c2.fulfill_loc_type||'</ext:FulfillType>
            <ext:FulfillId>'||c2.fulfill_loc||'</ext:FulfillId>
            <ext:EventId>'||c2.event_id||'</ext:EventId>
            <ext:EventComment>'||c2.EVENT_COMMENTS||'</ext:EventComment>
            <ext:EventReferenceId>'||c2.oms_dlv_conf_id||'</ext:EventReferenceId>
            <ext:UpdateDate>'||c2.update_datetime||'</ext:UpdateDate>
          </ext:OrderDetailStatus>';
          --dbms_output.put_line('Length of Buffer '|| length(lsv_Return_XML));
      END LOOP; --- End Item Loop
   RETURN lsv_Return_XML;
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('ERROR OCCURED AT FINAL');
    DBMS_OUTPUT.PUT_LINE('ERROR CODE'||SQLCODE);
    DBMS_OUTPUT.PUT_LINE('ERROR MESSAGE'||SQLERRM);
    IF c_item%ISOPEN THEN
      CLOSE c_item;
    END IF;
	return TO_CLOB(SQLERRM);
END get_Details_XML_COStatusUpdate;

END OMS_PENDRETURN_WS_INVOKER;
/
