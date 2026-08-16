create or replace PACKAGE XX_DLV_ADDRESS_UPDATE AS 

P_Sucesss_Resp              VARCHAR2(255)     := 'Success';
P_Failure_Resp               VARCHAR2(255)     := 'Failure';

FUNCTION VALIDATE_UPDATE_REQUEST (P_Orderno    In Oms_Cust_Ord_Head.Cust_Order_No%Type,
                                                                                   P_OmsCustOrdNo    In  Oms_Cust_Ord_Head.Oms_Cust_Ord_No%Type ,
                                                                                   P_Classification      In Oms_Cust_Ord_Item.Ship_Classification%Type, 
                                                                                   P_First_Name In Oms_Cust_Ord_Address.Deliver_First_Name%Type,
                                                                                   P_Last_Name  In Oms_Cust_Ord_Address.Deliver_Last_Name%Type,
                                                                                   P_Address In Oms_Cust_Ord_Address.Deliver_Add_1%Type,
                                                                                   P_Mobile In Oms_Cust_Ord_Address.Deliver_Phone_No%Type,
                                                                                   P_Response In Out Varchar2,
                                                                                   P_Response_Error In Out Rtk_Errors.Rtk_Text%Type)              
RETURN VARCHAR2;

END XX_DLV_ADDRESS_UPDATE;