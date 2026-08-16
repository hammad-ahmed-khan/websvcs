create or replace PACKAGE BODY XX_DLV_ADDRESS_UPDATE AS

  FUNCTION VALIDATE_UPDATE_REQUEST (P_Orderno    In Oms_Cust_Ord_Head.Cust_Order_No%Type,
                                                                                     P_OmsCustOrdNo    In  Oms_Cust_Ord_Head.Oms_Cust_Ord_No%Type ,
                                                                                     P_Classification      In Oms_Cust_Ord_Item.Ship_Classification%Type, 
                                                                                     P_First_Name In Oms_Cust_Ord_Address.Deliver_First_Name%Type,
                                                                                     P_Last_Name  In Oms_Cust_Ord_Address.Deliver_Last_Name%Type,
                                                                                     P_Address In Oms_Cust_Ord_Address.Deliver_Add_1%Type,
                                                                                     P_Mobile In Oms_Cust_Ord_Address.Deliver_Phone_No%Type,
                                                                                     P_Response In Out Varchar2,
                                                                                     P_Response_Error In Out Rtk_Errors.Rtk_Text%Type)              
RETURN VARCHAR2 AS

    v_Orderno  Oms_Cust_Ord_Head.Cust_Order_No%Type;
    v_OmsCustOrdNo   Oms_Cust_Ord_Head.Oms_Cust_Ord_No%Type;
    v_Classification  Oms_Cust_Ord_Item.Ship_Classification%Type;
    v_First_Name  Oms_Cust_Ord_Address.Deliver_First_Name%Type;
    v_Last_Name Oms_Cust_Ord_Address.Deliver_Last_Name%Type;
    v_Address Oms_Cust_Ord_Address.Deliver_Add_1%Type;
    v_Mobile Oms_Cust_Ord_Address.Deliver_Phone_No%Type;
    l_book_date date;
    l_dlv_qty number;
    l_pick_qty number;
    l_dlv_status_check number;
    l_wh_status_check number;
    l_book_status_check number;
    l_sim_status_check number;
    l_no_fulfillment_check number;
    v_Response Varchar2 (30);
    v_Response_Error Rtk_Errors.Rtk_Text%Type;

--- Cursor to check if fulfulment exists for the Order
        CURSOR C_CHECK_NO_FULFULLMENT is
        select count(*) from oms_co_fulfill_detail where oms_cust_ord_no = v_OmsCustOrdNo;

-- Cursor to Check if any quantity has been delivered for that order for any item
        CURSOR C_CHECK_DLV_QTY is
             select 1 from oms_cust_ord_item th
             where oms_cust_ord_no = v_omscustordno
             GROUP BY oms_cust_ord_no
             HAVING SUM(nvl(CUM_QTY_DELIVERED,0)) > 0;

-- Cursor to Check if the items are picked in WMS - Selected / Distributed quantity in the Transfers
        CURSOR C_CHECK_WH is
             select 1 from tsfdetail td, tsfhead th
             where td.tsf_no = th.tsf_no
             and th.TSF_TYPE='CO' and th.EXT_REF_NO = v_Orderno
             AND EXISTS (SELECT 1 FROM OMS_CO_FULFILL_DETAIL OD where SOURCE_LOC_TYPE = 'WH' and FULFILL_LOC_TYPE = 'V' AND
                                      OD.OMS_CUST_ORD_NO = v_omscustordno
                                      and ((source_loc_type = 'WH' and fulfill_loc_type = 'V') or (source_loc_type = 'ST' and fulfill_loc_type = 'S' and tsf_no is null)))
             GROUP BY th.EXT_REF_NO
             HAVING SUM(nvl(DISTRO_QTY,0) + nvl(SELECTED_QTY,0)) > 0;

-- Cursor to Check if the Items are Picked in SIM    
        CURSOR C_CHECK_SIM is
             select  1
               from ful_ord@simdb fo, ful_ord_line_item@simdb fi
                where fo.id = fi.ful_ord_id
                and cust_order_id = v_Orderno
                group by cust_order_id
                having SUM(QUANTITY_PICKED)> 0;    

-- Cursor to check if Valid booking exists for an Order    
        CURSOR C_CHECK_BOOK_SYSTEM is
                select MIN (REQUEST_DATE) from 
                    XX_DLVRY_BOOKING H, XX_DLVRY_BOOKING_LINES L
                    where h.book_id = l.book_id
                    and h.BOOK_STATUS = 'Confirmed'
                    and l.ORDER_NUMBER = v_Orderno;            

  BEGIN

    v_Orderno                := p_Orderno;
    v_OmsCustOrdNo   := p_OmsCustOrdNo;
    v_Classification        := p_Classification;
    v_First_Name           := p_First_Name;
    v_Last_Name            := p_Last_Name;
    v_Address                 := p_Address;
    v_Mobile                   := p_Mobile;

    l_dlv_qty := 0;
    l_dlv_status_check := 0;
    l_wh_status_check := 0;
    l_book_status_check := 0;
    l_sim_status_check := 0;
    l_no_fulfillment_check := 0;


insert into XX_DLV_ADDRESS_UPDATE_LOG
select v_Orderno,v_OmsCustOrdNo, v_Classification, v_First_Name, v_Last_Name, v_Address, v_Mobile, systimestamp from dual;
commit;

--- Step#1
--- Check if No Fulfillment Exists for the Order Yet
--- If there is no Fulfullment, only update OMS_CUST_ORD_ADDRESS table
        OPEN C_CHECK_NO_FULFULLMENT;
        FETCH C_CHECK_NO_FULFULLMENT INTO l_no_fulfillment_check;
            IF l_no_fulfillment_check = 0 THEN

            update oms_cust_ord_address
            set
                    Deliver_First_Name = NVL(v_First_Name, Deliver_First_Name),
                    Deliver_Last_Name = NVL(v_Last_Name, Deliver_Last_Name),
                    Deliver_Add_1          = NVL(v_Address, Deliver_Add_1),
                    Deliver_Phone_No  = NVL(v_Mobile, Deliver_Phone_No)
                where oms_cust_ord_no = v_OmsCustOrdNo;
            commit;

              P_Response := P_Sucesss_Resp;
              P_Response_Error := 'Address Updated Successfully';
              return P_Response;
            END IF;
        CLOSE C_CHECK_NO_FULFULLMENT; 	
--------------------------------------- Check No Fulfillment End -----------------------------------

--- Step#2
--- Check Delivery Qty Of The Order
--- If there is any Deliver Qty, return the Error
        OPEN C_CHECK_DLV_QTY;
        FETCH C_CHECK_DLV_QTY INTO l_dlv_qty;
        IF C_CHECK_DLV_QTY%NOTFOUND THEN
              null;
        ELSE 
             P_Response := P_Failure_Resp;
             P_Response_Error := 'Item already delivered';
             return P_Response;
        END IF;    
        CLOSE C_CHECK_DLV_QTY; 	

-------------------------------------- CHECK CLASSIFICATIONS --------------------------------------
        IF v_Classification = 'SMALL' THEN

                        DBMS_OUTPUT.PUT_LINE ('SMALL');

        FOR i in (select distinct source_loc_type, fulfill_loc_type from oms_co_fulfill_detail od where od.oms_cust_ord_no = v_omscustordno
                        and ((source_loc_type = 'WH' and fulfill_loc_type = 'V') or (source_loc_type = 'ST' and fulfill_loc_type = 'S' and tsf_no is null))) LOOP

                IF i.SOURCE_LOC_TYPE = 'WH' AND i.FULFILL_LOC_TYPE = 'V' THEN 

                        --- Step#1 in SMALL
                        --- Check Pick Quantity in Transfer Tables for Warehouse
                                OPEN C_CHECK_WH;
                                FETCH C_CHECK_WH INTO l_dlv_qty;
                                IF C_CHECK_WH%NOTFOUND THEN
                                      l_wh_status_check := 1;
                                ELSE 
                                     P_Response := P_Failure_Resp;
                                     P_Response_Error := 'Item is already picked in the Warehouse';
                                     return P_Response;
                                END IF;    
                                CLOSE C_CHECK_WH; 	        

                ELSIF i.SOURCE_LOC_TYPE = 'ST' AND i.FULFILL_LOC_TYPE = 'S' THEN


                        --- Step#2 in SMALL
                        --- Check Pick Quantity in SIM tables
                                OPEN C_CHECK_SIM;
                                FETCH C_CHECK_SIM INTO l_pick_qty;
                                IF C_CHECK_SIM%NOTFOUND THEN
                                      l_sim_status_check := 1;
                                ELSE 
                                     P_Response := P_Failure_Resp;
                                     P_Response_Error := 'Item is already picked in SIM';
                                     return P_Response;
                                END IF;    
                                CLOSE C_CHECK_SIM; 	      

                END IF; ---- SMALL COMBINATIONS IF
        END LOOP; -- BIG COMBINATIONS LOOP

                        update oms_cust_ord_address
                        set
                            Deliver_First_Name = NVL(v_First_Name, Deliver_First_Name),
                            Deliver_Last_Name = NVL(v_Last_Name, Deliver_Last_Name),
                            Deliver_Add_1          = NVL(v_Address, Deliver_Add_1),
                            Deliver_Phone_No  = NVL(v_Mobile, Deliver_Phone_No)
                        where oms_cust_ord_no = v_OmsCustOrdNo;

                        update ordcust
                        set
                            DELIVER_FIRST_NAME = NVL(v_First_Name, DELIVER_FIRST_NAME),
                            DELIVER_LAST_NAME  = NVL(v_Last_Name, DELIVER_LAST_NAME),
                            DELIVER_ADD1             = NVL(v_Address, DELIVER_ADD1),
                            DELIVER_PHONE          = NVL(v_Mobile, DELIVER_PHONE)
                        where CUSTOMER_ORDER_NO = v_Orderno
                        and l_wh_status_check = 1;                   

--                        update address@simdb b
--                        set
--                            FIRST_NAME                      = NVL(v_First_Name, FIRST_NAME),
--                            LAST_NAME                       = NVL(v_Last_Name, LAST_NAME),
--                            ADDRESS_LINE_1              = NVL(v_Address, ADDRESS_LINE_1),
--                            CONTACT_PHONE            = NVL(v_Mobile, CONTACT_PHONE)
--                        where
--                                  ENTITY_TYPE = 'FO' and TYPE = '08'
--                        AND  ENTITY_ID IN (SELECT ID FROM FUL_ORD@simdb a where a.CUST_ORDER_ID = v_Orderno)
--                        AND l_sim_status_check = 1;

                        commit;

              P_Response := P_Sucesss_Resp;
              P_Response_Error := 'Address Updated Successfully';        

        ELSIF v_Classification IN ('MIXED', 'BIG') THEN

                        --- Step#1 in BIG/MIXED
                        --- Check Booking Status
                                OPEN C_CHECK_BOOK_SYSTEM;
                                FETCH C_CHECK_BOOK_SYSTEM INTO l_book_date;
                                IF C_CHECK_BOOK_SYSTEM%NOTFOUND THEN
                                      l_book_status_check := 0;
                                ELSIF l_book_date > sysdate THEN
                                     l_book_status_check := 1;
                                ELSIF l_book_date <= sysdate  THEN
                                      P_Response_Error := 'Booking is less or equal to Current Date ';
                                      P_Response := P_Failure_Resp;
                                     return P_Response;
                                END IF;    
                                CLOSE C_CHECK_BOOK_SYSTEM;     

        FOR i in (select distinct source_loc_type, fulfill_loc_type from oms_co_fulfill_detail od where od.oms_cust_ord_no = v_omscustordno
                        and ((source_loc_type = 'WH' and fulfill_loc_type = 'V') or (source_loc_type = 'ST' and fulfill_loc_type = 'S' and tsf_no is null))) LOOP        

                IF i.SOURCE_LOC_TYPE = 'WH' AND i.FULFILL_LOC_TYPE = 'V' THEN 

                        --- Step#1 in BIG
                        --- Check Pick Quantity in Transfer Tables for Warehouse
                                OPEN C_CHECK_WH;
                                FETCH C_CHECK_WH INTO l_dlv_qty;
                                IF C_CHECK_WH%NOTFOUND THEN
                                      l_wh_status_check := 1;
                                ELSE 
                                     P_Response := P_Failure_Resp;
                                     P_Response_Error := 'Item is already picked in the Warehouse';
                                     return P_Response;
                                END IF;    
                                CLOSE C_CHECK_WH; 	        

                ELSIF i.SOURCE_LOC_TYPE = 'ST' AND i.FULFILL_LOC_TYPE = 'S' THEN

                        --- Step#2 in BIG
                        --- Check Pick Quantity in SIM tables
                                OPEN C_CHECK_SIM;
                                FETCH C_CHECK_SIM INTO l_dlv_qty;
                                IF C_CHECK_SIM%NOTFOUND THEN
                                      l_sim_status_check := 1;
                                ELSE 
                                     P_Response := P_Failure_Resp;
                                     P_Response_Error := 'Item is already picked in SIM';
                                     return P_Response;
                                END IF;    
                                CLOSE C_CHECK_SIM; 	      

                END IF; ---- BIG/MIXED COMBINATIONS IF

        END LOOP; -- BIG COMBINATIONS LOOP

                        update oms_cust_ord_address
                        set
                            deliver_first_name = nvl(v_first_name, deliver_first_name),
                            deliver_last_name = nvl(v_last_name, deliver_last_name),
                            deliver_add_1          = nvl(v_address, deliver_add_1),
                            deliver_phone_no  = nvl(v_mobile, deliver_phone_no)
                        where oms_cust_ord_no = v_OmsCustOrdNo;

                        update ordcust
                        set
                            DELIVER_FIRST_NAME = NVL(v_First_Name, DELIVER_FIRST_NAME),
                            DELIVER_LAST_NAME  = NVL(v_Last_Name, DELIVER_LAST_NAME),
                            DELIVER_ADD1             = NVL(v_Address, DELIVER_ADD1),
                            DELIVER_PHONE          = NVL(v_Mobile, DELIVER_PHONE)
                        where CUSTOMER_ORDER_NO = v_Orderno
                        and l_wh_status_check = 1;                   

--                        update address@simdb b
--                        set
--                            FIRST_NAME = NVL(v_First_Name, FIRST_NAME),
--                            LAST_NAME  = NVL(v_Last_Name, LAST_NAME),
--                            ADDRESS_LINE_1             = NVL(v_Address, ADDRESS_LINE_1),
--                            CONTACT_PHONE          = NVL(v_Mobile, CONTACT_PHONE)
--                        where
--                                  ENTITY_TYPE = 'FO' and TYPE = '08'
--                        AND  ENTITY_ID IN (SELECT ID FROM FUL_ORD@simdb a where a.CUST_ORDER_ID = v_Orderno)
--                        AND l_sim_status_check = 1;

                        update XX_DLVRY_BOOKING
                        set
                            CUSTOMER_NAME                  = NVL(v_First_Name || ' ' || v_Last_Name, CUSTOMER_NAME),
                            CUSTOMER_ADDRESS             = NVL(v_Address, CUSTOMER_ADDRESS),
                            MOBILE                                      = NVL(v_Mobile, MOBILE)
                        where BOOK_ID IN (SELECT BOOK_ID FROM XX_DLVRY_BOOKING_LINES WHERE ORDER_NUMBER= v_Orderno)
                        and l_book_status_check = 1;  
                        commit;

        END IF; ---- CLASSIFICATION IF BLOCK

              P_Response := P_Sucesss_Resp;
              P_Response_Error := 'Address Updated Successfully';

    RETURN P_Response;

EXCEPTION 
   WHEN others THEN 
               ROLLBACK;
               P_Response := P_Failure_Resp;
               P_Response_Error := 'Cannot update delivery address - ' || SQLERRM;
                return P_Response;
  END VALIDATE_UPDATE_REQUEST;

END XX_DLV_ADDRESS_UPDATE;