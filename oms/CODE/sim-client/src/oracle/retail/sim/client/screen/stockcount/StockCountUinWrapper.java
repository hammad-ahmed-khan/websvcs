package oracle.retail.sim.client.screen.stockcount;

import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.stockcount.StockCountSerialNumber;
import oracle.retail.sim.common.uin.UINType;

/********************************************************************************************************
 * UIN Stock Count Wrapper. Wraps a single serial number for a stock count line item.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountUinWrapper extends Wrapper implements StockCountUinInterface {

    private String itemId;
    private FunctionalArea area;
    private UINType type;
    private String label;
    private StockCountSerialNumber serialNumber;

    public void setSerialNumber(StockCountSerialNumber serialNumber) {
        this.serialNumber = serialNumber;
    }

    public StockCountSerialNumber getSerialNumber() {
        return serialNumber;
    }

    public void setFunctionalArea(FunctionalArea area) {
        this.area = area;
    }

    public FunctionalArea getFunctionalArea() {
        return area;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setType(UINType type) {
        this.type = type;
    }

    public UINType getType() {
        return type;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Determines if the given property is modifiable. If this returns false, then this property is not modifiable.
     */
    public boolean isPropertyModifiable(String property) throws Exception {
        return serialNumber == null;
    }
}
