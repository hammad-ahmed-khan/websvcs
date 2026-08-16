package oracle.retail.sim.client.screen.fulfillmentorderreversepick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.uom.UomUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePick;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickLineItem;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickStatus;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Fulfillment Order Reverse Pick Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderReversePickDetailModel extends SimScreenModel {

    private FulfillmentOrderReversePick reversePick;
    private FulfillmentOrder fulfillmentOrder;
    private Map<Long, FulfillmentOrderLineItem> orderLineItemMap = new HashMap<Long, FulfillmentOrderLineItem>();
    private boolean viewOnlyMode = false;

    public void loadFulfillmentOrder() throws Exception {
        fulfillmentOrder = (FulfillmentOrder) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER);
        for (FulfillmentOrderLineItem orderLineItem : fulfillmentOrder.getLineItems()) {
            orderLineItemMap.put(orderLineItem.getId(), orderLineItem);
        }
    }

    public FulfillmentOrder getFulfillmentOrder() {
        return fulfillmentOrder;
    }

    public void loadReversePick() throws Exception {
        reversePick = (FulfillmentOrderReversePick) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER_REVERSE_PICK);
        viewOnlyMode = !isReversePickEditAllowed();
        if (viewOnlyMode) {
            return;
        }
        if (obtainLock()) {
            return;
        }
        viewOnlyMode = true;
    }

    public FulfillmentOrderReversePick getReversePick() {
        return reversePick;
    }

    public boolean isScannerAvailable() {
        return !viewOnlyMode;
    }

    public boolean isNotesEditable() {
        FulfillmentOrderStatus status = fulfillmentOrder.getStatus();
        return status == FulfillmentOrderStatus.NEW || status == FulfillmentOrderStatus.IN_PROGRESS;
    }

    public void updateExistingLineItem(FulfillmentOrderReversePickLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        if (barcodeItem.getQuantity().isPositive()) {
            if (wrapper.isCasesMode() && wrapper.isEachesStandardUnitOfMeasure()) {
                wrapper.setQuantity(wrapper.getQuantityOrZero().add(barcodeItem.getQuantity().multiply(wrapper.getCaseSize())));
            } else {
                wrapper.setQuantity(wrapper.getQuantityOrZero().add(barcodeItem.getQuantity()));
            }
            return;
        }
        throw new UIException(CommonMessageText.NO_QUANTITY_APPLIED, RErrorSeverity.WARNING);
    }

    /**
     * Returns whether the current Reverse Pick is in canceled or completed status.
     * @return True if the current Reverse Pick is in canceled or completed status, otherwise false.
     */
    public boolean isReversePickClosed() {
        return FulfillmentOrderReversePickStatus.getClosedSet().contains(reversePick.getStatus());
    }

    public boolean obtainLock() throws Exception {
        if (reversePick.isNew()) {
            return true;
        }
        return obtainLock(ActivityLockType.FULFILLMENT_ORDER_REVERSE_PICK, reversePick.getId().toString());
    }

    public List<FulfillmentOrderReversePickLineItemWrapper> getReversePickItems() throws Exception {
        List<FulfillmentOrderReversePickLineItemWrapper> wrappers = new ArrayList<>();
        if (reversePick.getStatus() != FulfillmentOrderReversePickStatus.NEW) {
            for (FulfillmentOrderReversePickLineItem reversePickLineItem : reversePick.getLineItems()) {
                FulfillmentOrderLineItem orderLineItem = orderLineItemMap.get(reversePickLineItem.getFulfillmentOrderLineItemId());
                FulfillmentOrderReversePickLineItemWrapper wrapper = createLineItemWrapper(reversePick, reversePickLineItem);
                wrapper.setFulfillmentOrder(fulfillmentOrder);
                wrapper.setFulfillmentOrderLineItem(orderLineItem);
                wrapper.setPreferredUomConversionFactor(UomUtility.getStandardUomToTargetUom(orderLineItem.getStockItem(), orderLineItem.getPreferredUom()));
                wrappers.add(wrapper);
            }
            return wrappers;
        } else {
            for (FulfillmentOrderReversePickLineItem reversePickLineItem : reversePick.getLineItems()) {
                FulfillmentOrderLineItem orderLineItem = orderLineItemMap.get(reversePickLineItem.getFulfillmentOrderLineItemId());

                FulfillmentOrderReversePickLineItemWrapper wrapper = createLineItemWrapper(reversePick, reversePickLineItem);
                wrapper.setFulfillmentOrder(fulfillmentOrder);
                wrapper.setFulfillmentOrderLineItem(orderLineItem);
                wrapper.setPreferredUomConversionFactor(UomUtility.getStandardUomToTargetUom(orderLineItem.getStockItem(), orderLineItem.getPreferredUom()));
                wrappers.add(wrapper);

            }
        }
        return wrappers;
    }

    private FulfillmentOrderReversePickLineItemWrapper createLineItemWrapper(FulfillmentOrderReversePick reversePick, FulfillmentOrderReversePickLineItem reversePickLineItem) {
        return ClientWrapperFactory.createFulfillmentOrderReversePickLineItemWrapper(reversePick, reversePickLineItem);
    }

    /**
     * Saves the current FulfillmentOrderReversePick if there are any changes.
     */
    public void saveReversePick() throws Exception {
        if (reversePick.isNew() || (reversePick.getStatus().equals(FulfillmentOrderReversePickStatus.NEW)) || reversePick.isDirty() && reversePick.isCoherent()) {
            reversePick.doSetFulfillmentOrderId(fulfillmentOrder.getId());
            ClientServiceFactory.getFulfillmentOrderReversePickServices().updateFulfillmentOrderReversePick(reversePick);
            RepositoryManager.addStateObject(SimClientStateKey.CUSTOMER_ORDER_REVERSE_PICK_MODIFIED, Boolean.TRUE);
        }
    }

    /**
     * Returns whether the current Pick is empty. A pick is empty if it contains
     * no line items or if all line items have a null or zero quantity.
     *
     * @return True if the current Pick is empty, otherwise false.
     */
    public boolean isReversePickEmpty() {
        for (FulfillmentOrderReversePickLineItem lineItem : reversePick.getLineItems()) {
            if (lineItem.getQuantityOrZero().isPositive()) {
                return false;
            }
        }
        return true;
    }

    public boolean isConfirmFunctionAvailable() {
        return !FulfillmentOrderStatus.getClosedSet().contains(fulfillmentOrder.getStatus());
    }

    public void confirmReversePick() throws Exception {
        ClientServiceFactory.getFulfillmentOrderReversePickServices().confirmFulfillmentOrderReversePick(reversePick);
        RepositoryManager.addStateObject(SimClientStateKey.CUSTOMER_ORDER_REVERSE_PICK_MODIFIED, Boolean.TRUE);
        RepositoryManager.addStateObject(SimClientStateKey.FULFILLMENT_ORDER_MODIFIED, Boolean.TRUE);
    }

    public void releaseLock() throws Exception {
        releaseLock(ActivityLockType.FULFILLMENT_ORDER_REVERSE_PICK, reversePick.getIdAsString());
    }

    /**
     * Sets default delivery quantities to the line items on the current delivery.
     * @param wrappers The line items for which to set default delivery quantities.
     */
    public void setDefaultQuantities(List<FulfillmentOrderReversePickLineItemWrapper> wrappers) throws Exception {

        //We can't set default quantities if substitutes exist on the Reverse Pick
        for (FulfillmentOrderReversePickLineItem lineItem : reversePick.getLineItems()) {
            FulfillmentOrderLineItem orderLineItem = orderLineItemMap.get(lineItem.getFulfillmentOrderLineItemId());
            if (orderLineItem.isSubstitute()) {
                UIStatusUtility.displayWarning(this, FulfillmentOrderMessageText.UNABLE_TO_DEFAULT_SUBSTITUTES_EXIST);
                return;
            }
        }

        reversePick.setSetDefaultQuantities(true);
        if (reversePick.isExternallyInitiated()) {
            for (FulfillmentOrderReversePickLineItemWrapper wrapper : wrappers) {
                if (wrapper.getQuantity() == null || wrapper.getQuantity().isZero()) {
                    UOMMode currentMode = wrapper.getUnitOfMeasureMode();
                    wrapper.setUnitOfMeasureMode(UOMMode.STANDARD);
                    wrapper.setQuantityBasedOnUom(wrapper.getSuggestedQtyBasedOnUom());
                    wrapper.setUnitOfMeasureMode(currentMode);
                }
            }
        } else {
            for (FulfillmentOrderReversePickLineItemWrapper wrapper : wrappers) {
                if (wrapper.getQuantity() == null || wrapper.getQuantity().isZero()) {
                    Quantity defaultQuantity = fulfillmentOrder.getRemainingQty((wrapper.getReversePickLineItem().getFulfillmentOrderLineItemId()));
                    UOMMode currentMode = wrapper.getUnitOfMeasureMode();
                    wrapper.setUnitOfMeasureMode(UOMMode.STANDARD);
                    wrapper.setQuantityBasedOnUom(defaultQuantity);
                    wrapper.setUnitOfMeasureMode(currentMode);
                }
            }
        }
        reversePick.doSetDirty();
    }

    /**
     * Returns whether or not the editing the current Reverse Pick is allowed.
     * @return True if the current Reverse Pick can be edited, otherwise false.
     */
    public boolean isReversePickEditAllowed() throws Exception {
        if (isReversePickClosed() || (!hasPermission(PermissionKey.PC_EDIT_CUSTOMER_ORDER_REVERSE_PICK) && (!reversePick.isNew()))) {
            return false;
        }
        return true;
    }

    public void cancelReversePick(Long reversePickId) throws Exception {
        if (obtainLock(ActivityLockType.FULFILLMENT_ORDER_REVERSE_PICK, reversePickId.toString())) {
            ClientServiceFactory.getFulfillmentOrderReversePickServices().cancelFulfillmentOrderReversePick(reversePickId);
        }
    }

    public boolean checkLock() throws Exception {
        if (reversePick.isNew()) {
            return true;
        }
        return confirmLock(ActivityLockType.FULFILLMENT_ORDER_REVERSE_PICK, reversePick.getId().toString());
    }

    public void releaseLock(String reversePickId) {
        try {
            releaseLock(ActivityLockType.FULFILLMENT_ORDER_REVERSE_PICK, reversePickId);
        } catch (Exception e) {
            UILog.error(getClass(), e);
        }
    }

    public void setAllQuantitiesToZero() {
        for (FulfillmentOrderReversePickLineItem lineItem : reversePick.getLineItems()) {
            if (lineItem.getQuantity() == null) {
                lineItem.doSetQuantity(Quantity.ZERO);
            }
        }
    }
}
