create or replace PACKAGE BODY       OMSSUB_ASNOUT AS

   TYPE distro_record IS RECORD (distro_no              TSFHEAD.TSF_NO%TYPE,
                                 distro_type            VARCHAR2(2),
                                 franchise_ordret_ind   VARCHAR2(1),
                                 customer_order_no      ORDCUST.CUSTOMER_ORDER_NO%TYPE,
                                 fulfill_order_no       ORDCUST.FULFILL_ORDER_NO%TYPE,
                                 comments               TSFHEAD.COMMENT_DESC%TYPE);

   -- Define a record to store item records
   TYPE item_record IS RECORD (item                 ITEM_MASTER.ITEM%TYPE,
                               carton               SHIPSKU.CARTON%TYPE,
                               qty                  TSFDETAIL.TSF_QTY%TYPE,
                               weight               ITEM_LOC_SOH.AVERAGE_WEIGHT%TYPE,
                               weight_uom           UOM_CLASS.UOM%TYPE,
                               inv_status           INV_STATUS_CODES.INV_STATUS%TYPE,
                               extended_base_cost   ITEM_SUPP_COUNTRY_LOC.EXTENDED_BASE_COST%TYPE,
                               base_cost            ITEM_SUPP_COUNTRY_LOC.UNIT_COST%TYPE);

   -- Define a table based upon the item record defined above.
   TYPE item_table IS TABLE OF item_record INDEX BY BINARY_INTEGER;

   LP_inv_status       INV_STATUS_CODES.INV_STATUS%TYPE;

   LP_ils_item_tbl     ITEM_TABLE;

-- Define a table type to hold transfer number for repair context transfers


   TYPE tsf_record IS RECORD  (tsf_no             TSFHEAD.TSF_NO%TYPE,
                               from_loc_type      TSFHEAD.FROM_LOC_TYPE%TYPE);
   TYPE tsf_no_table IS TABLE OF tsf_record INDEX BY BINARY_INTEGER;
   LP_repair_tsf_no_tbl        tsf_no_table;
   LP_tbl_size                 BINARY_INTEGER := 0;
   L_seibel_out              VARCHAR2(1000);
-------------------------------------------------------------------------------------------------------
PROCEDURE HANDLE_ERRORS(O_status_code     IN OUT  VARCHAR2,
                        IO_error_message  IN OUT  VARCHAR2,
                        I_cause           IN      VARCHAR2,
                        I_program         IN      VARCHAR2);

-------------------------------------------------------------------------------------------------------


-------------------------------------------------------------------------------------------------------
-- Function Name: CONSUME_SHIPMENT_CORE
-- Purpose: This private function contains the core logic of asnout consume process.
------------------------------------------------------------------------------------
FUNCTION CONSUME_SHIPMENT_CORE (O_error_message     IN OUT  VARCHAR2,
                                I_message           IN      RIB_OBJECT,
                                I_message_type      IN      VARCHAR2)
return BOOLEAN;

------------------------------------------------------------------------------------
-- PUBLIC PROCEDURE --
-------------------------------------------------------------------------------------------------------

PROCEDURE CONSUME(O_status_code       IN OUT   VARCHAR2,
                  O_error_message     IN OUT   VARCHAR2,
                  I_message           IN       RIB_OBJECT,
                  I_message_type      IN       VARCHAR2)
IS

   L_program        VARCHAR2(255) := 'OMSSUB_ASNOUT.CONSUME';
   L_check_l10n_ind VARCHAR2(1) := 'Y';
   PROGRAM_ERROR  EXCEPTION;

BEGIN

   O_STATUS_CODE := API_CODES.SUCCESS;

   -- Perform common api initialization tasks
   if API_LIBRARY.INIT(O_error_message) = FALSE then

      raise PROGRAM_ERROR;


   end if;

   -- Check message type
   if I_message_type is NULL or LOWER(I_message_type) != LP_cre_type then
      O_error_message := SQL_LIB.CREATE_MSG('RMSSUB_INV_MSG_TYPE', NVL(I_message_type, 'NULL'));
      raise PROGRAM_ERROR;
   end if;


   CONSUME_SHIPMENT(O_status_code,
                    O_error_message,
                    I_message,
                    I_message_type,


                    L_check_l10n_ind);

   if O_error_message is not null then
      O_status_code := 'E';
      raise PROGRAM_ERROR;
   end if;

   return;

EXCEPTION

   when PROGRAM_ERROR then
      API_LIBRARY.HANDLE_ERRORS(O_status_code,
                    O_error_message,
                    API_LIBRARY.FATAL_ERROR,
                    L_program);

   when OTHERS then
      API_LIBRARY.HANDLE_ERRORS(O_status_code,
                    O_error_message,
                    API_LIBRARY.FATAL_ERROR,
                    L_program);

END CONSUME;

-------------------------------------------------------------------------------------------------------
-- PUBLIC PROCEDURE --



-------------------------------------------------------------------------------------------------------
PROCEDURE CONSUME_SHIPMENT(O_status_code       IN OUT  VARCHAR2,
                           O_error_message     IN OUT  VARCHAR2,
                           I_message           IN      RIB_OBJECT,
                           I_message_type      IN      VARCHAR2,
                           I_check_l10n_ind    IN      VARCHAR2)
IS


   L_program            VARCHAR2(255) := 'OMSSUB_ASNOUT.CONSUME_SHIPMENT';

   L_asnout_message    "RIB_ASNOutDesc_REC";
   L_distro_record     "RIB_ASNOutDistro_REC";


   L_l10n_rib_rec      "L10N_RIB_REC"          := L10N_RIB_REC();

   PROGRAM_ERROR       EXCEPTION;


BEGIN
   --

   L_l10n_rib_rec.rib_msg         :=  I_message;
   L_l10n_rib_rec.rib_msg_type    :=  I_message_type;
   L_l10n_rib_rec.procedure_key   := 'CONSUME_SHIPMENT';
   --

   if API_LIBRARY.INIT(O_error_message) = FALSE then
      raise PROGRAM_ERROR;
   end if;
   --

   if I_message_type is NULL or LOWER(I_message_type) != LP_cre_type then

      O_error_message := SQL_LIB.CREATE_MSG('RMSSUB_INV_MSG_TYPE', NVL(I_message_type, 'NULL'));
      raise PROGRAM_ERROR;
   end if;
   ---

         if OMSSUB_ASNOUT.CONSUME_SHIPMENT(O_error_message,
                                           L_l10n_rib_rec) = FALSE then
            raise PROGRAM_ERROR;
         end if;
         --

   -- If no error is raised, then the subscription has completed successfully.
   O_status_code := API_CODES.SUCCESS;

EXCEPTION
   when PROGRAM_ERROR then
      HANDLE_ERRORS(O_status_code,
                    O_error_message,
                    API_LIBRARY.FATAL_ERROR,
                    L_program);

   when OTHERS then
      HANDLE_ERRORS(O_status_code,

                    O_error_message,
                    API_LIBRARY.FATAL_ERROR,
                    L_program);
END CONSUME_SHIPMENT;
-------------------------------------------------------------------------------------------------------
-- PUBLIC FUNCTION --
-------------------------------------------------------------------------------------------------------
FUNCTION CONSUME_SHIPMENT (O_error_message     IN OUT  VARCHAR2,
                           IO_L10N_RIB_REC     IN OUT  L10N_OBJ)
RETURN BOOLEAN IS


   L_program          VARCHAR2(255) := 'OMSSUB_ASNOUT.CONSUME_SHIPMENT';



   L_l10n_rib_rec    "L10N_RIB_REC" := L10N_RIB_REC();


BEGIN
   --
   L_l10n_rib_rec := treat (IO_L10N_RIB_REC as L10N_RIB_REC);

   if CONSUME_SHIPMENT_CORE(O_error_message,
                            L_l10n_rib_rec.rib_msg,
                            L_l10n_rib_rec.rib_msg_type) = FALSE then


      return FALSE;
   end if;
   return TRUE;


EXCEPTION
  when OTHERS then

      O_error_message := SQL_LIB.CREATE_MSG('PACKAGE_ERROR',
                                            SQLERRM,
                                            L_program,
                                            to_char(SQLCODE));
      return FALSE;


END CONSUME_SHIPMENT;
-------------------------------------------------------------------------------------------------------
-- PRIVATE FUNCTION --
-------------------------------------------------------------------------------------------------------

FUNCTION CONSUME_SHIPMENT_CORE(O_error_message     IN OUT  VARCHAR2,
                               I_message           IN      RIB_OBJECT,
                               I_message_type      IN      VARCHAR2)
RETURN BOOLEAN IS


   L_program            VARCHAR2(255) := 'OMSSUB_ASNOUT.CONSUME_SHIPMENT_CORE';



   L_asnout_message    "RIB_ASNOutDesc_REC";
   L_distro_rec        "RIB_ASNOutDistro_REC";
   L_ctn_rec           "RIB_ASNOutCtn_REC";
   L_item_record       "RIB_ASNOutItem_REC";
   L_bol_record        shipment%rowtype;

   L_distro_record     distro_record;
   L_system_options_row  SYSTEM_OPTIONS%ROWTYPE;
   L_carton_exists     BOOLEAN := FALSE;
   L_detail_TBL        "RIB_ASNOutItem_TBL";
   L_sellable_TBL      "RIB_ASNOutItem_TBL";
   L_orderable_TBL     item_table;
   L_item_rec          item_record := NULL;

   processed_ind         VARCHAR2(1) :='N';

   L_message_id          OMS_ASNOUT_DESC.message_id%TYPE;

   L_spare_part          VARCHAR2(1) :=NULL;

   -- Local variable for Cursor C_OMS_CUS_ORD_NO
   L_oms_cust_ord_no     OMS_CUST_ORD_HEAD.oms_cust_ord_no%TYPE;

 --Local Variable for C_FULFILL_DETAIL
   L_req_qty            OMS_CO_FULFILL_DETAIL.FULFILL_REQ_QTY%TYPE;
   L_CONF_QTY           OMS_CO_FULFILL_DETAIL.FULFILL_CONF_QTY%TYPE;
   L_DELIVER_QTY        OMS_CO_FULFILL_DETAIL.FULFILL_DELIVER_QTY%TYPE;
   L_CANCEL_QTY         OMS_CO_FULFILL_DETAIL.FULFILL_CANCEL_QTY%TYPE;
   L_fulfil_no          OMS_CO_FULFILL_DETAIL.fulfill_order_no%TYPE;
   L_item               OMS_CO_FULFILL_DETAIL.ITEM%TYPE;
   L_line_no            OMS_CO_FULFILL_DETAIL.LINE_NO%TYPE;
   L_quantity           OMS_CO_FULFILL_DETAIL.FULFILL_DELIVER_QTY%TYPE;
   L_counter            NUMBER(3):=0;
   L_FROM_loc           OMS_ASNOUT_DESC.FROM_LOCATION%TYPE;
   L_FROM_loc_type      OMS_ASNOUT_DESC.FROM_LOC_TYPE%TYPE;
   L_TO_loc             OMS_ASNOUT_DESC.TO_LOCATION%TYPE;
   L_del_conf_id        OMS_CUST_ORD_LOG.oms_dlv_conf_id%TYPE;
   L_seq_no             OMS_CUST_ORD_LOG.LOG_SEQ_NO%TYPE;
   L_Event_id           OMS_CUST_ORD_LOG.EVENT_ID%TYPE;
   L_Event_comm         OMS_CUST_ORD_LOG.EVENT_COMMENTS%TYPE;
   L_consum_direct      OMS_ASNOUT_DISTRO.consumer_direct%TYPE;
   L_bol_no             OMS_ASNOUT_DESC.bol_nbr%TYPE;
   L_asn_nbr            OMS_ASNOUT_DESC.asn_nbr%TYPE;
   L_cust_no            OMS_ASNOUT_DISTRO.cust_order_nbr%TYPE;
   L_service_id         OMS_SPARE_PART_FULFILL.OMS_SERVICE_REQ_SEQ_ID%TYPE;
   L_audit_seq          OMS_SPARE_PART_AUDIT.OMS_AUDIT_SEQ_ID%TYPE;
   L_source_loc         OMS_SPARE_PART_FULFILL.SOURCE_LOCATION%TYPE;
   L_item_id            OMS_SPARE_PART_FULFILL.ITEM_ID%TYPE;
   L_asn_exists         VARCHAR2(1);

   --- Cursor to fetch the OMS_CUST_ORD_NO

   CURSOR C_OMS_CUS_ORD_NO (L_message_nbr OMS_ASNOUT_DESC.message_id%TYPE)IS

   -- Commented out for the customer order without sub cust no. if needed remove comments

   /*SELECT distinct oms_cust_ord_no
       FROM oms_cust_ord_head ocoh,
            oms_asnout_desc oaad,
            oms_asnout_distro oad
      WHERE oaad.message_id = L_message_id
        AND oaad.asn_nbr = oad.asn_nbr
        AND  ocoh.cust_order_no = oad.cust_order_nbr;*/

      SELECT distinct ocoh.oms_cust_ord_no
        FROM oms_cust_ord_head ocoh,
             oms_asnout_desc oaad,
             oms_asnout_distro oad
       WHERE oaad.message_id = L_message_nbr
         AND oad.asn_nbr = oaad.asn_nbr
         AND (ocoh.cust_order_no = oad.cust_order_nbr
              OR
              (ocoh.cust_order_no = SUBSTR(oad.cust_order_nbr,1,INSTR(oad.cust_order_nbr,'-') - 1)
              AND ocoh.sub_cust_order_no = SUBSTR(oad.cust_order_nbr,INSTR(oad.cust_order_nbr,'-') + 1, LENGTH(oad.cust_order_nbr)))
             )
         AND ocoh.status = 'S';

   --- CURSOR to Fetch the rrecords for fulfillment

   CURSOR C_FULFILL_DETAIL (L_message_nbr OMS_ASNOUT_DESC.message_id%TYPE,
                            L_oms_cust_ord_no OMS_CUST_ORD_HEAD.oms_cust_ord_no%TYPE) IS
      SELECT ocfd.oms_cust_ord_no,
             oaai.item_id,
             ocfd.line_no,
             SUM(oaai.unit_qty),
             oad.asn_nbr,
             oad.consumer_direct,
             oaad.BOL_NBR,
             ocfd.fulfill_order_no,
             ocfd.FULFILL_REQ_QTY ,
             ocfd.FULFILL_CONF_QTY ,
             ocfd.FULFILL_DELIVER_QTY ,
             ocfd.FULFILL_CANCEL_QTY,
             oaad.from_location,
             oaad.from_loc_type,
             oaad.to_location,
             (CASE WHEN (oad.consumer_direct = 'Y' and oaad.TO_STOCKHOLDING_IND ='N') THEN 'DL'
              ELSE 'SH'
              END )EVENT_ID,
             (CASE WHEN (oad.consumer_direct = 'Y' and oaad.TO_STOCKHOLDING_IND ='N') THEN 'Delivered to customer'
              ELSE 'RESERVATION'
              END )EVENT_COMMENTS
        FROM OMS_CO_FULFILL_DETAIL ocfd,
             OMS_ASNOUT_DESC oaad,
             OMS_ASNOUT_DISTRO oad,
             OMS_ASNOUT_ITEM oaai,
			 OMS_CUST_ORD_HEAD ocoh
       WHERE ocfd.oms_cust_ord_no = L_oms_cust_ord_no
         AND ocfd.item = oaai.item_id
		 AND ocoh.oms_cust_ord_no = ocfd.oms_cust_ord_no
		 AND (ocoh.cust_order_no = oad.cust_order_nbr
              or
              (ocoh.cust_order_no = substr(oad.cust_order_nbr,1,instr(oad.cust_order_nbr,'-') - 1)
              AND ocoh.sub_cust_order_no = SUBSTR(oad.cust_order_nbr,INSTR(oad.cust_order_nbr,'-') + 1, LENGTH(oad.cust_order_nbr)))
             )
         AND ocoh.status = 'S'
		 AND oad.distro_nbr = oaai.distro_nbr
         AND oaad.message_id = L_message_nbr
         AND oad.fulfill_order_nbr = ocfd.fulfill_order_no
         AND oaad.asn_nbr = oaai.asn_nbr
         AND oaad.asn_nbr = oad.asn_nbr
         AND oad.asn_nbr  = oaai.asn_nbr
         AND oaad.message_id = oaai.message_id
         AND oaad.message_id = oad.message_id
       GROUP BY ocfd.oms_cust_ord_no,
             oaai.item_id,
             ocfd.line_no,
             oad.asn_nbr,
             oad.consumer_direct,
             oaad.BOL_NBR,
             ocfd.fulfill_order_no,
             ocfd.FULFILL_REQ_QTY ,
             ocfd.FULFILL_CONF_QTY ,
             ocfd.FULFILL_DELIVER_QTY ,
             ocfd.FULFILL_CANCEL_QTY,
             oaad.from_location,
             oaad.from_loc_type,
             oaad.to_location,
             (CASE WHEN (oad.consumer_direct = 'Y' and oaad.TO_STOCKHOLDING_IND ='N') THEN 'DL'
              ELSE 'SH'
              END ),
             (CASE WHEN (oad.consumer_direct = 'Y' and oaad.TO_STOCKHOLDING_IND ='N') THEN 'Delivered to customer'
              ELSE 'RESERVATION'
              END );

   CURSOR C_SPARE_PART IS
      SELECT DISTINCT 'X',
             OMS_SERVICE_REQ_SEQ_ID,
             SOURCE_LOCATION,
             ITEM_ID
        FROM oms_spare_part_fulfill
       WHERE to_char(tran_id) = L_asnout_message.BOL_NBR;

   CURSOR C_ASN_NBR IS
      SELECT 'X'
        FROM oms_asnout_desc
       WHERE asn_nbr = L_asn_nbr;

   CURSOR C_ITEM_QTY(L_message_cust OMS_ASNOUT_DESC.MESSAGE_ID%TYPE) IS
      SELECT oaai.unit_qty
        FROM oms_asnout_item oaai,
             oms_asnout_desc ossd
       WHERE ossd.message_id = L_message_id
         AND oaai.asn_nbr = ossd.asn_nbr;

   CURSOR C_CHECK_SHIPPING_ITEM IS
      SELECT ocoi.item,
             ocoi.line_no,
             ocoi.QTY_ORDERED_SUOM  QUANTITY
        FROM oms_cust_ord_item ocoi,
             oms_system_parameters osp,
             item_master im
       WHERE ocoi.oms_cust_ord_no = L_oms_cust_ord_no
         AND ocoi.item            = im.item
         AND im.dept              = osp.parameter_value
         AND osp.parameter_name   = 'SHIPPING_CHARGE_DEPT'
         AND osp.parameter_id     = 'OMS_SYSTEM_OPTION'
         AND NVL(ocoi.cum_qty_delivered,0) = 0;

   CURSOR C_CHECK_INVENTORY_ITEM IS
      SELECT ocoi.item,
             ocoi.line_no,
             ocoi.qty_ordered_suom  quantity
        FROM oms_cust_ord_item ocoi,
             item_master im
       WHERE ocoi.oms_cust_ord_no = L_oms_cust_ord_no
         AND ocoi.item            = im.item
         AND im.inventory_ind     = 'N'
         AND ocoi.LINE_LINK_NO is not null
         AND ocoi.LINE_LINK_NO in (select line_no from OMS_CUST_ORD_ITEM x where x.line_no = ocoi.LINE_LINK_NO and NVL(x.CUM_QTY_DELIVERED,0) <> 0 and x.oms_cust_ord_no = ocoi.oms_cust_ord_no and rownum =1)
         AND NVL(ocoi.cum_qty_delivered,0) = 0;
         
   CURSOR C_CHECK_INVENTORY_ITEM_NLINK IS
      SELECT ocoi.item,
             ocoi.line_no,
             ocoi.qty_ordered_suom  quantity
        FROM oms_cust_ord_item ocoi,
             item_master im
       WHERE ocoi.oms_cust_ord_no = L_oms_cust_ord_no
         AND ocoi.item            = im.item
         AND im.inventory_ind     = 'N'
         AND ocoi.LINE_LINK_NO is  null
         AND 0 = (select SUM (NVL(QTY_ORDERED_SUOM,0) - NVL(CUM_QTY_DELIVERED,0) - NVL(QTY_CANCELLED,0)) from OMS_CUST_ORD_ITEM x, ITEM_MASTER i where i.item = x.item and i.inventory_ind  = 'Y' and x.oms_cust_ord_no = ocoi.oms_cust_ord_no)
         AND NVL(ocoi.cum_qty_delivered,0) = 0;

BEGIN

   L_asnout_message := treat(I_message as "RIB_ASNOutDesc_REC");
   -- L_distro_rec := L_asnout_message.ASNOUTDISTRO_TBL(1);
   -- check STOCKHOLDING indicator to filter the message from normal transfer and CO reservation and transfer.

   IF (L_asnout_message.TO_STOCKHOLDING_IND ='N') OR (L_asnout_message.TO_LOCATION is null )   THEN
      processed_ind :='Y';
   ELSE
      DBMS_OUTPUT.PUT_LINE('It is not a customer order/Reservation');
   END IF;

   FOR I IN L_asnout_message.ASNOutDistro_TBL.FIRST .. L_asnout_message.ASNOutDistro_TBL.LAST
   LOOP
      L_distro_rec := L_asnout_message.ASNOutDistro_TBL(I);
      IF (L_distro_rec.consumer_direct = 'Y') AND ((L_asnout_message.TO_STOCKHOLDING_IND ='N')  OR (L_asnout_message.TO_LOCATION is null ))THEN
         processed_ind :='Y';
      ELSE
         OPEN C_SPARE_PART;
         FETCH C_SPARE_PART INTO L_spare_part,
                                 L_service_id,
                                 L_source_loc,
                                 L_item_id;


         IF C_SPARE_PART%NOTFOUND THEN
            processed_ind :='N';
         ELSE
            processed_ind :='Y';
         END IF;
         CLOSE C_SPARE_PART;
      END IF;
   END LOOP;
   IF (processed_ind = 'Y') THEN  -- 1st IF

         SELECT oms_asnout_msg_seq.nextval
           INTO L_message_id
           FROM dual;

         IF L_asnout_message is NULL THEN
            O_error_message := SQL_LIB.CREATE_MSG('RMSSUB_INV_MESSAGE', NULL, NULL, NULL);
            RETURN FALSE;
         END IF;

         INSERT INTO OMS_ASNOUT_DESC(MESSAGE_ID, --           NOT NULL VARCHAR2(30)
                                     SCHEDULE_NBR, --          NOT NULL NUMBER(8)
                                     AUTO_RECEIVE, --                   VARCHAR2(1)
                                     TO_LOCATION, --                    VARCHAR2(10)
                                     TO_LOC_TYPE, --                    VARCHAR2(1)
                                     TO_STORE_TYPE, --                  VARCHAR2(1)
                                     TO_STOCKHOLDING_IND, --            VARCHAR2(1)
                                     FROM_LOCATION , --                  VARCHAR2(10)
                                     FROM_LOC_TYPE, --                  VARCHAR2(1)
                                     FROM_STORE_TYPE, --                VARCHAR2(1)
                                     FROM_STOCKHOLDING_IND, --          VARCHAR2(1)
                                     ASN_NBR , --                        VARCHAR2(30)
                                     ASN_TYPE, --                       VARCHAR2(1)
                                     CONTAINER_QTY , --                  NUMBER(6)
                                     BOL_NBR, ---                        VARCHAR2(17)
                                     SHIPMENT_DATE, --                  TIMESTAMP(6)
                                     EST_ARR_DATE, --                   TIMESTAMP(6)
                                     SHIP_ADDRESS1, --                  VARCHAR2(240)
                                     SHIP_ADDRESS2, --                  VARCHAR2(240)
                                     SHIP_ADDRESS3, --                  VARCHAR2(240)
                                     SHIP_ADDRESS4, --                  VARCHAR2(240)
                                     SHIP_ADDRESS5, --                  VARCHAR2(240)
                                     SHIP_CITY, --                      VARCHAR2(120)
                                     SHIP_STATE, --                     VARCHAR2(3)
                                     SHIP_ZIP, --                       VARCHAR2(30)
                                     SHIP_COUNTRY_ID, --                VARCHAR2(3)
                                     TRAILER_NBR, --                    VARCHAR2(12)
                                     SEAL_NBR, --                       VARCHAR2(12)
                                     TRANSSHIPMENT_NBR, --              VARCHAR2(30)
                                     COMMENTS, --                       VARCHAR2(2000)
                                     CARRIER_CODE, --                   VARCHAR2(4)
                                     CARRIER_SERVICE_CODE, --           VARCHAR2(6)
                                     MSG_STATUS, --            NOT NULL VARCHAR2(10)
                                     CREATE_TIMESTAMP, --      NOT NULL TIMESTAMP(6)
                                     LAST_UPDATE_TIMESTAMP --          TIMESTAMP(6)
                                    )
                              VALUES(L_message_id,--oms_asnout_msg_seq.nextval, --L_asnout_message.RIB_OID,
                                     L_asnout_message.SCHEDULE_NBR,
                                     L_asnout_message.AUTO_RECEIVE,
                                     L_asnout_message.TO_LOCATION,
                                     L_asnout_message.TO_LOC_TYPE,
                                     L_asnout_message.TO_STORE_TYPE,
                                     L_asnout_message.TO_STOCKHOLDING_IND, --            VARCHAR2(1)
                                     L_asnout_message.FROM_LOCATION , --                  VARCHAR2(10)
                                     L_asnout_message.FROM_LOC_TYPE, --                  VARCHAR2(1)
                                     L_asnout_message.FROM_STORE_TYPE, --                VARCHAR2(1)
                                     L_asnout_message.FROM_STOCKHOLDING_IND, --          VARCHAR2(1)
                                     L_asnout_message.ASN_NBR , --                        VARCHAR2(30)
                                     L_asnout_message.ASN_TYPE, --                       VARCHAR2(1)
                                     L_asnout_message.CONTAINER_QTY , --                  NUMBER(6)
                                     L_asnout_message.BOL_NBR, ---                        VARCHAR2(17)
                                     L_asnout_message.SHIPMENT_DATE, --                  TIMESTAMP(6)
                                     L_asnout_message.EST_ARR_DATE, --                   TIMESTAMP(6)
                                     L_asnout_message.SHIP_ADDRESS1, --                  VARCHAR2(240)
                                     L_asnout_message.SHIP_ADDRESS2, --                  VARCHAR2(240)
                                     L_asnout_message.SHIP_ADDRESS3, --                  VARCHAR2(240)
                                     L_asnout_message.SHIP_ADDRESS4, --                  VARCHAR2(240)
                                     L_asnout_message.SHIP_ADDRESS5, --                  VARCHAR2(240)
                                     L_asnout_message.SHIP_CITY, --                      VARCHAR2(120)
                                     L_asnout_message.SHIP_STATE, --                     VARCHAR2(3)
                                     L_asnout_message.SHIP_ZIP, --                       VARCHAR2(30)
                                     L_asnout_message.SHIP_COUNTRY_ID, --                VARCHAR2(3)
                                     L_asnout_message.TRAILER_NBR, --                    VARCHAR2(12)
                                     L_asnout_message.SEAL_NBR, --                       VARCHAR2(12)
                                     L_asnout_message.TRANSSHIPMENT_NBR, --              VARCHAR2(30)
                                     L_asnout_message.COMMENTS, --                       VARCHAR2(2000)
                                     L_asnout_message.CARRIER_CODE, --                   VARCHAR2(4)
                                     L_asnout_message.CARRIER_SERVICE_CODE, --           VARCHAR2(6)
                                     'S', --            NOT NULL VARCHAR2(10)
                                     SYSDATE,
                                     SYSDATE);


      FOR I IN L_asnout_message.ASNOutDistro_TBL.FIRST .. L_asnout_message.ASNOutDistro_TBL.LAST
      LOOP
         L_distro_rec := L_asnout_message.ASNOutDistro_TBL(I);
         INSERT INTO OMS_ASNOUT_DISTRO(message_id       ,
                                       asn_nbr          ,
                                       distro_nbr       ,
                                       distro_doc_type  ,
                                       cust_order_nbr   ,
                                       fulfill_order_nbr,
                                       consumer_direct  ,
                                       comments
                                      )
                                VALUES(L_message_id,
                                       L_asnout_message.ASN_NBR,
                                       L_distro_rec.distro_nbr,
                                       L_distro_rec.distro_doc_type,
                                       L_distro_rec.cust_order_nbr,
                                       L_distro_rec.fulfill_order_nbr,
                                       L_distro_rec.consumer_direct,
                                       L_distro_rec.comments
                                      );

         L_consum_direct := L_distro_rec.consumer_direct;

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 1, L_distro_rec.cust_order_nbr, null, 'OMSSUB_ASNOUT', 'OMS ASNOUT TABLES ENTRIES INSERTED', sysdate
                        FROM DUAL;        

         FOR J in L_distro_rec.ASNOutCtn_TBL.FIRST .. L_distro_rec.ASNOutCtn_TBL.LAST
         LOOP
            L_ctn_rec := L_distro_rec.ASNOutCtn_TBL(J);
            INSERT INTO OMS_ASNOUT_CTN(message_id       ,
                                       asn_nbr          ,
                                       distro_nbr       ,
                                       container_id        ,
                                       container_weight    ,
                                       container_length    ,
                                       container_width     ,
                                       container_height    ,
                                       container_cube      ,
                                       expedite_flag       ,
                                       in_store_date       ,
                                       freight_charge      ,
                                       tracking_nbr        ,
                                       master_container_id ,
                                       comments            ,
                                       weight              ,
                                       weight_uom          ,
                                       carrier_shipment_nbr,
                                       original_item_id
                                      )
                                VALUES(L_message_id,
                                       L_asnout_message.ASN_NBR,
                                       L_distro_rec.distro_nbr,
                                       L_ctn_rec.container_id        ,
                                       L_ctn_rec.container_weight    ,
                                       L_ctn_rec.container_length    ,
                                       L_ctn_rec.container_width     ,
                                       L_ctn_rec.container_height    ,
                                       L_ctn_rec.container_cube      ,
                                       L_ctn_rec.expedite_flag       ,
                                       L_ctn_rec.in_store_date       ,
                                       L_ctn_rec.freight_charge      ,
                                       L_ctn_rec.tracking_nbr        ,
                                       L_ctn_rec.master_container_id ,
                                       L_ctn_rec.comments            ,
                                       L_ctn_rec.weight              ,
                                       L_ctn_rec.weight_uom          ,
                                       L_ctn_rec.carrier_shipment_nbr,
                                       L_ctn_rec.original_item_id
                                      );

            FOR K in L_ctn_rec.ASNOutItem_TBL.FIRST .. L_ctn_rec.ASNOutItem_TBL.LAST
            LOOP
               L_item_record := L_ctn_rec.ASNOutItem_TBL(K);
               INSERT INTO OMS_ASNOUT_ITEM(message_id,
                                           asn_nbr          ,
                                           distro_nbr       ,
                                           container_id     ,
                                           item_id                ,
                                           unit_qty               ,
                                           gross_cost             ,
                                           priority_level         ,
                                           order_line_nbr         ,
                                           lot_nbr                ,
                                           final_location         ,
                                           from_disposition       ,
                                           to_disposition         ,
                                           voucher_number         ,
                                           voucher_expiration_date,
                                           container_qty          ,
                                           comments               ,
                                           unit_cost              ,
                                           base_cost              ,
                                           weight                 ,
                                           weight_uom
                                          )
                                    VALUES(L_message_id,
                                           L_asnout_message.ASN_NBR ,
                                           L_distro_rec.distro_nbr  ,
                                           L_ctn_rec.container_id,
                                           L_item_record.item_id                ,
                                           L_item_record.unit_qty               ,
                                           L_item_record.gross_cost             ,
                                           L_item_record.priority_level         ,
                                           L_item_record.order_line_nbr         ,
                                           L_item_record.lot_nbr                ,
                                           L_item_record.final_location         ,
                                           L_item_record.from_disposition       ,
                                           L_item_record.to_disposition         ,
                                           L_item_record.voucher_number         ,
                                           L_item_record.voucher_expiration_date,
                                           L_item_record.container_qty          ,
                                           L_item_record.comments               ,
                                           L_item_record.unit_cost              ,
                                           L_item_record.base_cost              ,
                                           L_item_record.weight                 ,
                                           L_item_record.weight_uom
                                          );

            END LOOP;
         END LOOP;
      END LOOP;
    
      IF (L_asnout_message.TO_STOCKHOLDING_IND = 'N') THEN    -- 2nd IF
         OPEN C_OMS_CUS_ORD_NO(L_message_id);
         LOOP
            FETCH C_OMS_CUS_ORD_NO into L_oms_cust_ord_no;
            L_Counter := 0;
             
            EXIT WHEN C_OMS_CUS_ORD_NO%NOTFOUND;
            OPEN C_FULFILL_DETAIL(L_message_id,L_oms_cust_ord_no);
            LOOP
               FETCH C_FULFILL_DETAIL INTO L_oms_cust_ord_no,
                                           L_item,
                                           L_line_no,
                                           L_quantity,
                                           L_del_conf_id,
                                           L_consum_direct,
                                           L_bol_no,
                                           L_fulfil_no,
                                           L_req_qty,
                                           L_CONF_QTY,
                                           L_DELIVER_QTY,
                                           L_CANCEL_QTY,
                                           L_FROM_loc,
                                           L_FROM_loc_type,
                                           L_TO_loc,
                                           L_Event_id,
                                           L_Event_comm;
               EXIT WHEN C_FULFILL_DETAIL%NOTFOUND;
               IF (L_req_qty <= (L_DELIVER_QTY + L_CANCEL_QTY)) THEN   -- 3rd IF
                  DBMS_OUTPUT.PUT_LINE('This item is already fulfilled or cancelled');
                  -- Inserting into Error tables
                  -- Calling Error handling procedure_key P_INS_ERROR_DTL
                  P_INS_ERROR_DTL ('OMSSUB_ASNOUT',
                                   'CONSUME_SHIPMENT_CORE',
                                   'MESSAGE_ID',
                                   L_message_id,
                                   'Fulfillment Number',
                                   L_fulfil_no,
                                   'Item',
                                   L_item,
                                   null,
                                   'The item is already fulfilled or cancelled',
                                   SYSDATE);
               ELSE
                  UPDATE OMS_CUST_ORD_ITEM
                     SET CUM_QTY_DELIVERED = nvl(CUM_QTY_DELIVERED,0) + L_quantity,
                         LAST_UPDATE_DATETIME = SYSDATE
                   WHERE oms_cust_ord_no = L_oms_cust_ord_no
                     AND item = L_item
                     AND line_no = L_line_no;

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 2, null, L_oms_cust_ord_no, 'OMSSUB_ASNOUT', 'OMS_CUST_ORD_ITEM VALUES UPDATED', sysdate
                        FROM DUAL;        

      -- update in fulfill _detail table for item_fulfill

                 UPDATE OMS_CO_FULFILL_DETAIL
                     SET FULFILL_DELIVER_QTY = NVL(FULFILL_DELIVER_QTY,0) + L_quantity,
                         LAST_UPDATE_DATETIME = SYSDATE
                   WHERE oms_cust_ord_no = L_oms_cust_ord_no
                     AND fulfill_order_no = L_fulfil_no
                     AND item = L_item
                     AND line_no = L_line_no;

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 3, null, L_oms_cust_ord_no, 'OMSSUB_ASNOUT', 'OMS_CO_FULFILL_DETAIL VALUES UPDATED', sysdate
                        FROM DUAL;        

 --- New change for updaing the close date time
                  UPDATE OMS_CUST_ORD_HEAD
                     SET CLOSE_DATETIME = sysdate,
					     LAST_UPDATE_DATETIME = sysdate
                   WHERE NOT EXISTS (SELECT 1
                                       FROM oms_cust_ord_item ocoi
                                      WHERE NVL(ocoi.QTY_ORDERED_SUOM,0) > NVL(ocoi.CUM_QTY_DELIVERED,0) + NVL(ocoi.QTY_CANCELLED,0)
                                        AND ocoi.OMS_CUST_ORD_NO = L_oms_cust_ord_no)
                     AND OMS_CUST_ORD_NO = L_oms_cust_ord_no;

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 4, null, L_oms_cust_ord_no, 'OMSSUB_ASNOUT', 'OMS_CUST_ORD_HEAD VALUES UPDATED', sysdate
                        FROM DUAL;        

      --  INSERT INTO OMS_RTLOG_PUBLISH_LOG table

                  INSERT INTO OMS_RTLOG_PUBLISH_LOG(OMS_RTLOG_PUB_SEQ_NO,
                                                    OMS_CUST_ORD_NO,
                                                    FULFILL_ORDER_NO,
                                                    ITEM,
                                                    LINE_NO,
                                                    QTY,
                                                    LOCATION,
                                                    TRAN_TYPE,
                                                    PUBLISHED_IND,
                                                    ERROR_MESSAGE,
                                                    CREATE_DATETIME,
                                                    LAST_UPDATE_DATETIME,
                                                    OMS_CANCEL_ID)
                                             VALUES(OMS_RTLOG_PUB_SEQ_NO_SEQ.nextval,
                                                    L_oms_cust_ord_no,
                                                    L_fulfil_no,
                                                    L_item,
                                                    L_line_no,
                                                    L_quantity,
                                                    (CASE
                                                     WHEN L_FROM_loc_type='S' THEN L_FROM_LOC
                                                     WHEN L_FROM_loc_type='W' THEN L_TO_loc
                                                     END),
                                                    'ORD',
                                                    'N',
                                                    NULL,
                                                    SYSDATE,
                                                    SYSDATE,
                                                    NULL);

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 5, null, L_oms_cust_ord_no, 'OMSSUB_ASNOUT', 'OMS_RTLOG_PUBLISH_LOG VALUES INSERTED', sysdate
                        FROM DUAL;        

                  IF (L_consum_direct = 'Y') THEN
                     -- Counter for inserting LOG table only once
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
                                                     NULL);

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 6, null, L_oms_cust_ord_no, 'OMSSUB_ASNOUT', 'OMS_CUST_ORD_LOG VALUES INSERTED', sysdate
                        FROM DUAL;        

                     END IF;

                    -- Insert into log item table

                     INSERT INTO OMS_CUST_ORD_LOG_ITEM(ITEM,
                                                       LINE_NO,
                                                       LOG_SEQ_NO,
                                                       QTY,
                                                       FULFILL_ORDER_NO,
                                                       CREATE_DATETIME)
                                                VALUES(L_item,
                                                       L_line_no,
                                                       L_seq_no,
                                                       L_quantity,
                                                       L_fulfil_no,
                                                       SYSDATE);

                  END IF;
               END IF; -- 3rd IF
            END LOOP;
            CLOSE C_FULFILL_DETAIL;
         END LOOP;
         CLOSE C_OMS_CUS_ORD_NO;

		 
		 OPEN C_OMS_CUS_ORD_NO(L_message_id);
         LOOP
            FETCH C_OMS_CUS_ORD_NO into L_oms_cust_ord_no;
				EXIT WHEN C_OMS_CUS_ORD_NO%NOTFOUND;

         FOR rec IN C_CHECK_SHIPPING_ITEM
         LOOP
            UPDATE OMS_CUST_ORD_ITEM
               SET CUM_QTY_DELIVERED = nvl(CUM_QTY_DELIVERED,0) + QTY_ORDERED_SUOM,
                   LAST_UPDATE_DATETIME = SYSDATE
             WHERE oms_cust_ord_no = L_oms_cust_ord_no
               AND item = rec.item
               AND line_no = rec.line_no
               AND NVL(CUM_QTY_DELIVERED,0) = 0;

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 7, null, L_oms_cust_ord_no, 'OMSSUB_ASNOUT', 'SHIPPING ITEM ENTRY UPDATED IN OMS_CUST_ORD_ITEM', sysdate
                        FROM DUAL;        

            -- Insert into OMS_RTLOG_PUBLISH_LOG
            INSERT INTO OMS_RTLOG_PUBLISH_LOG(OMS_RTLOG_PUB_SEQ_NO,
                                              OMS_CUST_ORD_NO,
                                              FULFILL_ORDER_NO,
                                              ITEM,
                                              LINE_NO,
                                              QTY,
                                              LOCATION,
                                              TRAN_TYPE,
                                              PUBLISHED_IND,
                                              ERROR_MESSAGE,
                                              CREATE_DATETIME,
                                              LAST_UPDATE_DATETIME,
                                              OMS_CANCEL_ID)
                                       VALUES(OMS_RTLOG_PUB_SEQ_NO_SEQ.nextval,
                                              L_oms_cust_ord_no,
                                              L_fulfil_no,
                                              rec.item,
                                              rec.line_no,
                                              rec.quantity,
                                              (CASE
                                               WHEN L_FROM_loc_type='S' THEN L_FROM_LOC
                                               WHEN L_FROM_loc_type='W' THEN L_TO_loc
                                               END),
                                              'ORD',
                                              'N',
                                              NULL,
                                              SYSDATE,
                                              SYSDATE,
                                              NULL);
         END LOOP;
		 END LOOP;
         CLOSE C_OMS_CUS_ORD_NO;
		 
		  OPEN C_OMS_CUS_ORD_NO(L_message_id);
         LOOP
            FETCH C_OMS_CUS_ORD_NO into L_oms_cust_ord_no;
				EXIT WHEN C_OMS_CUS_ORD_NO%NOTFOUND;
         FOR item_rec IN C_CHECK_INVENTORY_ITEM
         LOOP
            UPDATE OMS_CUST_ORD_ITEM a
               SET CUM_QTY_DELIVERED = nvl(CUM_QTY_DELIVERED,0) + QTY_ORDERED_SUOM,
                   LAST_UPDATE_DATETIME = SYSDATE
             WHERE oms_cust_ord_no = L_oms_cust_ord_no
               AND item = item_rec.item
               AND line_no = item_rec.line_no
               AND NVL(CUM_QTY_DELIVERED,0) = 0;

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 8, null, L_oms_cust_ord_no, 'OMSSUB_ASNOUT', 'NON INVENTORY LINK ITEM ENTRY UPDATED IN OMS_CUST_ORD_ITEM', sysdate
                        FROM DUAL;        

            -- Insert into OMS_RTLOG_PUBLISH_LOG
            INSERT INTO OMS_RTLOG_PUBLISH_LOG(OMS_RTLOG_PUB_SEQ_NO,
                                              OMS_CUST_ORD_NO,
                                              FULFILL_ORDER_NO,
                                              ITEM,
                                              LINE_NO,
                                              QTY,
                                              LOCATION,
                                              TRAN_TYPE,
                                              PUBLISHED_IND,
                                              ERROR_MESSAGE,
                                              CREATE_DATETIME,
                                              LAST_UPDATE_DATETIME,
                                              OMS_CANCEL_ID)
                                       VALUES(OMS_RTLOG_PUB_SEQ_NO_SEQ.nextval,
                                              L_oms_cust_ord_no,
                                              L_fulfil_no,
                                              item_rec.item,
                                              item_rec.line_no,
                                              item_rec.quantity,
                                              (CASE
                                               WHEN L_FROM_loc_type='S' THEN L_FROM_LOC
                                               WHEN L_FROM_loc_type='W' THEN L_TO_loc
                                               END),
                                              'ORD',
                                              'N',
                                              NULL,
                                              SYSDATE,
                                              SYSDATE,
                                              NULL);
         END LOOP;
		 END LOOP;
         CLOSE C_OMS_CUS_ORD_NO;

		  OPEN C_OMS_CUS_ORD_NO(L_message_id);
         LOOP
            FETCH C_OMS_CUS_ORD_NO into L_oms_cust_ord_no;
				EXIT WHEN C_OMS_CUS_ORD_NO%NOTFOUND;
         FOR item_rec IN C_CHECK_INVENTORY_ITEM_NLINK
         LOOP
            UPDATE OMS_CUST_ORD_ITEM a
               SET CUM_QTY_DELIVERED = nvl(CUM_QTY_DELIVERED,0) + QTY_ORDERED_SUOM,
                   LAST_UPDATE_DATETIME = SYSDATE
             WHERE oms_cust_ord_no = L_oms_cust_ord_no
               AND item = item_rec.item
               AND line_no = item_rec.line_no
               AND NVL(CUM_QTY_DELIVERED,0) = 0;

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 9, null, L_oms_cust_ord_no, 'OMSSUB_ASNOUT', 'NON INVENTORY NON ITEM ENTRY UPDATED IN OMS_CUST_ORD_ITEM', sysdate
                        FROM DUAL;        

            -- Insert into OMS_RTLOG_PUBLISH_LOG
            INSERT INTO OMS_RTLOG_PUBLISH_LOG(OMS_RTLOG_PUB_SEQ_NO,
                                              OMS_CUST_ORD_NO,
                                              FULFILL_ORDER_NO,
                                              ITEM,
                                              LINE_NO,
                                              QTY,
                                              LOCATION,
                                              TRAN_TYPE,
                                              PUBLISHED_IND,
                                              ERROR_MESSAGE,
                                              CREATE_DATETIME,
                                              LAST_UPDATE_DATETIME,
                                              OMS_CANCEL_ID)
                                       VALUES(OMS_RTLOG_PUB_SEQ_NO_SEQ.nextval,
                                              L_oms_cust_ord_no,
                                              L_fulfil_no,
                                              item_rec.item,
                                              item_rec.line_no,
                                              item_rec.quantity,
                                              (CASE
                                               WHEN L_FROM_loc_type='S' THEN L_FROM_LOC
                                               WHEN L_FROM_loc_type='W' THEN L_TO_loc
                                               END),
                                              'ORD',
                                              'N',
                                              NULL,
                                              SYSDATE,
                                              SYSDATE,
                                              NULL);
         END LOOP;
		 END LOOP;
         CLOSE C_OMS_CUS_ORD_NO;
        
          OPEN C_OMS_CUS_ORD_NO(L_message_id);
         LOOP
            FETCH C_OMS_CUS_ORD_NO into L_oms_cust_ord_no;
				EXIT WHEN C_OMS_CUS_ORD_NO%NOTFOUND;
		 UPDATE OMS_CUST_ORD_HEAD ocoh
                     SET CLOSE_DATETIME = sysdate
                   WHERE NOT EXISTS (SELECT 1
                                       FROM oms_cust_ord_item ocoi
                                      WHERE NVL(ocoi.QTY_ORDERED_SUOM,0) > NVL(ocoi.CUM_QTY_DELIVERED,0) + NVL(ocoi.QTY_CANCELLED,0)
                                        AND ocoi.OMS_CUST_ORD_NO = L_oms_cust_ord_no)
                     AND ocoh.OMS_CUST_ORD_NO = L_oms_cust_ord_no;
                     
             END LOOP;
         CLOSE C_OMS_CUS_ORD_NO;
         /*UPDATE OMS_CUST_ORD_HEAD ocoh
            SET CLOSE_DATETIME = sysdate
          WHERE NOT EXISTS (SELECT 1
                              FROM oms_cust_ord_item ocoi
                             WHERE ocoi.oms_cust_ord_no = ocoh.oms_cust_ord_no
                               AND NVL(QTY_ORDERED_SUOM,0) > NVL(CUM_QTY_DELIVERED,0) + NVL(QTY_CANCELLED,0)
                           );*/
      END IF; -- 2nd if

      OPEN C_SPARE_PART ;
      FETCH C_SPARE_PART INTO L_spare_part,
                              L_service_id,
                              L_source_loc,
                              L_item_id;

      OPEN C_ITEM_QTY(L_message_id);
      FETCH C_ITEM_QTY INTO L_quantity;

      IF ((L_consum_direct ='N') AND (L_spare_part ='X')) THEN -- Changed to X instead of Y
         UPDATE OMS_SPARE_PART_FULFILL
            SET quantity_shipped = nvl(quantity_shipped,0) + L_quantity,
                last_updated_datetime = SYSDATE
          WHERE item_id = L_item_id
            AND OMS_SERVICE_REQ_SEQ_ID = L_service_id
            AND tran_id = L_asnout_message.BOL_NBR;

         UPDATE OMS_SPARE_PART_HEADER
            SET CUM_QUANTITY_SHIPPED = nvl(CUM_QUANTITY_SHIPPED,0) + L_quantity,
                last_update_datetime = SYSDATE
          WHERE OMS_SERVICE_REQ_SEQ_ID = L_service_id
            AND item_id = L_item_id;

         SELECT OMS_SPAREPART_AUDIT_SEQ.nextval
           INTO L_audit_seq
           FROM dual;

         INSERT INTO OMS_SPARE_PART_AUDIT(OMS_AUDIT_SEQ_ID,
                                          OMS_SERVICE_REQ_SEQ_ID,
                                          SOURCE_LOCATION,
                                          ITEM_ID,
                                          EVENT_TYPE,
                                          QUANTITY,
                                          CREATED_BY,
                                          CREATE_DATETIME)
                                   VALUES(L_audit_seq,
                                          L_service_id,
                                          L_source_loc,
                                          L_item_id,
                                          'SHIPED',
                                          L_quantity,
                                          USER,
                                          SYSDATE);

      END IF;
      CLOSE C_ITEM_QTY;
      CLOSE C_SPARE_PART;

      IF(L_spare_part IS NULL) THEN
      -- Calling Seibel web service
         L_seibel_out:=OMS_ASNOUT_WS_INVOKER.f_ASNOUT_Siebel_COStatusUpdate(L_message_id);
      END IF;
   END IF; -- 1st IF
   IF SYSTEM_OPTIONS_SQL.GET_SYSTEM_OPTIONS(O_error_message,
                                            L_system_options_row) = FALSE THEN
      RETURN FALSE;
   END IF;
   ------------------
   ------NEW CODE----
   ---------------

 IF (processed_ind = 'N')  and (L_asnout_message.TO_STOCKHOLDING_IND ='Y') AND (L_distro_rec.consumer_direct = 'Y') THEN
         SELECT oms_asnout_msg_seq.nextval
           INTO L_message_id
           FROM dual;

         IF L_asnout_message is NULL THEN
            O_error_message := SQL_LIB.CREATE_MSG('RMSSUB_INV_MESSAGE', NULL, NULL, NULL);
            RETURN FALSE;
         END IF;

         INSERT INTO OMS_ASNOUT_DESC(MESSAGE_ID, --           NOT NULL VARCHAR2(30)
                                     SCHEDULE_NBR, --          NOT NULL NUMBER(8)
                                     AUTO_RECEIVE, --                   VARCHAR2(1)
                                     TO_LOCATION, --                    VARCHAR2(10)
                                     TO_LOC_TYPE, --                    VARCHAR2(1)
                                     TO_STORE_TYPE, --                  VARCHAR2(1)
                                     TO_STOCKHOLDING_IND, --            VARCHAR2(1)
                                     FROM_LOCATION , --                  VARCHAR2(10)
                                     FROM_LOC_TYPE, --                  VARCHAR2(1)
                                     FROM_STORE_TYPE, --                VARCHAR2(1)
                                     FROM_STOCKHOLDING_IND, --          VARCHAR2(1)
                                     ASN_NBR , --                        VARCHAR2(30)
                                     ASN_TYPE, --                       VARCHAR2(1)
                                     CONTAINER_QTY , --                  NUMBER(6)
                                     BOL_NBR, ---                        VARCHAR2(17)
                                     SHIPMENT_DATE, --                  TIMESTAMP(6)
                                     EST_ARR_DATE, --                   TIMESTAMP(6)
                                     SHIP_ADDRESS1, --                  VARCHAR2(240)
                                     SHIP_ADDRESS2, --                  VARCHAR2(240)
                                     SHIP_ADDRESS3, --                  VARCHAR2(240)
                                     SHIP_ADDRESS4, --                  VARCHAR2(240)
                                     SHIP_ADDRESS5, --                  VARCHAR2(240)
                                     SHIP_CITY, --                      VARCHAR2(120)
                                     SHIP_STATE, --                     VARCHAR2(3)
                                     SHIP_ZIP, --                       VARCHAR2(30)
                                     SHIP_COUNTRY_ID, --                VARCHAR2(3)
                                     TRAILER_NBR, --                    VARCHAR2(12)
                                     SEAL_NBR, --                       VARCHAR2(12)
                                     TRANSSHIPMENT_NBR, --              VARCHAR2(30)
                                     COMMENTS, --                       VARCHAR2(2000)
                                     CARRIER_CODE, --                   VARCHAR2(4)
                                     CARRIER_SERVICE_CODE, --           VARCHAR2(6)
                                     MSG_STATUS, --            NOT NULL VARCHAR2(10)
                                     CREATE_TIMESTAMP, --      NOT NULL TIMESTAMP(6)
                                     LAST_UPDATE_TIMESTAMP --          TIMESTAMP(6)
                                    )
                              VALUES(L_message_id,--oms_asnout_msg_seq.nextval, --L_asnout_message.RIB_OID,
                                     L_asnout_message.SCHEDULE_NBR,
                                     L_asnout_message.AUTO_RECEIVE,
                                     L_asnout_message.TO_LOCATION,
                                     L_asnout_message.TO_LOC_TYPE,
                                     L_asnout_message.TO_STORE_TYPE,
                                     L_asnout_message.TO_STOCKHOLDING_IND, --            VARCHAR2(1)
                                     L_asnout_message.FROM_LOCATION , --                  VARCHAR2(10)
                                     L_asnout_message.FROM_LOC_TYPE, --                  VARCHAR2(1)
                                     L_asnout_message.FROM_STORE_TYPE, --                VARCHAR2(1)
                                     L_asnout_message.FROM_STOCKHOLDING_IND, --          VARCHAR2(1)
                                     L_asnout_message.ASN_NBR , --                        VARCHAR2(30)
                                     L_asnout_message.ASN_TYPE, --                       VARCHAR2(1)
                                     L_asnout_message.CONTAINER_QTY , --                  NUMBER(6)
                                     L_asnout_message.BOL_NBR, ---                        VARCHAR2(17)
                                     L_asnout_message.SHIPMENT_DATE, --                  TIMESTAMP(6)
                                     L_asnout_message.EST_ARR_DATE, --                   TIMESTAMP(6)
                                     L_asnout_message.SHIP_ADDRESS1, --                  VARCHAR2(240)
                                     L_asnout_message.SHIP_ADDRESS2, --                  VARCHAR2(240)
                                     L_asnout_message.SHIP_ADDRESS3, --                  VARCHAR2(240)
                                     L_asnout_message.SHIP_ADDRESS4, --                  VARCHAR2(240)
                                     L_asnout_message.SHIP_ADDRESS5, --                  VARCHAR2(240)
                                     L_asnout_message.SHIP_CITY, --                      VARCHAR2(120)
                                     L_asnout_message.SHIP_STATE, --                     VARCHAR2(3)
                                     L_asnout_message.SHIP_ZIP, --                       VARCHAR2(30)
                                     L_asnout_message.SHIP_COUNTRY_ID, --                VARCHAR2(3)
                                     L_asnout_message.TRAILER_NBR, --                    VARCHAR2(12)
                                     L_asnout_message.SEAL_NBR, --                       VARCHAR2(12)
                                     L_asnout_message.TRANSSHIPMENT_NBR, --              VARCHAR2(30)
                                     L_asnout_message.COMMENTS, --                       VARCHAR2(2000)
                                     L_asnout_message.CARRIER_CODE, --                   VARCHAR2(4)
                                     L_asnout_message.CARRIER_SERVICE_CODE, --           VARCHAR2(6)
                                     'S', --            NOT NULL VARCHAR2(10)
                                     SYSDATE,
                                     SYSDATE);


      FOR I IN L_asnout_message.ASNOutDistro_TBL.FIRST .. L_asnout_message.ASNOutDistro_TBL.LAST
      LOOP
         L_distro_rec := L_asnout_message.ASNOutDistro_TBL(I);
         INSERT INTO OMS_ASNOUT_DISTRO(message_id       ,
                                       asn_nbr          ,
                                       distro_nbr       ,
                                       distro_doc_type  ,
                                       cust_order_nbr   ,
                                       fulfill_order_nbr,
                                       consumer_direct  ,
                                       comments
                                      )
                                VALUES(L_message_id,
                                       L_asnout_message.ASN_NBR,
                                       L_distro_rec.distro_nbr,
                                       L_distro_rec.distro_doc_type,
                                       L_distro_rec.cust_order_nbr,
                                       L_distro_rec.fulfill_order_nbr,
                                       L_distro_rec.consumer_direct,
                                       L_distro_rec.comments
                                      );

         L_consum_direct := L_distro_rec.consumer_direct;

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 1, L_distro_rec.cust_order_nbr, null, 'OMSSUB_ASNOUT', 'RECORD INSERTED TO ASN TABLES FOR TO_STOCKHOLDING = Y', sysdate
                        FROM DUAL;        

         FOR J in L_distro_rec.ASNOutCtn_TBL.FIRST .. L_distro_rec.ASNOutCtn_TBL.LAST
         LOOP
            L_ctn_rec := L_distro_rec.ASNOutCtn_TBL(J);
            INSERT INTO OMS_ASNOUT_CTN(message_id       ,
                                       asn_nbr          ,
                                       distro_nbr       ,
                                       container_id        ,
                                       container_weight    ,
                                       container_length    ,
                                       container_width     ,
                                       container_height    ,
                                       container_cube      ,
                                       expedite_flag       ,
                                       in_store_date       ,
                                       freight_charge      ,
                                       tracking_nbr        ,
                                       master_container_id ,
                                       comments            ,
                                       weight              ,
                                       weight_uom          ,
                                       carrier_shipment_nbr,
                                       original_item_id
                                      )
                                VALUES(L_message_id,
                                       L_asnout_message.ASN_NBR,
                                       L_distro_rec.distro_nbr,
                                       L_ctn_rec.container_id        ,
                                       L_ctn_rec.container_weight    ,
                                       L_ctn_rec.container_length    ,
                                       L_ctn_rec.container_width     ,
                                       L_ctn_rec.container_height    ,
                                       L_ctn_rec.container_cube      ,
                                       L_ctn_rec.expedite_flag       ,
                                       L_ctn_rec.in_store_date       ,
                                       L_ctn_rec.freight_charge      ,
                                       L_ctn_rec.tracking_nbr        ,
                                       L_ctn_rec.master_container_id ,
                                       L_ctn_rec.comments            ,
                                       L_ctn_rec.weight              ,
                                       L_ctn_rec.weight_uom          ,
                                       L_ctn_rec.carrier_shipment_nbr,
                                       L_ctn_rec.original_item_id
                                      );

            FOR K in L_ctn_rec.ASNOutItem_TBL.FIRST .. L_ctn_rec.ASNOutItem_TBL.LAST
            LOOP
               L_item_record := L_ctn_rec.ASNOutItem_TBL(K);
               INSERT INTO OMS_ASNOUT_ITEM(message_id,
                                           asn_nbr          ,
                                           distro_nbr       ,
                                           container_id     ,
                                           item_id                ,
                                           unit_qty               ,
                                           gross_cost             ,
                                           priority_level         ,
                                           order_line_nbr         ,
                                           lot_nbr                ,
                                           final_location         ,
                                           from_disposition       ,
                                           to_disposition         ,
                                           voucher_number         ,
                                           voucher_expiration_date,
                                           container_qty          ,
                                           comments               ,
                                           unit_cost              ,
                                           base_cost              ,
                                           weight                 ,
                                           weight_uom
                                          )
                                    VALUES(L_message_id,
                                           L_asnout_message.ASN_NBR ,
                                           L_distro_rec.distro_nbr  ,
                                           L_ctn_rec.container_id,
                                           L_item_record.item_id                ,
                                           L_item_record.unit_qty               ,
                                           L_item_record.gross_cost             ,
                                           L_item_record.priority_level         ,
                                           L_item_record.order_line_nbr         ,
                                           L_item_record.lot_nbr                ,
                                           L_item_record.final_location         ,
                                           L_item_record.from_disposition       ,
                                           L_item_record.to_disposition         ,
                                           L_item_record.voucher_number         ,
                                           L_item_record.voucher_expiration_date,
                                           L_item_record.container_qty          ,
                                           L_item_record.comments               ,
                                           L_item_record.unit_cost              ,
                                           L_item_record.base_cost              ,
                                           L_item_record.weight                 ,
                                           L_item_record.weight_uom
                                          );

            END LOOP;
         END LOOP;
      END LOOP;


         OPEN C_OMS_CUS_ORD_NO(L_message_id);
         LOOP
            FETCH C_OMS_CUS_ORD_NO into L_oms_cust_ord_no;
            L_Counter := 0;

            EXIT WHEN C_OMS_CUS_ORD_NO%NOTFOUND;
            OPEN C_FULFILL_DETAIL(L_message_id,L_oms_cust_ord_no);
            LOOP
               FETCH C_FULFILL_DETAIL INTO L_oms_cust_ord_no,
                                           L_item,
                                           L_line_no,
                                           L_quantity,
                                           L_del_conf_id,
                                           L_consum_direct,
                                           L_bol_no,
                                           L_fulfil_no,
                                           L_req_qty,
                                           L_CONF_QTY,
                                           L_DELIVER_QTY,
                                           L_CANCEL_QTY,
                                           L_FROM_loc,
                                           L_FROM_loc_type,
                                           L_TO_loc,
                                           L_Event_id,
                                           L_Event_comm;
               EXIT WHEN C_FULFILL_DETAIL%NOTFOUND;

                     -- Counter for inserting LOG table only once
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
                                                     NULL);

                     END IF;

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 2, L_distro_rec.cust_order_nbr, null, 'OMSSUB_ASNOUT', 'RECORD INSERTED TO OMS_CUST_ORD_LOG FOR TO_STOCKHOLDING = Y', sysdate
                        FROM DUAL;        

                    -- Insert into log item table

                     INSERT INTO OMS_CUST_ORD_LOG_ITEM(ITEM,
                                                       LINE_NO,
                                                       LOG_SEQ_NO,
                                                       QTY,
                                                       FULFILL_ORDER_NO,
                                                       CREATE_DATETIME)
                                                VALUES(L_item,
                                                       L_line_no,
                                                       L_seq_no,
                                                       L_quantity,
                                                       L_fulfil_no,
                                                       SYSDATE);
            END LOOP;
            CLOSE C_FULFILL_DETAIL;
         END LOOP;
         CLOSE C_OMS_CUS_ORD_NO;

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 3, L_distro_rec.cust_order_nbr, null, 'OMSSUB_ASNOUT', 'CALLING SIEBEL WEB SERVICE FOR TO_STOCKHOLDING = Y', sysdate
                        FROM DUAL;        

      -- Calling Seibel web service
         L_seibel_out:=OMS_ASNOUT_WS_INVOKER.f_ASNOUT_Siebel_COStatusUpdate(L_message_id);

                        INSERT INTO CUST_ORDER_TRACKING (SEQ_NO, CUST_ORDER_NO, OMS_CUST_ORD_NO, PACKAGE_NAME, EVENT_NAME, CREATE_TIMESTAMP)
                        SELECT 4, L_distro_rec.cust_order_nbr, null, 'OMSSUB_ASNOUT', ' SIEBEL WEB SERVICE SUCESSFUL', sysdate
                        FROM DUAL;        

   IF SYSTEM_OPTIONS_SQL.GET_SYSTEM_OPTIONS(O_error_message,
                                            L_system_options_row) = FALSE THEN
      RETURN FALSE;
   END IF;
   END IF;
   RETURN TRUE;

EXCEPTION
   WHEN OTHERS THEN
      O_error_message := SQL_LIB.CREATE_MSG('PACKAGE_ERROR',
                                            SQLERRM,
                                            L_program,
                                            to_char(SQLCODE));
      RETURN FALSE;
END CONSUME_SHIPMENT_CORE;

----------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------
PROCEDURE HANDLE_ERRORS(O_status_code     IN OUT  VARCHAR2,
                        IO_error_message  IN OUT  VARCHAR2,
                        I_cause           IN      VARCHAR2,
                        I_program         IN      VARCHAR2)
IS

   L_program  VARCHAR2(100)  := 'OMSSUB_ASNOUT.HANDLE_ERRORS';


BEGIN
   API_LIBRARY.HANDLE_ERRORS(O_status_code,
                             IO_error_message,
                             I_cause,
                             I_program);
EXCEPTION
   when OTHERS then
      IO_error_message := SQL_LIB.CREATE_MSG('PACKAGE_ERROR',

                                             SQLERRM,
                                             L_program,
                                             To_Char(SQLCODE));
      ---
      API_LIBRARY.HANDLE_ERRORS(O_status_code,
                                IO_error_message,
                                API_LIBRARY.FATAL_ERROR,
                                L_program);
END HANDLE_ERRORS;
-----------------------------------------------------------------------------------------

END OMSSUB_ASNOUT;