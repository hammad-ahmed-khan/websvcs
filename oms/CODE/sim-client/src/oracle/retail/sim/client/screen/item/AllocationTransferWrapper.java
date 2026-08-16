package oracle.retail.sim.client.screen.item;

import java.util.Date;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.item.AllocationVO;
import oracle.retail.sim.common.source.Finisher;
import oracle.retail.sim.common.transfer.TransferAllocationVO;

/*******************************************************************************
 * Allocation/Transfer Wrapper for the item detail screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************/

public class AllocationTransferWrapper {
    private static String FINISHER = Translator.getText("Finisher");
    private static String STORE = Translator.getText("Store");
    private static String WAREHOUSE = Translator.getText("Warehouse");

    private Date deliveryDate;
    private String name;
    private String location;
    private String unitOfMeasure;
    private Quantity quantity;
    private String deliveryTimeSlotDesc;

    public AllocationTransferWrapper(AllocationVO allocation, String uom) {
        name = allocation.getName();
        deliveryDate = allocation.getDeliveryDate();
        if (allocation.getSource() instanceof Finisher) {
            location = FINISHER;
        } else {
            location = WAREHOUSE;
        }
        quantity = allocation.getQuantity();
        unitOfMeasure = uom;
        deliveryTimeSlotDesc = allocation.getDeliverySlotDescription();
    }

    public AllocationTransferWrapper(TransferAllocationVO transferLineItem, String uom) {
        name = transferLineItem.getSendingStoreFullName();
        deliveryDate = transferLineItem.getCreateDate();
        location = STORE;
        quantity = transferLineItem.getQuantity();
        unitOfMeasure = uom;
        deliveryTimeSlotDesc = transferLineItem.getDeliverySlotDescription();
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public String getDeliverySlotDescription() {
        return deliveryTimeSlotDesc;
    }
}
