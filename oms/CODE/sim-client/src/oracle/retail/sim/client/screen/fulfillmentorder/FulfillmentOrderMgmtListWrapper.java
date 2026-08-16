package oracle.retail.sim.client.screen.fulfillmentorder;

import java.util.Date;

import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtListVO;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderTranType;

/********************************************************************************************************
 * Fulfillment Order Management List Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderMgmtListWrapper extends Wrapper {

    private FulfillmentOrderMgmtListVO fulfillmentOrderMgmtListVO;

    /**
     * Parameterized constructor to initialize data member of the class with actual data containing object.
     * @param Value Object containing actual data to be displayed.
     */
    public FulfillmentOrderMgmtListWrapper(FulfillmentOrderMgmtListVO vo) {
        fulfillmentOrderMgmtListVO = vo;
    }

    /**
     * @return Returns value object of type FulfillmentOrderMgmtListVO
     */
    public FulfillmentOrderMgmtListVO getFulfillmentOrderMgmtListVO() {
        return fulfillmentOrderMgmtListVO;
    }

    /**
     * @return Returns Transaction_Id field of the value object as it is to be displayed on the panel.
     */
    public String getTranId() {
        return fulfillmentOrderMgmtListVO.getTranId();
    }

    /**
     * @return Returns Transaction_Type field of the value object as it is to be displayed on the panel.
     */
    public FulfillmentOrderTranType getTranType() {
        return fulfillmentOrderMgmtListVO.getTranType();
    }

    /**
     * @return Returns Customer_Order_Id field of the value object as it is to be displayed on the panel.
     */
    public String getCustomerOrderId() {
        return fulfillmentOrderMgmtListVO.getCustomerOrderId();
    }

    /**
     * @return Returns From_Location_Number field concatenated with From_Location_Name field of the value object as it is to be displayed on the panel.
     */
    public String getFromLocation() {
        String fromLocation = fulfillmentOrderMgmtListVO.getFromLocation();
        String fromLocationName = fulfillmentOrderMgmtListVO.getFromLocationName();

        if (fromLocation == null) {
            fromLocation = "";
        }
        if (fromLocationName == null) {
            fromLocationName = "";
        }
        return fromLocation + " - " + fromLocationName;
    }

    /**
     * @return Returns To_Location_Number field concatenated with To_Location_Name field of the value object as it is to be displayed on the panel.
     */
    public String getToLocation() {
        String toLocation = fulfillmentOrderMgmtListVO.getToLocation();
        String toLocationName = fulfillmentOrderMgmtListVO.getToLocationName();
        
        if (toLocation == null) {
            toLocation = "";
        }
        if (toLocationName == null) {
            toLocationName = "";
        }
        return toLocation + " - " + toLocationName;
    }

    /**
     * @return Returns Transaction_Date field of the value object as it is to be displayed on the panel.
     */
    public Date getTranDate() {
        return fulfillmentOrderMgmtListVO.getCreateDate();
    }

    /**
     * @return Returns Transaction_Status field of the value object as it is to be displayed on the panel.
     */
    public String getTranStatus() {
        return fulfillmentOrderMgmtListVO.getTranStatus();
    }

    /**
     * @return Returns Total_SKUs field of the value object as it is to be displayed on the panel.
     */
    public Long getLineItemsCount() {
        return fulfillmentOrderMgmtListVO.getTotalSKUs();
    }

    /**
     * @return Returns User field of the value object as it is to be displayed on the panel.
     */
    public String getUser() {
        return fulfillmentOrderMgmtListVO.getUser();
    }

}
