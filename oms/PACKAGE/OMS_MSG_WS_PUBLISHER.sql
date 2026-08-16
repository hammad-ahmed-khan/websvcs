CREATE OR REPLACE PACKAGE OMS_MSG_WS_PUBLISHER
AS
  ---
  psv_Republish_Stop_Count_Param CONSTANT OMS_SYSTEM_PARAMETERS.PARAMETER_ID%TYPE := 'REPUBLISH_STOP_COUNT';
  ---
  TYPE Republish_Data_RT
  IS
  RECORD
  (
    Seq_No OMS_REPUBLISH_DATA.Seq_No%TYPE,
    XML_MSG OMS_REPUBLISH_DATA.XML_MSG%TYPE,
    Transaction_Key OMS_REPUBLISH_DATA.Transaction_Key%TYPE,
    Application_Id OMS_REPUBLISH_DATA.Application_Id%TYPE,
    Web_Service_Name OMS_WEBSERVICE_URI_DETAIL.WEB_SERVICE_NAME%TYPE,
    URL OMS_WEBSERVICE_URI_DETAIL.URL%TYPE,
      FLAG OMS_SYSTEM_PARAMETERS.PARAMETER_VALUE%TYPE);
  TYPE Unpublished_Data_Coll
  IS
  TABLE OF Republish_Data_RT;
  ----
  --- Function : proc_Message_Republisher
  --- Return   : NULL
  ----
PROCEDURE publish_Messages;

FUNCTION get_Rebublish_Stop_Count
  RETURN OMS_SYSTEM_PARAMETERS.PARAMETER_VALUE%TYPE;

FUNCTION get_Unpublished_Data(isv_republish_stop_count IN OMS_SYSTEM_PARAMETERS.PARAMETER_VALUE%TYPE,
                              ltv_unpublished_msgs IN OUT Unpublished_Data_Coll)
   RETURN BOOLEAN;

PROCEDURE delete_Publish_Data;

PROCEDURE update_Republish_Data(
    inv_seq_no           IN OMS_REPUBLISH_DATA.SEQ_NO%TYPE,
    isv_Republish_Status IN OMS_REPUBLISH_DATA.REPUBLISH_STATUS%TYPE,
    isv_error_msg        IN OMS_REPUBLISH_DATA.ERROR_MSG%TYPE);
END OMS_MSG_WS_PUBLISHER;
/


CREATE OR REPLACE PACKAGE BODY OMS_MSG_WS_PUBLISHER AS
PROCEDURE publish_Messages AS
   lsv_Stop_Parameter_Value OMS_SYSTEM_PARAMETERS.PARAMETER_VALUE%TYPE;
   lcv_unpublished_data Unpublished_Data_Coll;
   theOutputMessage CLOB := NULL;
   lxv_response OMS_REPUBLISH_DATA.ERROR_MSG%TYPE;
   l_response_payload XMLType; 
   L_test clob;
   lrt_republish_record Republish_Data_RT;
   v_SOAP CLOB;
   l_response CLOB;
BEGIN
   ---
   --- Step 1: Get the Republishing Stop value from OMS_SYSTEM_PARAMETERS table.
   ---

   lsv_Stop_Parameter_Value:=get_Rebublish_Stop_Count();
   ---
   --- Step 2: Fetch the unpublished data from OMS_PUBLISH_WS_DATA table.
   ---
   if get_Unpublished_Data(lsv_Stop_Parameter_Value, 
                           lcv_unpublished_data) = FALSE then
      DBMS_OUTPUT.PUT_LINE('Error occured while calling function get_Unpublished_Data');
   end if;

   ---
   --- Step 3: Loop through and Invoke the generic_oms_soap_call funtion from OMS_WS_INVOKER package.
   ---

   FOR indx in 1..lcv_unpublished_data.count LOOP
      theOutputMessage:=NULL;                               --- Reinitialize the Output Message.
      lrt_republish_record :=lcv_unpublished_data(indx);    --- Get the record type from the collection.

      IF lrt_republish_record.FLAG = 'Y' THEN
        l_response_payload := OMS_WS_INVOKER.generic_oms_soap_call(lrt_republish_record.XML_MSG,lrt_republish_record.URL,NULL, v_SOAP);  
      END IF;

      ---
      --- Step 4: Check if a Soap Fault has occured in the response.
      ---
      select DBMS_LOB.SUBSTR(l_response_payload.EXTRACT('UpdateOrderStatusResponse/UpdateOrderStatusResult/text()' ,'xmlns="'||'http://www.extra.com/Services/OmsStatus'||'"').getClobVal(),2000,1)
        into lxv_response
        from dual;

      ---
      --- Step 5: If NO SOAP Fault is recieved in the response then update the Republish_Status field value as 'P'.
      ---         Else only update the timestamps and increment the Attempt_Cnt by 1
      IF  lxv_response is NOT NULL THEN 
         IF lxv_response = 'Success' OR lxv_response = 'ORPOS' THEN
           update_Republish_Data(lrt_republish_record.seq_no,'P',NULL); -- Successfully Published
         ELSE
           update_Republish_Data(lrt_republish_record.seq_no,'F',lxv_response); -- Failed Again
         END IF;
      ELSE 
         IF l_response_payload.existsNode('/S:Fault', 'xmlns:S="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns4="http://www.w3.org/2003/05/soap-envelope"')=1 THEN
            theOutputMessage := 'SOAP Error :'|| (l_response_payload.extract('/S:Fault/faultstring/text()', 'xmlns:S="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns4="http://www.w3.org/2003/05/soap-envelope"').getStringVal());         
            update_Republish_Data(lrt_republish_record.seq_no,'F',DBMS_LOB.SUBSTR(theOutputMessage,2000,1)); 
         ELSE
            update_Republish_Data(lrt_republish_record.seq_no,'P',NULL);
         END IF;
      END IF;  
   END LOOP;
   COMMIT;
EXCEPTION
   WHEN OTHERS THEN
      ROLLBACK;
      DBMS_OUTPUT.PUT_LINE('ERROR OCCURED in Proc publish_Messages');
      DBMS_OUTPUT.PUT_LINE('ERROR CODE'||SQLCODE);
      DBMS_OUTPUT.PUT_LINE('ERROR MESSAGE'||SQLERRM);
END publish_Messages;

FUNCTION get_Rebublish_Stop_Count
   RETURN OMS_SYSTEM_PARAMETERS.PARAMETER_VALUE%TYPE AS

   lsv_Republish_Stop_Value OMS_SYSTEM_PARAMETERS.PARAMETER_VALUE%TYPE;
BEGIN
   SELECT PARAMETER_VALUE INTO lsv_Republish_Stop_Value
     FROM OMS_SYSTEM_PARAMETERS
    WHERE PARAMETER_NAME = psv_Republish_Stop_Count_Param;

   IF SQL%ROWCOUNT=0 THEN
      dbms_output.put_line('System Parameters not configured for '||psv_Republish_Stop_Count_Param);
      RAISE NO_DATA_FOUND;
   END IF;
   RETURN lsv_Republish_Stop_Value;
EXCEPTION
   WHEN NO_DATA_FOUND THEN
      DBMS_OUTPUT.PUT_LINE('ERROR OCCURED in function get_Rebublish_Stop_Count due to No data found');
      DBMS_OUTPUT.PUT_LINE('ERROR CODE'||SQLCODE);
      DBMS_OUTPUT.PUT_LINE('ERROR MESSAGE'||SQLERRM);
	  return SQLERRM;
   WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('ERROR OCCURED in function get_Rebublish_Stop_Count');
      DBMS_OUTPUT.PUT_LINE('ERROR CODE'||SQLCODE);
      DBMS_OUTPUT.PUT_LINE('ERROR MESSAGE'||SQLERRM);
	  return SQLERRM;
END get_Rebublish_Stop_Count;

FUNCTION get_Unpublished_Data(isv_republish_stop_count IN OMS_SYSTEM_PARAMETERS.PARAMETER_VALUE%TYPE,
                              ltv_unpublished_msgs IN OUT Unpublished_Data_Coll)
   RETURN BOOLEAN AS

   CURSOR c_unpublished_msgs IS
      (SELECT SEQ_NO,XML_MSG, Transaction_Key, Application_Id
               , Web_Service_Name, URL, (SELECT PARAMETER_VALUE
                                           FROM OMS_SYSTEM_PARAMETERS
                                          WHERE parameter_id = 'OMS_SYSTEM_OPTION'
                                            and parameter_name = 'SIEBEL_WS_REPUB_IND') FLAG
         FROM OMS_PUBLISH_WS_DATA repub, OMS_WEBSERVICE_URI_DETAIL wsd
        WHERE repub.WEB_SERVICE_ID = wsd.WEB_SERVICE_ID
          AND repub.Republish_Status = 'N'
          AND repub.attempt_cnt<isv_republish_stop_count
--            AND TRANSACTION_KEY = 'WEB903982641'
--          AND XML_MSG LIKE '%RECEVING%'
          AND wsd.WEB_SERVICE_NAME in ('SIEBEL_ORDER_CANCEL',
                                       'SIEBEL_ORDER_FEED',
                                       'SIEBEL_STATUS_UPDATE',
                                       'SOA_CO_STATUS_UPDATE',
                                       'RECEIPT_SIEBEL_CO_STATUS_UPDATE',
                                       'ASNOUT_SIEBEL_CO_STATUS_UPDATE')
                                        ) order by seq_no;


 --  ltv_unpublished_msgs Unpublished_Data_Coll;
BEGIN
   -- Calling delete_Publish_Data procedure to delete all published data from OMS_PUBLISH_WS_DATA
--   delete_Publish_Data();
   OPEN c_unpublished_msgs;
   FETCH c_unpublished_msgs
   BULK COLLECT INTO ltv_unpublished_msgs LIMIT 5000;
   CLOSE c_unpublished_msgs;
   RETURN TRUE;
EXCEPTION
   WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('ERROR OCCURED in function get_Unpublished_Data');
      DBMS_OUTPUT.PUT_LINE('ERROR CODE'||SQLCODE);
      DBMS_OUTPUT.PUT_LINE('ERROR MESSAGE'||SQLERRM);
      IF c_unpublished_msgs%ISOPEN THEN
        CLOSE c_unpublished_msgs;
      END IF;
	  RETURN FALSE;
END get_Unpublished_Data;

PROCEDURE delete_Publish_Data
AS
BEGIN
   FOR C_seqNo in (SELECT SEQ_NO
                     FROM OMS_PUBLISH_WS_DATA
                    WHERE republish_status = 'P')
   LOOP
      --DBMS_OUTPUT.PUT_LINE('C_seqNo :' || C_seqNo.SEQ_NO);
      DELETE FROM OMS_PUBLISH_WS_DATA
       WHERE SEQ_NO = C_seqNo.SEQ_NO;
      COMMIT;
   END LOOP;
EXCEPTION
    WHEN OTHERS THEN
	  ROLLBACK;
      DBMS_OUTPUT.PUT_LINE('ERROR OCCURED');
      DBMS_OUTPUT.PUT_LINE('ERROR CODE'||SQLCODE);
      DBMS_OUTPUT.PUT_LINE('ERROR MESSAGE'||SQLERRM);
END delete_Publish_Data;

PROCEDURE update_Republish_Data(
      inv_seq_no           IN OMS_REPUBLISH_DATA.SEQ_NO%TYPE,
      isv_Republish_Status IN OMS_REPUBLISH_DATA.REPUBLISH_STATUS%TYPE,
      isv_error_msg        IN OMS_REPUBLISH_DATA.ERROR_MSG%TYPE)
    AS

BEGIN

--- ************************* FAILURE MESSAGE **************************************
----- INSERT ENTRY INTO REPUBLISH TABLE


IF isv_Republish_Status = 'F' THEN
--- ************************* FAILURE MESSAGE **************************************
INSERT INTO OMS_REPUBLISH_DATA (SEQ_NO, APPLICATION_ID, FIRST_ATTEMPT_DATETIME, TRANSACTION_KEY, XML_MSG, WEB_SERVICE_ID, ATTEMPT_CNT, ERROR_MSG, REPUBLISH_STATUS, LAST_ATTEMPT_TIME)
SELECT
SEQ_NO, APPLICATION_ID, FIRST_ATTEMPT_DATETIME, TRANSACTION_KEY, XML_MSG, WEB_SERVICE_ID, 1, isv_error_msg, 'F', sysdate
FROM OMS_PUBLISH_WS_DATA where SEQ_NO = inv_seq_no AND isv_Republish_Status = 'F';

----- DELETE THE ENTRY FROM OMS_PUBLISH_WS_DATA TABLE
DELETE FROM OMS_PUBLISH_WS_DATA where SEQ_NO = inv_seq_no AND isv_Republish_Status = 'F';

ELSE
--- ************************* SUCESSS MESSAGE **************************************
INSERT INTO OMS_PUBLISH_WS_DATA_LOG (SEQ_NO, APPLICATION_ID, FIRST_ATTEMPT_DATETIME, TRANSACTION_KEY, XML_MSG, WEB_SERVICE_ID, ATTEMPT_CNT, ERROR_MSG, REPUBLISH_STATUS, LAST_ATTEMPT_TIME)
SELECT
SEQ_NO, APPLICATION_ID, FIRST_ATTEMPT_DATETIME, TRANSACTION_KEY, XML_MSG, WEB_SERVICE_ID, 1, 'NO ERROR', 'P', sysdate
FROM OMS_PUBLISH_WS_DATA where SEQ_NO = inv_seq_no AND isv_Republish_Status = 'P';

----- DELETE THE ENTRY FROM OMS_PUBLISH_WS_DATA TABLE
DELETE FROM OMS_PUBLISH_WS_DATA where SEQ_NO = inv_seq_no AND isv_Republish_Status = 'P';

END IF;

commit;
--    UPDATE OMS_PUBLISH_WS_DATA
--       SET republish_status = isv_Republish_Status
--          ,attempt_cnt = attempt_cnt+1
--          ,last_attempt_time = sysdate
--          ,error_msg = NVL(isv_error_msg,'PUBLISHED')
--     WHERE SEQ_NO = inv_seq_no
--     AND isv_Republish_Status = 'P';
--    commit;

EXCEPTION
   WHEN OTHERS THEN
      ROLLBACK;
      DBMS_OUTPUT.PUT_LINE('ERROR OCCURED');
      DBMS_OUTPUT.PUT_LINE('ERROR CODE'||SQLCODE);
      DBMS_OUTPUT.PUT_LINE('ERROR MESSAGE'||SQLERRM);
END update_Republish_Data;

END OMS_MSG_WS_PUBLISHER;
/
