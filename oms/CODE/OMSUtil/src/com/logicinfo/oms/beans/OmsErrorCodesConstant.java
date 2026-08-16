package com.logicinfo.oms.beans;

public interface OmsErrorCodesConstant {

    public static String errorUnHandledExceptionCode = "ERR_UNHANDLED_EXCEPTION";
    
    public String customerOrderNotExist="CO_NOT_EXISTS";
    
    public String paymentRejected="PAYMNT_REJECTED";
    
    public String paymentAlreadyConfirmed="PAYMNT_ALDRY_CONFD";
    
    public String paymentCannotConfirm="PAYMENT_CANT_CONFIRM";
    
    public String SubCustomerReqid="SUB_ORD_REQ";
    
    public String customerAddressReqid="CUST_ADD_REQ";
    
    public String pickUpLocationReqid="PICK_LOC_REQ";
    
    public String invalidTenderCode="INVALID_TENDER";
    
    public String tenderRefernceReqid="TEND_REF_REQ";
    
    public String creditCardNoReqId="CC_NO_REQ";
    
    public String CreditCardAuthorizartionNo="CC_AUTH_NO";
    
    public String creditCardAuthorizationSourceNo="CC_AUTH_SRC";
    
    public String creditCardHolderRequired="CC_HOLDER_REQ";
    
    public String CreditCardEntryReqId="CC_ENTRY_REQ";
    
    public String CreditCardExpiryDateReqId="CC_EXP_REQ";
    
    public String CreditCardSpecReqid ="CC_SPEC_REQ";
    
    public String CreditCardTermsReqId="CC_TERM_REQ";
    
    public String duplicateCustomerOrderNo="DUP_CUST_ORD";
    
    public String itemAlredayDelieverd="ITEM_ALRDY_DLVD";
    
    public String itemAlreadyCancelled="ITEM_ALRDY_CLD";
    
    public String requestedQtyOfanItemForCancellationNotAvailable="ITEM_ALRDY_DLVD_CLD";
    
    public String itemAvailableForCancellation="ITEM_QTY_UNAVB";
    
    public String linkedItem="LINKED_ITEM";
    
    public String CancelledIdExist="CANCEL_ID_EXIST";
    
    public String applicationDown_Sim_RMS="SIM/RMS_IS_DOWN";
    
    public String Cannot_Cancel_ShippingCharge_item="CANT_CANCL_SHIP_CHRG";
    
    public String Cannot_Cancel_Delievry_In_Progress="CANT_CANCEL";
    
    public String Cancel_Invalid_Item="INVALID_CANCEL_ITEM";
    
    public String Rma_Already_Exist="RMA_EXIST";
    
    public String phyiscal_WareHouse_Unavailable="PHY_WH_UNAVAILABLE";
    
    public String Customer_Order_Exist="INVALID_CO_INPUT";
    
    public String Invalid_Rma_ReturnDate="INVALID_RMA_RTN_DATE";
    
    public String Invalid_Rma_item="INVALID_RMA_ITEM";
    
    public String Invalid_Return_qty="INVALID_RTN_QTY";
    
    public String Item_already_Returned="ITEM_ALDY_RETUNED";
    
    public String Invalid_Rma_Id="INVALID_RMA_ID";
    
    public String  Invalid_Rma_mod_id="INVALID_RMA_MOD_ID";
    
    public String  Rma_mod_greater_qty ="RMA_MOD_GT_QTY";
    
    public String  Rma_mod_lesser_qty="RMA_MOD_LT_QTY";
    
    public String baseLanguageCodeValue ="1";
    
}
