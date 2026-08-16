package oracle.retail.sim.client.screen.fulfillmentorderpick;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderBin;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickProperty;

/********************************************************************************************************
 * Fulfillment Order Bin Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderBinWrapper extends Wrapper {

    private FulfillmentOrderBin bin;
    private boolean isEditable = false;

    /**
     * Constructs a new CustomerOrderBinWrapper around the input FulfillmentOrderBin
     * @param bin The FulfillmentOrderBin around which to construct a new CustomerOrderBinWrapper
     */
    public FulfillmentOrderBinWrapper(FulfillmentOrderBin bin, boolean isEditable) {
        this.bin = bin;
        this.isEditable = isEditable;
    }

    /**
     * Returns the FulfillmentOrderBin around which this CustomerOrderBinWrapper is constructed.
     * @return The FulfillmentOrderBin around which this CustomerOrderBinWrapper is constructed.
     */
    public FulfillmentOrderBin getBin() {
        return bin;
    }

    /**
     * Returns the system or user defined Bin Id.
     * @return The system or user defined Bin Id.
     */
    public String getBinId() {
        return bin.getBinId();
    }

    /**
     * Sets the user defined Bin Id
     * @param The user defined Bin Id to set to the Fulfillment Order Bin
     */
    public void setBinId(String binId) throws BusinessException {
        bin.setBinId(binId);
    }

    /**
     * Returns the ID of the Fulfillment Order attached to the Fulfillment Order Bin.
     * @return The ID of the Fulfillment Order attached to the Fulfillment Order Bin.
     */
    public Long getFulfillmentOrderId() {
        return bin.getFulfillmentOrderId();
    }

    public boolean isPropertyModifiable(String property) {
        if (FulfillmentOrderPickProperty.BIN_ID.equals(property)) {
            return isEditable;
        }
        return false;
    }
}
