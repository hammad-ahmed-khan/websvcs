create or replace PACKAGE BODY       OMSSUB_RECEIVING AS
PROCEDURE CONSUME (O_status_code          IN OUT  VARCHAR2,
                   O_error_message        IN OUT  VARCHAR2,
                   I_message              IN      RIB_OBJECT,
                   I_message_type         IN      VARCHAR2,
                   O_rib_otbdesc_rec         OUT  RIB_OBJECT,
                   O_rib_error_tbl           OUT  RIB_ERROR_TBL)
   IS


   L_program           VARCHAR2(61)    := 'OMSSUB_RECEIVING.CONSUME';
   PROGRAM_ERROR       EXCEPTION;
   INV_MESSAGE_TYPE    EXCEPTION;
   L_message_id        OMS_RECEIPT.MESSAGE_ID%TYPE;
   L_oms_cust_ord_no   OMS_CUST_ORD_HEAD.OMS_CUST_ORD_NO%TYPE;
   L_cust_ord_no       OMS_CUST_ORD_HEAD.CUST_ORDER_NO%TYPE;
   L_combination_id    OMS_CO_FULFILL_DETAIL.COMBINATION_ID%TYPE;
   L_fulfill_seq       OMS_CO_FULFILL_DETAIL.fulfill_order_no%TYPE;

   L_rib_receiptdesc_rec "RIB_ReceiptDesc_REC" := null;

   L_rib_receipt_rec "RIB_Receipt_REC" := null;
   L_rib_receiptdtl_rec "RIB_ReceiptDtl_REC" := null;
   L_rib_receiptcartondtl_rec "RIB_ReceiptCartonDtl_REC" := null;
   L_rib_receiptoverage_rec "RIB_ReceiptOverage_REC" := null;
   L_rib_receiptoveragedtl_rec "RIB_ReceiptOverageDtl_REC" := null;

   TYPE item_rec IS RECORD 
   ( T_item               OMS_CO_FULFILL_DETAIL.ITEM%TYPE,
     T_dc_dest_id         OMS_RECEIPT.DC_DEST_ID%TYPE,
     T_quantity           OMS_CO_FULFILL_DETAIL.FULFILL_DELIVER_QTY%TYPE,
     T_po_nbr             OMS_RECEIPT.po_nbr%TYPE,
     T_fulfil_no          OMS_CO_FULFILL_DETAIL.fulfill_order_no%TYPE,
     T_line_no            OMS_CO_FULFILL_DETAIL.line_no%TYPE,
     T_req_qty            OMS_CO_FULFILL_DETAIL.FULFILL_REQ_QTY%TYPE,
     T_conf_qty           OMS_CO_FULFILL_DETAIL.FULFILL_CONF_QTY%TYPE,
     T_deliver_qty        OMS_CO_FULFILL_DETAIL.FULFILL_DELIVER_QTY%TYPE,
     T_cancel_qty         OMS_CO_FULFILL_DETAIL.FULFILL_CANCEL_QTY%TYPE,
     T_from_loc           OMS_CO_FULFILL_DETAIL.SOURCE_LOC%TYPE,
     T_event_id           OMS_CUST_ORD_LOG.EVENT_ID%TYPE,
     T_event_comm         OMS_CUST_ORD_LOG.EVENT_COMMENTS%TYPE);
         
   L_item_rec     item_rec;
   
   TYPE tbl_item IS TABLE OF l_item_rec%TYPE INDEX BY PLS_INTEGER;
   tbl_item_rec   tbl_item; 

   --Local Variable for C_FULFILL_DETAIL
   L_req_qty            OMS_CO_FULFILL_DETAIL.FULFILL_REQ_QTY%TYPE;
   L_dc_dest_id         OMS_RECEIPT.DC_DEST_ID%TYPE;
   L_conf_qty           OMS_CO_FULFILL_DETAIL.FULFILL_CONF_QTY%TYPE;
   L_deliver_qty        OMS_CO_FULFILL_DETAIL.FULFILL_DELIVER_QTY%TYPE;
   L_del_conf_id        OMS_CUST_ORD_LOG.OMS_DLV_CONF_ID%TYPE;
   L_cancel_qty         OMS_CO_FULFILL_DETAIL.FULFILL_CANCEL_QTY%TYPE;
   L_fulfil_no          OMS_CO_FULFILL_DETAIL.fulfill_order_no%TYPE;
   L_line_no            OMS_CO_FULFILL_DETAIL.line_no%TYPE;
   L_line_no_new        OMS_CO_FULFILL_DETAIL.line_no%TYPE;
   L_item               OMS_CO_FULFILL_DETAIL.ITEM%TYPE;
   L_quantity           OMS_CO_FULFILL_DETAIL.FULFILL_DELIVER_QTY%TYPE;
   L_counter            NUMBER(10):=0;
   L_seq_no             OMS_CUST_ORD_LOG.LOG_SEQ_NO%TYPE;
   L_event_id           OMS_CUST_ORD_LOG.EVENT_ID%TYPE;
   L_event_comm         OMS_CUST_ORD_LOG.EVENT_COMMENTS%TYPE;
   L_po_nbr             OMS_RECEIPT.po_nbr%TYPE;
   L_process_ind        VARCHAR2(1) :='N';
   L_doc_type           OMS_RECEIPT.DOCUMENT_TYPE%TYPE;
   L_doc_no             OMS_RECEIPT.REF_DOC_NO%TYPE;
   L_spare_item         OMS_SPARE_PART_HEADER.item_id%TYPE;
   L_spare_qty          OMS_SPARE_PART_HEADER.QUANTITY_REQUESTED%TYPE;
   L_spare_loc          OMS_RECEIPT.FROM_LOC%TYPE;
   L_src_loc            OMS_RECEIPT.FROM_LOC%TYPE;
   L_spare_po           OMS_RECEIPT.po_nbr%TYPE;
   L_found              VARCHAR2(1);
   L_from_loc           OMS_CO_FULFILL_DETAIL.SOURCE_LOC%TYPE;
   L_oms_service_req_id NUMBER(15);
   L_audit_seq          OMS_SPARE_PART_AUDIT.OMS_AUDIT_SEQ_ID%TYPE;
   L_seibel_out         VARCHAR2(1000);
   L_sim_invadj_ws      VARCHAR2(1000);
   L_sim_ws_co          VARCHAR2(7000);
   L_rms_ws_co          VARCHAR2(7000);
   L_response           VARCHAR2(100);
   L_cnf_qty            NUMBER(10);
   L_dlv_qty            NUMBER(10);
   L_error              VARCHAR2(1) :='N';
   L_recv_qty           NUMBER(10);
   L_qty_tsf_rsv        NUMBER(10);
   L_qty_can            NUMBER(10);
   L_delv_qty           NUMBER(10);
   L_receive_qty        NUMBER(10);
   L_application_cnt    NUMBER(10);

   L_cnt                NUMBER       := 0;       
   L_item_check         VARCHAR2(25) := '0';          
   L_item_check_array   OMS_CO_FULFILL_DETAIL.ITEM%TYPE := '0';          
   index_value          VARCHAR2(25) := '0' ;
   
   -- Assume the customer_order_no will be suffix with the sub cust order number as
   -- last 3 digits

   CURSOR C_OMS_CUS_ORD_NO (L_message_id oms_receipt.message_id%TYPE)IS
      SELECT distinct oms_cust_ord_no
        FROM oms_cust_ord_head ocoh,
             oms_receipt orr,
             oms_receipt_dtl ord
       WHERE orr.message_id = L_message_id
         AND orr.message_id = ord.message_id
         AND ord.po_nbr = orr.po_nbr
         AND (ocoh.cust_order_no = orr.cust_order_nbr 
              OR
              (ocoh.cust_order_no = SUBSTR(orr.cust_order_nbr,1,INSTR(orr.cust_order_nbr,'-') - 1)
              AND ocoh.sub_cust_order_no = SUBSTR(orr.cust_order_nbr,INSTR(orr.cust_order_nbr,'-') + 1, LENGTH(orr.cust_order_nbr)))
             )
         AND ocoh.status = 'S';


   -- To check whether the transfer_id exists for the transfer id
   -- for this RECEIPT message


   CURSOR C_SPARE_PART IS
      SELECT DISTINCT 'Y',
             ospf.oms_service_req_seq_id
        FROM oms_spare_part_fulfill ospf,
             oms_spare_part_header  osph  
       WHERE ospf.tran_id = L_rib_receipt_rec.po_nbr
         AND ospf.oms_service_req_seq_id = osph.oms_service_req_seq_id;

   --cursor consists of the details  for oms_cust_ord_log and
   --oms_cust_ord_log_item tables

   CURSOR C_FULFILL_DETAIL (L_message_id oms_receipt.message_id%TYPE,
                            L_oms_cust_ord_no oms_cust_ord_head.oms_cust_ord_no%TYPE) IS
      SELECT ord.item_id,
             orr.dc_dest_id,
             SUM(ord.unit_qty) unit_qty,
             orr.po_nbr,
             ocfd.fulfill_order_no,
             ocfd.line_no,
             ocfd.fulfill_req_qty ,
             ocfd.fulfill_conf_qty ,
             ocfd.fulfill_deliver_qty ,
             ocfd.fulfill_cancel_qty,
             orr.from_loc,
             'RE' event_id,
             'RECEIVING' event_comments
        FROM oms_co_fulfill_detail ocfd,
             oms_receipt orr,
             oms_receipt_dtl ord,
             oms_cust_ord_head ocoh
       WHERE ocfd.oms_cust_ord_no = L_oms_cust_ord_no
         AND ocfd.oms_cust_ord_no = ocoh.oms_cust_ord_no
         AND ocoh.cust_order_no = orr.cust_order_nbr
         AND ocfd.item = ord.item_id
         AND orr.message_id = L_message_id
         AND orr.fulfill_order_nbr = ocfd.fulfill_order_no
         AND orr.po_nbr = ord.po_nbr
         AND orr.message_id = ord.message_id
      GROUP BY ord.item_id,
               orr.dc_dest_id,
               orr.po_nbr,
               ocfd.fulfill_order_no,
               ocfd.line_no,
               ocfd.fulfill_req_qty ,
               ocfd.fulfill_conf_qty ,
               ocfd.fulfill_deliver_qty ,
               ocfd.fulfill_cancel_qty,
               orr.from_loc,
               'RE',
               'RECEIVING'
       ORDER BY ord.item_id;


   CURSOR C_SPARE_PART_DETAIL (L_message_id oms_receipt.message_id%TYPE) IS
      SELECT ord.item_id,
             ord.unit_qty,
             orr.dc_dest_id,
             orr.from_loc,
             orr.po_nbr
        FROM oms_receipt orr,
             oms_receipt_dtl ord
       WHERE orr.message_id = L_message_id
         AND orr.message_id = ord.message_id
         AND orr.po_nbr = ord.po_nbr
         AND ord.unit_qty > 0;
         
   CURSOR C_CUST_ORDER_NO (L_message_id oms_receipt.message_id%TYPE) IS
      SELECT distinct orr.cust_order_nbr
        FROM oms_receipt orr
       WHERE orr.message_id = L_message_id
         AND orr.cust_order_nbr IS NOT NULL;
		 
   CURSOR C_SPARE_PART_QTY_DTL(L_po_nbr         oms_spare_part_fulfill.tran_id%type,
                               L_srv_req_id     oms_spare_part_fulfill.oms_service_req_seq_id%type,
                               L_src_loc        oms_spare_part_fulfill.source_location%type,
                               L_item           oms_spare_part_fulfill.item_id%type) IS
      SELECT NVL(quantity_tsf_reserved,0),
             NVL(quantity_received,0),
             NVL(quantity_cancelled,0)
        FROM oms_spare_part_fulfill ospf,
             oms_spare_part_header osph  
       WHERE ospf.tran_id = L_po_nbr
         AND ospf.oms_service_req_seq_id = osph.oms_service_req_seq_id
         AND ospf.oms_service_req_seq_id = L_srv_req_id
         AND ospf.source_location = L_src_loc
         AND ospf.item_id = L_item;

	CURSOR C_GETAPPLICATION_CNT (V_cust_order_no  oms_cust_ord_head.cust_order_no%TYPE) IS	
		
		SELECT COUNT(APPLICATION_ID)
			FROM OMS_CUST_ORD_HEAD
			WHERE CUST_ORDER_NO=V_cust_order_no
			AND STATUS='S'
			AND ORD_PAYMENT_STATUS='S'
			AND APPLICATION_ID<>'ORPOS';
BEGIN

--INSERT INTO CUST_ORDER_JOURNEY (CUST_ORDER_NO, EVENT, CREATE_TIMESTAMP)
--SELECT NULL, 'INSIDE THE PACKAGE BODY', sysdate from dual;

--commit;
 
   O_status_code := API_CODES.SUCCESS;
   O_rib_otbdesc_rec := NULL;

   -- Perform common api initialization tasks
   IF API_LIBRARY.INIT(O_error_message) = FALSE THEN
      raise PROGRAM_ERROR;
   END IF;

   -- Check message type
   IF I_message_type is NULL THEN
      O_error_message := SQL_LIB.CREATE_MSG('RMSSUB_INV_MSG_TYPE', NVL(I_message_type, 'NULL'));
      RAISE PROGRAM_ERROR;
   END IF;

   IF (lower(I_message_type) not in (RECEIPT_ADD, RECEIPT_ORDADD)) THEN
     -- O_error_message := SQL_LIB.CREATE_MSG('RMSSUB_INV_MSG_TYPE', NVL(I_message_type, 'NULL'));
      RAISE INV_MESSAGE_TYPE;
   END IF;

   L_rib_receiptdesc_rec := treat (I_message as "RIB_ReceiptDesc_REC");

   IF L_rib_receiptdesc_rec is NULL or L_rib_receiptdesc_rec.receipt_tbl is NULL or L_rib_receiptdesc_rec.receipt_tbl.COUNT <= 0 THEN
      O_error_message := SQL_LIB.CREATE_MSG('RMSSUB_INV_MESSAGE', NULL, NULL, NULL);
      RAISE PROGRAM_ERROR;
   END IF;
   --When SIM publishes ReceiptDesc during receiving process, it will publish with message type ?ReceiptCre?
   --if the receipt is not related to a customer order and message type ?ReceiptOrdCre? if it is associated
   --to a customer order. All message will continue to flow to RMS. Messages of type ?ReceiptOrdCre? will
   --also flow to OMS. RMS will treat 'ReceiptCre' and 'ReceiptOrdCre' exactly the same.

      IF (LOWER(I_message_type) = RECEIPT_ORDADD) THEN
         L_process_ind :='Y';
      END IF;
      
      
      IF (LOWER(I_message_type) = RECEIPT_ADD) THEN
         L_doc_type := L_rib_receiptdesc_rec.receipt_tbl(1).document_type;
         L_doc_no   := L_rib_receiptdesc_rec.receipt_tbl(1).po_nbr;
         L_rib_receipt_rec := L_rib_receiptdesc_rec.receipt_tbl(1);
      
         IF (L_doc_type = 'T') THEN
            OPEN C_SPARE_PART;
            FETCH C_SPARE_PART into L_found,
                                    L_oms_service_req_id;
      
            IF C_SPARE_PART%NOTFOUND THEN
               L_process_ind :='N';
            ELSE
               L_process_ind :='Y';
            END IF;
            CLOSE C_SPARE_PART;
         END IF;
      END IF;
      -- Checking the L_process_ind , if the indicator is 'Y' then we can
      -- Process the insertion and then we can insert the records in the
      -- OMS receipt table
      
      IF (L_process_ind = 'Y') THEN
         SELECT oms_receipt_msg_seq.nextval
           INTO L_message_id
           FROM DUAL;
      
         INSERT INTO OMS_RECEIPT_DESC(MESSAGE_ID      ,
                                      SCHEDULE_NBR    ,
                                      APPT_NBR        ,
                                      CREATE_TIMESTAMP
                                     )
                               VALUES(L_message_id,
                                      L_rib_receiptdesc_rec.schedule_nbr,
                                      L_rib_receiptdesc_rec.appt_nbr,
                                      SYSDATE
                                     );
      
         FOR I IN L_rib_receiptdesc_rec.Receipt_TBL.FIRST .. L_rib_receiptdesc_rec.Receipt_TBL.LAST
         LOOP
            L_rib_receipt_rec := L_rib_receiptdesc_rec.Receipt_TBL(I);
            INSERT INTO OMS_RECEIPT(MESSAGE_ID,
                                    DC_DEST_ID,
                                    PO_NBR           ,
                                    CUST_ORDER_NBR   ,
                                    FULFILL_ORDER_NBR,
                                    DOCUMENT_TYPE    ,
                                    REF_DOC_NO       ,
                                    ASN_NBR          ,
                                    RECEIPT_TYPE     ,
                                    FROM_LOC         ,
                                    FROM_LOC_TYPE    ,
                                    CREATE_TIMESTAMP
                                   )
                             VALUES(L_message_id,
                                    L_rib_receipt_rec.dc_dest_id,
                                    L_rib_receipt_rec.po_nbr,
                                    L_rib_receipt_rec.cust_order_nbr,
                                    L_rib_receipt_rec.fulfill_order_nbr,
                                    L_rib_receipt_rec.document_type,
                                    L_rib_receipt_rec.ref_doc_no,
                                    L_rib_receipt_rec.asn_nbr,
                                    L_rib_receipt_rec.receipt_type,
                                    L_rib_receipt_rec.from_loc,
                                    L_rib_receipt_rec.from_loc_type,
                                    SYSDATE
                                   );
         
         
--                    INSERT INTO CUST_ORDER_JOURNEY (CUST_ORDER_NO, EVENT, CREATE_TIMESTAMP)
--                    SELECT L_rib_receipt_rec.cust_order_nbr, 'VALUES INSERTED TO OMS_RECEIPT TABLE', sysdate from dual;
--                    commit;

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 1, L_rib_receipt_rec.cust_order_nbr, null, 'OMSSUB_RECEIVING', 'VALUES INSERTED TO OMS_RECEIPT TABLE', sysdate
                        FROM DUAL;

            FOR L IN L_rib_receipt_rec.ReceiptDtl_TBL.FIRST .. L_rib_receipt_rec.ReceiptDtl_TBL.LAST
            LOOP
               L_rib_receiptdtl_rec := L_rib_receipt_rec.ReceiptDtl_TBL(L);
         
               INSERT INTO OMS_RECEIPT_DTL(MESSAGE_ID,
                                           PO_NBR,
                                           ITEM_ID,
                                           UNIT_QTY,
                                           RECEIPT_XACTN_TYPE,
                                           RECEIPT_DATE,
                                           RECEIPT_NBR,
                                           DEST_ID,
                                           CONTAINER_ID,
                                           DISTRO_NBR,
                                           DISTRO_DOC_TYPE,
                                           TO_DISPOSITION,
                                           FROM_DISPOSITION,
                                           TO_WIP,
                                           FROM_WIP,
                                           TO_TROUBLE,
                                           FROM_TROUBLE,
                                           USER_ID,
                                           DUMMY_CARTON_IND,
                                           TAMPERED_CARTON_IND,
                                           UNIT_COST,
                                           SHIPPED_QTY,
                                           WEIGHT,
                                           WEIGHT_UOM,
                                           GROSS_COST,
                                           CREATE_TIMESTAMP
                                          )
                                    VALUES(L_message_id,
                                           L_rib_receipt_rec.po_nbr,
                                           L_rib_receiptdtl_rec.item_id,
                                           L_rib_receiptdtl_rec.unit_qty,
                                           L_rib_receiptdtl_rec.receipt_xactn_type,
                                           L_rib_receiptdtl_rec.receipt_date,
                                           L_rib_receiptdtl_rec.receipt_nbr,
                                           L_rib_receiptdtl_rec.dest_id,
                                           L_rib_receiptdtl_rec.container_id,
                                           L_rib_receiptdtl_rec.distro_nbr,
                                           L_rib_receiptdtl_rec.distro_doc_type,
                                           L_rib_receiptdtl_rec.to_disposition,
                                           L_rib_receiptdtl_rec.from_disposition,
                                           L_rib_receiptdtl_rec.to_wip,
                                           L_rib_receiptdtl_rec.from_wip,
                                           L_rib_receiptdtl_rec.to_trouble,
                                           L_rib_receiptdtl_rec.from_trouble,
                                           L_rib_receiptdtl_rec.user_id,
                                           L_rib_receiptdtl_rec.dummy_carton_ind,
                                           L_rib_receiptdtl_rec.tampered_carton_ind,
                                           L_rib_receiptdtl_rec.unit_cost,
                                           L_rib_receiptdtl_rec.shipped_qty,
                                           L_rib_receiptdtl_rec.weight,
                                           L_rib_receiptdtl_rec.weight_uom,
                                           L_rib_receiptdtl_rec.gross_cost,
                                           SYSDATE
                                          );
            END LOOP;
         
            IF L_rib_receipt_rec.ReceiptCartonDtl_TBL.EXISTS(1) THEN
         
         
               IF (L_rib_receipt_rec.ReceiptCartonDtl_TBL.COUNT >0) THEN
         
                  FOR M IN L_rib_receipt_rec.ReceiptCartonDtl_TBL.FIRST .. L_rib_receipt_rec.ReceiptCartonDtl_TBL.LAST
                  LOOP
                     L_rib_receiptcartondtl_rec := L_rib_receipt_rec.ReceiptCartonDtl_TBL(M);
         
                     INSERT INTO OMS_RECEIPT_CARTONDTL(MESSAGE_ID,
                                                       PO_NBR,
                                                       CARTON_STATUS_IND,
                                                       CONTAINER_ID,
                                                       DEST_ID,
                                                       RECEIPT_XACTN_TYPE,
                                                       RECEIPT_DATE,
                                                       RECEIPT_NBR,
                                                       USER_ID,
                                                       TO_DISPOSITION,
                                                       WEIGHT,
                                                       WEIGHT_UOM,
                                                       CREATE_TIMESTAMP
                                                      )
                                                VALUES(L_message_id,
                                                       L_rib_receipt_rec.po_nbr,
                                                       L_rib_receiptcartondtl_rec.carton_status_ind,
                                                       L_rib_receiptcartondtl_rec.container_id,
                                                       L_rib_receiptcartondtl_rec.dest_id,
                                                       L_rib_receiptcartondtl_rec.receipt_xactn_type,
                                                       L_rib_receiptcartondtl_rec.receipt_date,
                                                       L_rib_receiptdtl_rec.receipt_nbr,
                                                       L_rib_receiptcartondtl_rec.user_id,
                                                       L_rib_receiptcartondtl_rec.to_disposition,
                                                       L_rib_receiptcartondtl_rec.weight,
                                                       L_rib_receiptcartondtl_rec.weight_uom,
                                                       SYSDATE
                                                      );
         
                  END LOOP;
               END IF;
            END IF;
         
         END LOOP;
         IF L_rib_receiptdesc_rec.ReceiptOverage_TBL.EXISTS(1) THEN
            IF (L_rib_receiptdesc_rec.ReceiptOverage_TBL.COUNT >0) THEN
               FOR J IN L_rib_receiptdesc_rec.ReceiptOverage_TBL.FIRST .. L_rib_receiptdesc_rec.ReceiptOverage_TBL.LAST
               LOOP
                  L_rib_receiptoverage_rec := L_rib_receiptdesc_rec.ReceiptOverage_TBL(J);
                  INSERT INTO OMS_RECEIPT_OVERAGE(MESSAGE_ID,
                                                  PO_NBR,
                                                  DOCUMENT_TYPE,
                                                  ASN_NBR,
                                                  CREATE_TIMESTAMP
                                                 )
                                          VALUES(L_message_id,
                                                 L_rib_receiptoverage_rec.po_nbr,
                                                 L_rib_receiptoverage_rec.document_type,
                                                 L_rib_receiptoverage_rec.asn_nbr,
                                                 SYSDATE
                                                );
         
                  FOR K IN L_rib_receiptoverage_rec.ReceiptOverageDtl_TBL.FIRST .. L_rib_receiptoverage_rec.ReceiptOverageDtl_TBL.LAST
                  LOOP
                     L_rib_receiptoveragedtl_rec := L_rib_receiptoverage_rec.ReceiptOverageDtl_TBL(K);
                     INSERT INTO OMS_RECEIPT_OVERAGEDTL(MESSAGE_ID,
                                                        PO_NBR,
                                                        ITEM_ID,
                                                        QTY_RECEIVED,
                                                        REASON_CODE,
                                                        CREATE_TIMESTAMP
                                                       )
                                                 VALUES(L_message_id,
                                                        L_rib_receiptoverage_rec.po_nbr,
                                                        L_rib_receiptoveragedtl_rec.item_id,
                                                        L_rib_receiptoveragedtl_rec.qty_received,
                                                        L_rib_receiptoveragedtl_rec.reason_code ,
                                                        SYSDATE
                                                       );
         
                  END LOOP;
               END LOOP;
            END IF;
         END IF;
         
         IF (lower(I_message_type) = RECEIPT_ORDADD) THEN
         -- This is a CO related receipt messsage , the item_id
         -- has been received in fulfill location .
         -- Need to reserve the item by callign the SIM and RMS web service
         -- update and insert oms tables line oms_co_fulfill_Detail,
         -- oms_cust_ord_log , oms_cust_ord_log_item tables
         -- then call the seibel WS to update the status of the receiving
         -- Calling SEIBEL web service to update the status of the receving
         
         OPEN C_CUST_ORDER_NO(L_message_id);    
         LOOP     
                       
            FETCH C_CUST_ORDER_NO INTO L_cust_ord_no;
            EXIT WHEN C_CUST_ORDER_NO%NOTFOUND;

--                    INSERT INTO CUST_ORDER_JOURNEY (CUST_ORDER_NO, EVENT, CREATE_TIMESTAMP)
--                    SELECT L_cust_ord_no, 'CALLING SIM WEBSERVICE', sysdate from dual;
--                    commit;

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 2, L_cust_ord_no, null, 'OMSSUB_RECEIVING', 'CALLING SIM WEBSERVICE', sysdate
                        FROM DUAL;
                    
         -- CAll the SIM WS
            L_sim_ws_co :=OMS_RECEIPT_CO_SIM_WS_INVOKER.f_REC_SIM_CO_RESERVE(L_message_id,L_cust_ord_no);

--                    INSERT INTO CUST_ORDER_JOURNEY (CUST_ORDER_NO, EVENT, CREATE_TIMESTAMP)
--                    SELECT L_cust_ord_no, 'SIM WEBSERVICE SUCESSFULL', sysdate from dual;
--                    commit;

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 3, L_cust_ord_no, null, 'OMSSUB_RECEIVING', 'SIM WEBSERVICE SUCESSFULL', sysdate
                        FROM DUAL;

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 4, L_cust_ord_no, null, 'OMSSUB_RECEIVING', 'CALLING RMS WEB SERVICE', sysdate
                        FROM DUAL;

         -- CALL the RMS WS
            L_rms_ws_co :=OMS_RECEIPT_CO_WS_INVOKER.f_REC_RMS_CO_RESERVE(L_message_id,L_cust_ord_no);
			
--                    INSERT INTO CUST_ORDER_JOURNEY (CUST_ORDER_NO, EVENT, CREATE_TIMESTAMP)
--                    SELECT L_cust_ord_no, 'RMS WEBSERVICE SUCESSFULL', sysdate from dual;
--                    commit;

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 5, L_cust_ord_no, null, 'OMSSUB_RECEIVING', 'RMS WEB SERVICE SUCESSFUL', sysdate
                        FROM DUAL;

		 -- Call the siebel webservices  
		 -- Sibel or order status web services is called when the order is E-commerce order . 
		 -- Other wise it will not call.
		 
			 L_application_cnt:=0;
			 open  C_GETAPPLICATION_CNT(L_cust_ord_no);
			 fetch C_GETAPPLICATION_CNT into L_application_cnt; 
			 close C_GETAPPLICATION_CNT;
			
			if(L_application_cnt>0) then 
                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 6, L_cust_ord_no, null, 'OMSSUB_RECEIVING', 'CALLING SIEBEL WEB SERVICE', sysdate
                        FROM DUAL;
                        
--                 L_seibel_out :=OMS_RECEIPT_WS_INVOKER.f_REC_Siebel_COStatusUpdate(L_message_id,L_cust_ord_no);	
                        
                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 7, L_cust_ord_no, null, 'OMSSUB_RECEIVING', 'SIEBEL WEB SERVICE SUCESSFUL', sysdate
                        FROM DUAL;
			end if;
			
         END LOOP;
         CLOSE C_CUST_ORDER_NO;
         
         -- always the DEstination id will be the source loc id,
         -- since its is reservation , and also the location type will be 'ST'
         -- because the pickup will be done always only in store not from WH TO WH
         -- Checking the corresponding OMS_CUST_ORD_NO for teh message id processing
         -- from teh receipt table
         
         OPEN C_OMS_CUS_ORD_NO(L_message_id);
         LOOP
            L_counter := 0;
            FETCH C_OMS_CUS_ORD_NO into L_oms_cust_ord_no;
            EXIT WHEN C_OMS_CUS_ORD_NO %NOTFOUND;
            SELECT MAX(FULFILL_ORDER_NO)
              INTO L_fulfill_seq
              FROM oms_co_fulfill_detail
             WHERE oms_cust_ord_no = L_oms_cust_ord_no;
         
             
         -- Fecthing the fulfilment details for the specif oms_cust_ord_no
               
               L_fulfill_seq := L_fulfill_seq + 1;

               FOR c2 IN C_FULFILL_DETAIL(L_message_id,L_oms_cust_ord_no)
               LOOP
                  if c2.item_id = l_item_check then
                     tbl_item_rec(L_cnt).T_item        := c2.item_id;
                     tbl_item_rec(L_cnt).T_dc_dest_id  := c2.dc_dest_id;
                     tbl_item_rec(L_cnt).T_quantity    := c2.unit_qty;
                     tbl_item_rec(L_cnt).T_po_nbr      := c2.po_nbr;
                     tbl_item_rec(L_cnt).T_fulfil_no   := c2.fulfill_order_no;
                     tbl_item_rec(L_cnt).T_line_no     := c2.line_no;
                     tbl_item_rec(L_cnt).T_req_qty     := c2.fulfill_req_qty;
                     tbl_item_rec(L_cnt).T_conf_qty    := c2.fulfill_conf_qty;
                     tbl_item_rec(L_cnt).T_deliver_qty := c2.fulfill_deliver_qty;
                     tbl_item_rec(L_cnt).T_cancel_qty  := c2.fulfill_cancel_qty;
                     tbl_item_rec(L_cnt).T_from_loc    := c2.from_loc;
                     tbl_item_rec(L_cnt).T_event_id    := c2.event_id;
                     tbl_item_rec(L_cnt).T_event_comm  := c2.event_comments; 
                     L_cnt := L_cnt+1;
                  else
                     L_item        := c2.item_id;
                     L_dc_dest_id  := c2.dc_dest_id;
                     L_quantity    := c2.unit_qty;
                     L_po_nbr      := c2.po_nbr;
                     L_fulfil_no   := c2.fulfill_order_no;
                     L_line_no     := c2.line_no;
                     L_req_qty     := c2.fulfill_req_qty;
                     L_conf_qty    := c2.fulfill_conf_qty;
                     L_deliver_qty := c2.fulfill_deliver_qty;
                     L_cancel_qty  := c2.fulfill_cancel_qty;
                     L_from_loc    := c2.from_loc;
                     L_event_id    := c2.event_id;
                     L_event_comm  := c2.event_comments;

                     L_dlv_qty := L_conf_qty - L_cancel_qty - L_deliver_qty;
                     IF L_quantity > L_dlv_qty THEN
                        L_conf_qty := L_dlv_qty;
                     ELSE
                        L_conf_qty := L_quantity;
                     END IF;
                  
                     SELECT line_no,
                            combination_id
                       INTO L_line_no_new,
                            L_combination_id
                       FROM oms_co_fulfill_Detail ocfd
                      WHERE oms_cust_ord_no = L_oms_cust_ord_no
                        AND fulfill_order_no = L_fulfil_no
                        AND item             = L_item;
                  
                     IF L_conf_qty > 0 THEN
                        INSERT INTO OMS_CO_FULFILL_DETAIL(OMS_CUST_ORD_NO,
                                                          FULFILL_ORDER_NO,
                                                          ITEM,
                                                          LINE_NO,
                                                          ORIG_ITEM,
                                                          SOURCE_LOC_TYPE,
                                                          SOURCE_LOC,
                                                          FULFILL_LOC_TYPE,
                                                          FULFILL_LOC,
                                                          FULFILL_REQ_QTY,
                                                          FULFILL_CONF_QTY,
                                                          FULFILL_DELIVER_QTY,
                                                          FULFILL_CANCEL_QTY,
                                                          FULFILL_STATUS,
                                                          CREATE_DATETIME,
                                                          LAST_UPDATE_DATETIME,
                                                          COMBINATION_ID)
                                                   VALUES(L_oms_cust_ord_no,
                                                          L_fulfill_seq,
                                                          L_item,
                                                          L_line_no_new,
                                                          NULL,
                                                          'ST',
                                                          L_dc_dest_id,
                                                          'S',
                                                          L_dc_dest_id,
                                                          L_conf_qty,
                                                          L_conf_qty,
                                                          0,
                                                          0,
                                                          'C',
                                                          SYSDATE,
                                                          null,
                                                          L_combination_id);
                        
--                    INSERT INTO CUST_ORDER_JOURNEY (CUST_ORDER_NO, EVENT, CREATE_TIMESTAMP)
--                    SELECT L_oms_cust_ord_no, 'FULFILLMENT CREATED IN OMS_CO_FULFILL_DETAIL', sysdate from dual;
--                    commit;                        

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 8, null, L_oms_cust_ord_no, 'OMSSUB_RECEIVING', 'FULFILLMENT CREATED IN OMS_CO_FULFILL_DETAIL', sysdate
                        FROM DUAL;

                           -- Checking the counter should be '1' since the oms_cust_ord_log should insert once for a oms_cust_ord_no
                           -- where the multiple item will come for the same order not
                        
                           L_Counter := L_Counter + 1;
                           IF (L_Counter = 1) THEN
                        
                           -- Sequence to insert into log table
                              SELECT OMS_LOG_SEQ_NO_SEQ.nextval
                                INTO L_seq_no 
                                FROM dual;
                        
                           -- Insert into ord_log table
                        
                              INSERT INTO OMS_CUST_ORD_LOG(OMS_CUST_ORD_NO,
                                                           LOG_SEQ_NO,
                                                           OMS_DLV_CONF_ID,
                                                           EVENT_ID,
                                                           EVENT_COMMENTS,
                                                           CREATE_DATETIME,
                                                           OMS_CANCEL_ID)
                                                    VALUES(L_oms_cust_ord_no,
                                                           L_seq_no,
                                                           L_del_conf_id,
                                                           L_Event_id,
                                                           L_Event_comm,
                                                           SYSDATE,
                                                           null);
                           END IF;
                        
--                    INSERT INTO CUST_ORDER_JOURNEY (CUST_ORDER_NO, EVENT, CREATE_TIMESTAMP)
--                    SELECT L_oms_cust_ord_no, 'CUST ORD LOG ENTRY INSERTED', sysdate from dual;
                    
                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 9, null, L_oms_cust_ord_no, 'OMSSUB_RECEIVING', 'CUST ORD LOG ENTRY INSERTED', sysdate
                        FROM DUAL;                    
                    
--                    commit;                        
                           -- Insert into log item table
                        
                           INSERT INTO OMS_CUST_ORD_LOG_ITEM(ITEM,
                                                             LINE_NO,
                                                             LOG_SEQ_NO,
                                                             QTY,
                                                             FULFILL_ORDER_NO,
                                                             CREATE_DATETIME
                                                            )
                                                      VALUES(L_item,
                                                             L_line_no,
                                                             L_seq_no,
                                                             L_conf_qty,
                                                             L_fulfil_no,
                                                             SYSDATE);
                           
                           UPDATE OMS_CO_FULFILL_DETAIL
                              SET fulfill_deliver_qty = NVL(fulfill_deliver_qty,0) + L_conf_qty
                            WHERE oms_cust_ord_no = L_oms_cust_ord_no
                              AND fulfill_order_no = L_fulfil_no
                              AND line_no = L_line_no
                              AND item = L_item;

                        --Update the last_update_datetime in head table
                           UPDATE OMS_CUST_ORD_HEAD
                              SET last_update_datetime = sysdate
                            WHERE oms_cust_ord_no = L_oms_cust_ord_no
                              AND status = 'S';
                     END IF;
                  end if;
                  l_item_check := L_item;
               END LOOP;

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 10, null, L_oms_cust_ord_no, 'OMSSUB_RECEIVING', 'OMS CUST ORD HEAD TABLE UPDATED', sysdate
                        FROM DUAL;                    

--                    INSERT INTO CUST_ORDER_JOURNEY (CUST_ORDER_NO, EVENT, CREATE_TIMESTAMP)
--                    SELECT L_oms_cust_ord_no, 'OMS CUST ORD HEAD TABLE UPDATED', sysdate from dual;
--                    commit;                        
               
               l_item_check := '0';
               --Looping through the array tbl_item_rec
               LOOP
                  EXIT WHEN tbl_item_rec.COUNT = 0;
                  l_item_check_array := '0';

                  if tbl_item_rec is not NULL and tbl_item_rec.COUNT > 0 then
                     l_fulfill_seq := l_fulfill_seq + 1;
                     index_value := tbl_item_rec.FIRST;
                  --Looping through the array tbl_item_rec
                  LOOP
                     EXIT WHEN index_value IS NULL;
                     if tbl_item_rec(index_value).T_item <> l_item_check_array then
                        L_item       := tbl_item_rec(index_value).T_item;
                        L_dc_dest_id := tbl_item_rec(index_value).T_dc_dest_id;
                        L_quantity   := tbl_item_rec(index_value).T_quantity;
                        L_po_nbr     := tbl_item_rec(index_value).T_po_nbr;
                        L_fulfil_no  := tbl_item_rec(index_value).T_fulfil_no;
                        L_line_no    := tbl_item_rec(index_value).T_line_no;
                        L_req_qty    := tbl_item_rec(index_value).T_req_qty;
                        L_conf_qty   := tbl_item_rec(index_value).T_conf_qty;
                        L_deliver_qty:= tbl_item_rec(index_value).T_deliver_qty;
                        L_cancel_qty := tbl_item_rec(index_value).T_cancel_qty;
                        L_from_loc   := tbl_item_rec(index_value).T_from_loc;
                        L_event_id   := tbl_item_rec(index_value).T_event_id;
                        L_event_comm := tbl_item_rec(index_value).T_event_comm;
                        L_dlv_qty := L_conf_qty - L_cancel_qty - L_deliver_qty;

                        IF L_quantity > L_dlv_qty THEN
                           L_conf_qty := L_dlv_qty;
                        ELSE
                           L_conf_qty := L_quantity;
                        END IF;
                        
                        SELECT line_no,
                               combination_id
                          INTO L_line_no_new,
                               L_combination_id
                          FROM oms_co_fulfill_Detail ocfd
                         WHERE oms_cust_ord_no = L_oms_cust_ord_no
                           AND fulfill_order_no = L_fulfil_no
                           AND item             = L_item;

                        IF L_conf_qty > 0 THEN
                           INSERT INTO OMS_CO_FULFILL_DETAIL(OMS_CUST_ORD_NO,
                                                             FULFILL_ORDER_NO,
                                                             ITEM,
                                                             LINE_NO,
                                                             ORIG_ITEM,
                                                             SOURCE_LOC_TYPE,
                                                             SOURCE_LOC,
                                                             FULFILL_LOC_TYPE,
                                                             FULFILL_LOC,
                                                             FULFILL_REQ_QTY,
                                                             FULFILL_CONF_QTY,
                                                             FULFILL_DELIVER_QTY,
                                                             FULFILL_CANCEL_QTY,
                                                             FULFILL_STATUS,
                                                             CREATE_DATETIME,
                                                             LAST_UPDATE_DATETIME,
                                                             COMBINATION_ID)
                                                      VALUES(L_oms_cust_ord_no,
                                                             L_fulfill_seq,
                                                             L_item,
                                                             L_line_no_new,
                                                             NULL,
                                                             'ST',
                                                             L_dc_dest_id,
                                                             'S',
                                                             L_dc_dest_id,
                                                             L_conf_qty,
                                                             L_conf_qty,
                                                             0,
                                                             0,
                                                             'C',
                                                             SYSDATE,
                                                             null,
                                                             L_combination_id);
                           
                              -- Checking the counter should be '1' since the oms_cust_ord_log should insert once for a oms_cust_ord_no
                              -- where the multiple item will come for the same order not
                           
                              L_Counter := L_Counter + 1;
                              IF (L_Counter = 1) THEN
                           
                              -- Sequence to insert into log table
                                 SELECT OMS_LOG_SEQ_NO_SEQ.nextval
                                   INTO L_seq_no FROM dual;
                           
                              -- Insert into ord_log table
                                 INSERT INTO OMS_CUST_ORD_LOG(OMS_CUST_ORD_NO,
                                                              LOG_SEQ_NO,
                                                              OMS_DLV_CONF_ID,
                                                              EVENT_ID,
                                                              EVENT_COMMENTS,
                                                              CREATE_DATETIME,
                                                              OMS_CANCEL_ID)
                                                       VALUES(L_oms_cust_ord_no,
                                                              L_seq_no,
                                                              L_del_conf_id,
                                                              L_Event_id,
                                                              L_Event_comm,
                                                              SYSDATE,
                                                              null);
                              END IF;
                           
                              -- Insert into log item table
                              INSERT INTO OMS_CUST_ORD_LOG_ITEM(ITEM,
                                                                LINE_NO,
                                                                LOG_SEQ_NO,
                                                                QTY,
                                                                FULFILL_ORDER_NO,
                                                                CREATE_DATETIME
                                                               )
                                                         VALUES(L_item,
                                                                L_line_no,
                                                                L_seq_no,
                                                                L_conf_qty,
                                                                L_fulfil_no,
                                                                sysdate);

                              UPDATE OMS_CO_FULFILL_DETAIL
                                 SET fulfill_deliver_qty = NVL(fulfill_deliver_qty,0) + L_conf_qty
                               WHERE oms_cust_ord_no = L_oms_cust_ord_no
                                 AND fulfill_order_no = L_fulfil_no
                                 AND line_no = L_line_no
                                 AND item = L_item;
                              
                              UPDATE OMS_CUST_ORD_HEAD
                                 SET last_update_datetime = sysdate
                               WHERE oms_cust_ord_no = L_oms_cust_ord_no
                                 AND status = 'S';
                        END IF; 
                        tbl_item_rec.delete(index_value);
                     END IF;
                     l_item_check_array := L_item;
                     index_value := tbl_item_rec.next(index_value);
                  END LOOP;
               END if;
               END LOOP;      
            END LOOP;
            CLOSE C_OMS_CUS_ORD_NO;
         ELSE
         
         --This is related to the spare parts and the item
         -- has to be received in service centre.
         
         -- Need to reserve teh item at service centre by calling the
         -- inventory adjustment WS with the reason code specified in the
         -- oms_system_parameters tables.
         
         -- Also insert / Update the spare parts table for specific item and
         -- and the quantity present in the receipt table
         
            OPEN C_SPARE_PART_DETAIL(L_message_id);
            LOOP
               FETCH C_SPARE_PART_DETAIL INTO L_spare_item,
                                              L_spare_qty,
                                              L_spare_loc,
                                              L_src_loc,
                                              L_spare_po;
               EXIT WHEN C_SPARE_PART_DETAIL%NOTFOUND;
               OPEN C_SPARE_PART_QTY_DTL(L_rib_receipt_rec.po_nbr,
			                             L_oms_service_req_id,
										 L_src_loc,
										 L_spare_item);
               FETCH C_SPARE_PART_QTY_DTL INTO L_qty_tsf_rsv,
                                               L_recv_qty,
                                               L_qty_can;
               CLOSE C_SPARE_PART_QTY_DTL;
               
               L_delv_qty := L_qty_tsf_rsv - L_recv_qty - L_qty_can;

               IF L_spare_qty > L_delv_qty THEN
                  L_receive_qty := L_delv_qty;
               ELSE
                  L_receive_qty := L_spare_qty;
               END IF;
               

               IF L_delv_qty > 0 THEN
               --Call inventory Adjustment package
               -- Callign inventory Ajustment package which makes adjusment in SIM for teh spare parts
               -- Request
               
                  L_sim_invadj_ws := OMS_RECEIPT_SIM_INVADJ_INVOKER.f_REC_SIM_INVADJ(L_message_id,L_response);
               
                  IF (NVL(L_sim_invadj_ws,'SUCCESS') <> 'ERROR') THEN
            
                     -- Update the CUM_QUANTITY_RECEIVED from oms_spare_part_header
                     -- based on the quantity from the RECEIPT message
                     
                     UPDATE oms_spare_part_header
                        SET cum_quantity_received = nvl(cum_quantity_received,0) + L_spare_qty,
                            cum_quantity_reserved = NVL(cum_quantity_reserved,0) + L_spare_qty,
                            last_update_datetime =SYSDATE
                      WHERE oms_service_req_seq_id IN (SELECT DISTINCT oms_service_req_seq_id
                                                         FROM oms_spare_part_fulfill
                                                        WHERE item_id = L_spare_item
                                                          AND tran_id = L_spare_po);
                     
                     UPDATE oms_spare_part_fulfill
                        SET quantity_received = nvl(quantity_received,0) + L_spare_qty,
                            last_updated_datetime =SYSDATE
                      WHERE item_id = L_spare_item
                        AND tran_id = L_spare_po;
                     
                     -- insert a records with the destination location
                     INSERT INTO OMS_SPARE_PART_FULFILL(OMS_SERVICE_REQ_SEQ_ID,
                                                        SOURCE_LOCATION,
                                                        ITEM_ID,
                                                        QUANTITY_RESERVED,
                                                        QUANTITY_TSF_RESERVED,
                                                        QUANTITY_UNRESERVED,
                                                        QUANTITY_DEDUCTED,
                                                        QUANTITY_SHIPPED,
                                                        QUANTITY_RECEIVED,
                                                        QUANTITY_CANCELLED,
                                                        TRAN_ID,
                                                        CREATED_BY,
                                                        CREATE_DATETIME,
                                                        UPDATED_BY,
                                                        LAST_UPDATED_DATETIME,
                                                        TRAN_TYPE
                                                       )
                                                 VALUES(L_oms_service_req_id,
                                                        L_spare_loc,
                                                        L_spare_item,
                                                        L_receive_qty,
                                                        0,
                                                        0,
                                                        0,
                                                        0,
                                                        0,
                                                        0,
                                                        L_sim_invadj_ws,
                                                        USER,
                                                        SYSDATE,
                                                        null,
                                                        null,
                                                        'RSV');

                     -- Insert into oms_spare_part_audit table
                     SELECT OMS_SPAREPART_AUDIT_SEQ.nextval
                       INTO L_audit_seq
                       FROM dual;
                     
                     INSERT INTO OMS_SPARE_PART_AUDIT(OMS_AUDIT_SEQ_ID,OMS_SERVICE_REQ_SEQ_ID,
                                                      SOURCE_LOCATION,
                                                      ITEM_ID,
                                                      EVENT_TYPE,
                                                      QUANTITY,
                                                      CREATED_BY,
                                                      CREATE_DATETIME)
                                               VALUES(L_audit_seq,
                                                      L_oms_service_req_id,
                                                      L_spare_loc,
                                                      L_spare_item,
                                                      'RSV',
                                                      L_receive_qty,
                                                      USER,
                                                      SYSDATE);
                     UPDATE oms_spare_part_header
                        SET ready_to_notify_flag = 'Y'
                      WHERE oms_service_req_seq_id = L_oms_service_req_id
                        AND (NVL(cum_quantity_reserved,0) - NVL(cum_quantity_unreserved,0))= pending_qty;
                  END IF;
               END IF;
            END LOOP;
            CLOSE C_SPARE_PART_DETAIL;
         END IF;
      END IF;      

IF O_error_message is not null then
   O_status_code := 'E';
   raise PROGRAM_ERROR;
END IF;

EXCEPTION
   WHEN INV_MESSAGE_TYPE THEN
      O_error_message := 'Invalid message type for OMS Receiving subscriber';
      O_status_code := 'E';
   WHEN PROGRAM_ERROR THEN
      API_LIBRARY.HANDLE_ERRORS(O_status_code,
                                O_error_message,
                                API_LIBRARY.FATAL_ERROR,
                                L_program);
   WHEN OTHERS THEN
      O_error_message := SQL_LIB.CREATE_MSG('PACKAGE_ERROR',
                                             SQLERRM,
                                             L_program,
                                             to_char(SQLCODE));
      DBMS_OUTPUT.PUT_LINE('O_error_message'||O_error_message);
END CONSUME;
------------------------------------------------------------------------------------------------------
END OMSSUB_RECEIVING;