package oracle.retail.sim.client.screen.fulfillmentorderpick;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.uom.UomUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPick;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickAreaType;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickItemOrderComparator;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickLineItem;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickStatus;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickType;
import oracle.retail.sim.common.fulfillmentorderpick.GenerateBinsOptions;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.reportrequest.FulfillmentOrderPickReportRequest;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.tolerance.ToleranceAdmin;
import oracle.retail.sim.common.tolerance.ToleranceTopic;
import oracle.retail.sim.common.tolerance.ToleranceVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Fulfillment Order Pick Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderPickDetailModel extends SimScreenModel {

    private FulfillmentOrderPick pick;
    private boolean viewOnlyMode;

    private Map<Long, FulfillmentOrder> fulfillmentOrders = new HashMap<>();
    private Map<String, ToleranceAdmin> tolerances = new HashMap<>();
    private Map<String, String> primaryLocations = new HashMap<>();

    /**
     * Loads the selected Fulfillment Order Pick and sets if the screen is to be in view only mode.
     */
    public void loadPick() throws Exception {
        pick = (FulfillmentOrderPick) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER_PICK);

        viewOnlyMode = !isPickEditAllowed();
        if (viewOnlyMode) {
            return;
        }
        if (obtainLock()) {
            return;
        }
        viewOnlyMode = true;
    }

    /**
     * Returns the current Fulfillment Order Pick.
     * @return The current Fulfillment Order Pick.
     */
    public FulfillmentOrderPick getPick() {
        return pick;
    }

    /**
     * Returns a List of CustomerOrderPickLineItemWrappers representing all 'original' (not substitute) items on the current Pick.
     * @return A List of CustomerOrderPickLineItemWrappers.
     */
    public List<FulfillmentOrderPickLineItemWrapper> getPickItems() throws Exception {
        List<FulfillmentOrderPickLineItemWrapper> wrappers = new ArrayList<>();

        if (fulfillmentOrders.isEmpty()) {
            List<Long> fulfillmentOrderIds = new ArrayList<>();
            for (FulfillmentOrderPickLineItem lineItem : pick.getLineItems()) {
                if (!fulfillmentOrderIds.contains(lineItem.getFulfillmentOrderId())) {
                    fulfillmentOrderIds.add(lineItem.getFulfillmentOrderId());
                }
            }

            fulfillmentOrders = ClientServiceFactory.getFulfillmentOrderServices().readFulfillmentOrders(fulfillmentOrderIds);
        }

        List<FulfillmentOrderPickLineItem> lineItems = new ArrayList<>(pick.getLineItems());
        Collections.sort(lineItems, new FulfillmentOrderPickItemOrderComparator());
        pick.doSetLineItems(lineItems);

        loadTolerances();
        loadSequences();

        for (FulfillmentOrderPickLineItem lineItem : pick.getLineItems()) {
            if (lineItem.isSubstitute()) {
                continue;
            }
            StockItem stockItem = lineItem.getStockItem();
            BigDecimal factor = UomUtility.getStandardUomToTargetUom(stockItem, lineItem.getPreferredUom());
            FulfillmentOrder fulfillmentOrder = fulfillmentOrders.get(lineItem.getFulfillmentOrderId());
            FulfillmentOrderPickLineItemWrapper wrapper = ClientWrapperFactory.createFulfillmentOrderPickLineItemWrapper(pick, lineItem, fulfillmentOrder, tolerances.get(stockItem.getId()), factor);
            wrapper.setPrimaryLocation(primaryLocations.get(stockItem.getId()));
            wrappers.add(wrapper);
        }
        return wrappers;
    }

    private void loadTolerances() throws Exception {
        List<ToleranceVO> toleranceVOs = new ArrayList<>();
        for (FulfillmentOrderPickLineItem lineItem : pick.getLineItems()) {
            StockItem stockItem = lineItem.getStockItem();

            boolean toleranceFound = false;
            for (ToleranceVO toleranceVO : toleranceVOs) {
                if (stockItem.getClassId().equals(toleranceVO.getClassId()) && stockItem.getDepartmentId().equals(toleranceVO.getDepartmentId())) {
                    toleranceFound = true;
                    break;
                }
            }
            if (!toleranceFound) {
                ToleranceVO toleranceVO = BOFactory.createToleranceVO();
                toleranceVO.doSetClassId(stockItem.getClassId());
                toleranceVO.doSetDepartmentId(stockItem.getDepartmentId());
                toleranceVO.doSetStoreId(pick.getStoreId());
                toleranceVO.doSetTopic(ToleranceTopic.FULFILLMENT_ORDER_PICKING);
                toleranceVOs.add(toleranceVO);
            }
        }

        List<ToleranceAdmin> toleranceAdmins = ClientServiceFactory.getToleranceAdminServices().findToleranceAdmins(toleranceVOs);

        for (FulfillmentOrderPickLineItem lineItem : pick.getLineItems()) {
            for (ToleranceAdmin toleranceAdmin : toleranceAdmins) {
                if (lineItem.getStockItem().getClassId().equals(toleranceAdmin.getHierarchyNode().getClassId())
                        && lineItem.getStockItem().getDepartmentId().equals(toleranceAdmin.getHierarchyNode().getDepartmentId())) {
                    tolerances.put(lineItem.getStockItem().getId(), toleranceAdmin);
                    break;
                }
            }
        }

    }

    private void loadSequences() throws Exception {
        List<String> itemIds = new ArrayList<String>();
        for (FulfillmentOrderPickLineItem lineItem : pick.getLineItems()) {
            if (!itemIds.contains(lineItem.getStockItem().getId())) {
                itemIds.add(lineItem.getStockItem().getId());
            }
        }

        primaryLocations = ClientServiceFactory.getStoreSequenceServices().findPrimaryStoreSequenceAreaDescriptions(itemIds, pick.getStoreId());

        //If the sequence was not found for the item, use default areas
        for (FulfillmentOrderPickLineItem lineItem : pick.getLineItems()) {
            StockItem stockItem = lineItem.getStockItem();
            String primaryLocation = primaryLocations.get(stockItem.getId());
            if (StringHelper.isNullOrEmpty(primaryLocation)) {
                if (stockItem.getStockInBackRoom().isPositive()) {
                    primaryLocations.put(stockItem.getId(), FulfillmentOrderPickAreaType.BACKROOM.toString());
                } else if (stockItem.getStockOnShopFloor().isPositive()) {
                    primaryLocations.put(stockItem.getId(), FulfillmentOrderPickAreaType.SHOPFLOOR.toString());
                } else if (stockItem.getStockInDeliveryBay().isPositive()) {
                    primaryLocations.put(stockItem.getId(), FulfillmentOrderPickAreaType.DELIVERY_BAY.toString());
                } else {
                    primaryLocations.put(stockItem.getId(), FulfillmentOrderPickAreaType.NO_LOCATION.toString());
                }
            }
        }

    }

    /**
     * Returns whether or not the current Fulfillment Order Pick is allowed to be edited.
     * @return True if the current Pick can be edited, otherwise false.
     */
    public boolean isPickEditAllowed() throws Exception {
        if (viewOnlyMode) {
            return false;
        }
        if (!hasPermission(PermissionKey.PC_EDIT_CUSTOMER_ORDER_PICK) && !pick.isNew()) {
            return false;
        }
        return true;
    }

    /**
     * Returns whether the current Pick is in canceled or completed status.
     * @return True if the current Pick is in canceled or completed status, otherwise false.
     */
    public boolean isPickClosed() {
        return pick.getStatus() == FulfillmentOrderPickStatus.CANCELED || pick.getStatus() == FulfillmentOrderPickStatus.COMPLETED;
    }

    /**
     * Returns whether the current Pick is empty. A pick is empty if it contains no line items or
     * if all line items have a null or zero quantity.
     * @return True if the current Pick is empty, otherwise false.
     */
    public boolean isPickEmpty() {
        if (pick.getLineItems().isEmpty()) {
            return true;
        }
        boolean hasQuantity = false;
        for (FulfillmentOrderPickLineItem lineItem : pick.getLineItems()) {
            if (lineItem.getQuantityOrZero().isPositive()) {
                hasQuantity = true;
                break;
            }
        }
        return !hasQuantity;
    }

    /**
     * Returns whether or not the current Fulfillment Order Pick is a 'Bin' type pick.
     * @return True if the current Pick is a 'Bin' type pick.
     */
    public boolean isPickBinType() {
        return pick.getType() == FulfillmentOrderPickType.BIN;
    }

    /**
     * Returns whether the current store is configured to display sequence fields.
     * @return True if the current store is configured to display sequence fields, otherwise false.
     */
    public boolean isSequenceFieldEnabled() {
        return SimConfigManager.getStoreBoolean(StoreConfigKeys.DISPLAY_SEQUENCE_FIELDS, getStoreId());
    }

    /**
     * Returns whether all Bin Ids have been captured for the current Fulfillment Order Pick.
     * @return True if all Bin Ids have been captured for the current Pick, otherwise false.
     */
    public boolean isBinsCaptured() {
        if (pick.getType() != FulfillmentOrderPickType.BIN) {
            return true;
        }
        GenerateBinsOptions option = GenerateBinsOptions.toValue(getStoreString(StoreConfigKeys.GENERATE_BINS));
        if (option == GenerateBinsOptions.MANUAL) {
            for (FulfillmentOrderPickLineItem lineItem : pick.getLineItems()) {
                if (lineItem.getBin() == null || StringHelper.isNullOrEmpty(lineItem.getBin().getBinId())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns whether the screen is in 'view only' mode and edits are unable to be made.
     * @return True if the screen is in view only mode, otherwise false.
     */
    public boolean isViewOnlyMode() {
        return viewOnlyMode;
    }

    /**
     * Returns whether the GS1 scanner can be used.
     * @return True if the GS1 barcode scanner can be used, otherwise false.
     */
    public boolean isScannerAvailable() {
        return !(viewOnlyMode || isPickClosed());
    }

    public void updateExistingLineItem(FulfillmentOrderPickLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
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
     * Attempts to obtain an activity lock on the current Fulfillment Order Pick.
     * @return True if an activity lock was obtained, otherwise false.
     */
    public boolean obtainLock() throws Exception {
        if (pick.isNew()) {
            return true;
        }
        return obtainLock(ActivityLockType.FULFILLMENT_ORDER_PICK, pick.getId().toString());
    }

    /**
     * Checks if the user still holds an activity lock on the current Fulfillment Order Pick.
     * @return True if the user still holds an activity lock on the current Fulfillment Order Pick.
     */
    public boolean checkLock() throws Exception {
        if (pick.isNew()) {
            return true;
        }
        return confirmLock(ActivityLockType.FULFILLMENT_ORDER_PICK, pick.getId().toString());
    }

    /**
     * Releases the activity lock on the current Fulfillment Order Pick.
     */
    public void releaseLock() throws Exception {
        releaseLock(ActivityLockType.FULFILLMENT_ORDER_PICK, pick.getIdAsString());
    }

    /**
     * Prints the current Pick.
     */
    public void printPick() throws Exception {
        Long storeId = getStoreId();

        List<ReportFormat> formats = new ArrayList<>();
        formats.add(ReportFormat.CUSTOMER_ORDER_PICK);
        formats.add(ReportFormat.CUSTOMER_ORDER_PICK_DISCREPANCY);

        List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(storeId, formats);
        if (formatPrinters != null && formatPrinters.size() > 0) {
            FulfillmentOrderPickReportRequest reportRequest = BOFactory.createFulfillmentOrderPickReportRequest(pick.getId());
            SimClientPrintUtility.printReportRequest(reportRequest, formatPrinters, FulfillmentOrderMessageText.PICK_REPORT_PRINTED);
        }
    }

    /**
     * Cancels the current Fulfillment Order Pick.
     */
    public void cancelPick() throws Exception {
        if (checkLock()) {
            ClientServiceFactory.getFulfillmentOrderPickServices().cancelFulfillmentOrderPick(pick.getId());
            RepositoryManager.addStateObject(SimClientStateKey.CUSTOMER_ORDER_PICK_MODIFIED, Boolean.TRUE);
        }
    }

    /**
     * Confirms the current Fulfillment Order Pick.
     */
    public void confirmPick() throws Exception {
        if (checkLock()) {
            ClientServiceFactory.getFulfillmentOrderPickServices().confirmFulfillmentOrderPick(pick);
            RepositoryManager.addStateObject(SimClientStateKey.CUSTOMER_ORDER_PICK_MODIFIED, Boolean.TRUE);
        }
    }

    /**
     * Saves the current FulfillmentOrderPick if there are any changes.
     */
    public void savePick() throws Exception {
        if (checkLock() && (pick.getStatus() == FulfillmentOrderPickStatus.NEW || pick.isDirty())) {
            ClientServiceFactory.getFulfillmentOrderPickServices().updateFulfillmentOrderPick(pick);
            RepositoryManager.addStateObject(SimClientStateKey.CUSTOMER_ORDER_PICK_MODIFIED, Boolean.TRUE);
        }
    }
}
