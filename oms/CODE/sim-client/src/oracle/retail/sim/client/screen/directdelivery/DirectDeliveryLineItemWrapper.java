package oracle.retail.sim.client.screen.directdelivery;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.uom.UomUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.directdelivery.DirectDelivery;
import oracle.retail.sim.common.directdelivery.DirectDeliveryCarton;
import oracle.retail.sim.common.directdelivery.DirectDeliveryLineItem;
import oracle.retail.sim.common.directdelivery.DirectDeliveryMessageText;
import oracle.retail.sim.common.directdelivery.DirectDeliveryPreferredCurrency;
import oracle.retail.sim.common.directdelivery.DirectDeliveryProperty;
import oracle.retail.sim.common.directdelivery.DirectDeliverySimpleLineItem;
import oracle.retail.sim.common.directdelivery.DirectDeliveryStatus;
import oracle.retail.sim.common.directdelivery.PurchaseOrderLineItem;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemStatus;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.item.SupplierItem;
import oracle.retail.sim.common.lineitem.SerialNumberLineItemWrapper;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.rules.core.CaseSizeAndQtyValidForUomRule;
import oracle.retail.sim.common.rules.core.QuantityCannotBeNegativeRule;
import oracle.retail.sim.common.rules.core.QuantityMustConformToUOMRule;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Direct Delivery Line Item Wrapper for Direct Delivery GUI
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DirectDeliveryLineItemWrapper extends SerialNumberLineItemWrapper {
    private DirectDelivery delivery;
    private Map<String, PurchaseOrderLineItem> purchaseOrderLineItemMap;
    private DirectDeliveryLineItem deliveryLineItem;
    private PurchaseOrderLineItem purchaseOrderLineItem;
    private BigDecimal preferredUomConversionFactor;

    public DirectDeliveryLineItemWrapper(DirectDelivery delivery) {
        this.delivery = delivery;
    }

    public void setPurchaseOrderLineItemMap(Map<String, PurchaseOrderLineItem> purchaseOrderLineItemMap) {
        //Used to construct existing line item
        if (this.purchaseOrderLineItemMap != null) {
            throw new IllegalArgumentException("Value is immutable");
        }
        this.purchaseOrderLineItemMap = purchaseOrderLineItemMap;
        initializePurchaseOrderLineItem();
    }

    public DirectDeliveryLineItem getLineItem() {
        return deliveryLineItem;
    }

    public void setLineItem(DirectDeliveryLineItem deliveryLineItem) {
        //Used to construct existing line item
        if (this.deliveryLineItem != null) {
            throw new IllegalArgumentException("Value is immutable");
        }
        this.deliveryLineItem = deliveryLineItem;
        initializePurchaseOrderLineItem();
    }

    private void initializePurchaseOrderLineItem() {
        if (purchaseOrderLineItem == null && purchaseOrderLineItemMap != null && deliveryLineItem != null) {
            purchaseOrderLineItem = purchaseOrderLineItemMap.get(deliveryLineItem.getStockItem().getId());
        }
    }

    public boolean isNew() {
        return deliveryLineItem == null;
    }

    public boolean isDeleteAllowed() {
        //Empty line item
        if (deliveryLineItem == null) {
            return true;
        }
        //Line item existing on purchase order
        if (purchaseOrderLineItem != null) {
            return false;
        }
        //New unsaved unexpected line item
        if (deliveryLineItem.isNew()) {
            return true;
        }
        //Saved unexpected line item being adjusted after confirmation
        if (delivery.isAllowAdjustment()) {
            return false;
        }
        //Saved unexpected line item not been confirmed
        return true;
    }

    public StockItem getStockItem() {
        return deliveryLineItem != null ? deliveryLineItem.getStockItem() : null;
    }

    public void setStockItem(StockItem stockItem) throws Exception {
        if (stockItem == null) {
            return;
        }
        if (deliveryLineItem != null && (stockItem.equals(deliveryLineItem.getStockItem()) || !isDeleteAllowed())) {
            return;
        }
        ItemStatus status = stockItem.getStatus();
        if (status == ItemStatus.DELETED || status == ItemStatus.INACTIVE || status == ItemStatus.DISCONTINUED) {
            if (!RConfirmUtility.confirm("Direct Delivery", CommonMessageText.NON_ACTIVE_ITEM_CONFIRM, status.toString())) {
                return;
            }
        }
        if (deliveryLineItem != null) {
            removeLineItem();
        }
        try {
            //Create new delivery line item
            deliveryLineItem = createLineItem(stockItem);
            //Add to carton
            DirectDeliveryCarton carton = delivery.getInternalCarton();
            if (carton == null) {
                carton = BOFactory.createDirectDeliveryCarton();
                carton.doSetStatus(DirectDeliveryStatus.IN_PROGRESS);
                delivery.doAddCarton(carton);
            }
            carton.addLineItem(deliveryLineItem);
        } catch (Exception e) {
            deliveryLineItem = null;
            purchaseOrderLineItem = null;
            throw e;
        }
    }

    private DirectDeliveryLineItem createLineItem(StockItem stockItem) throws Exception {
        DirectDeliverySimpleLineItem newLineItem = BOFactory.createDirectDeliverySimpleLineItem(stockItem);
        if (purchaseOrderLineItemMap != null) {
            purchaseOrderLineItem = purchaseOrderLineItemMap.get(stockItem.getId());
            if (purchaseOrderLineItem != null) {
                //Using purchase order line item
                newLineItem.setQuantityExpected(purchaseOrderLineItem.getQuantityOrdered());
                newLineItem.setCaseSize(purchaseOrderLineItem.getCaseSize());
                newLineItem.setUnitCost(purchaseOrderLineItem.getUnitCost());
                preferredUomConversionFactor = UomUtility.getStandardUomToTargetUom(stockItem, purchaseOrderLineItem.getPreferredUom());
                return newLineItem;
            }
        }
        //Using supplier item
        SupplierItem supplierItem = getSupplierItemForNewLineItem(stockItem.getId());
        if (supplierItem == null) {
            throw new BusinessException(DirectDeliveryMessageText.INVALID_VENDOR);
        }
        newLineItem.setQuantityExpected(Quantity.ZERO);
        Quantity caseSize = supplierItem.getCaseSize();
        if (caseSize == null) {
            caseSize = stockItem.getDefaultCaseSize();
        }
        newLineItem.setCaseSize(caseSize);
        SimMoney unitCost = getUnitCostForNewLineItem(supplierItem);
        if (unitCost != null) {
            newLineItem.setUnitCost(unitCost);
        }
        return newLineItem;
    }

    private SupplierItem getSupplierItemForNewLineItem(String itemId) throws Exception {
        List<SupplierItem> supplierItems = ClientServiceFactory.getItemServices().findSupplierItems(itemId, delivery.getSupplier().getId());
        if (supplierItems.isEmpty()) {
            return null;
        }
        SupplierItem supplierItem = null;
        String purchaseOrderCountryCode = getPurchaseOrderCountryCode();
        if (purchaseOrderCountryCode != null) {
            supplierItem = getSupplierItemByCountry(supplierItems, purchaseOrderCountryCode);
        }
        if (supplierItem == null) {
            supplierItem = getSupplierItemByCountry(supplierItems, delivery.getStore().getCountry());
        }
        return supplierItem;
    }

    private String getPurchaseOrderCountryCode() {
        if (purchaseOrderLineItemMap == null) {
            return null;
        }
        for (PurchaseOrderLineItem lineItem : purchaseOrderLineItemMap.values()) {
            if (!StringHelper.isNullOrEmpty(lineItem.getSupplierItem().getCountryCode())) {
                return lineItem.getSupplierItem().getCountryCode();
            }
        }
        return null;
    }

    private SupplierItem getSupplierItemByCountry(List<SupplierItem> supplierItems, String countryCode) {
        for (SupplierItem supplierItem : supplierItems) {
            if (supplierItem.getCountryCode().equals(countryCode)) {
                return supplierItem;
            }
        }
        return null;
    }

    private SimMoney getUnitCostForNewLineItem(SupplierItem supplierItem) {
        SimMoney unitCost = supplierItem.getUnitCost();
        if (unitCost != null && unitCost.getCurrencyCode().equals(getCurrencyCodeForNewUnitCost())) {
            return unitCost;
        }
        return null;
    }

    private String getCurrencyCodeForNewUnitCost() {
        String currencyCode = getDeliveryCurrencyCode();
        if (currencyCode != null) {
            return currencyCode;
        }
        Integer preferredCurrency = SimConfigManager.getInteger(SimConfigManager.DIRECT_DELIVERY_PREFERRED_CURRENCY);
        if (preferredCurrency != null && DirectDeliveryPreferredCurrency.toValue(preferredCurrency) == DirectDeliveryPreferredCurrency.SUPPLIER) {
            return delivery.getSupplier().getCurrencyCode();
        }
        return delivery.getStore().getCurrencyCode();
    }

    private String getDeliveryCurrencyCode() {
        for (DirectDeliveryLineItem lineItem : delivery.getLineItems()) {
            if (lineItem.getUnitCost() != null) {
                return lineItem.getUnitCost().getCurrencyCode();
            }
        }
        return null;
    }

    public boolean hasPurchaseOrder() {
        return delivery.getPurchaseOrder() != null;
    }

    public void removeLineItem() throws BusinessException {
        if (deliveryLineItem == null) {
            return;
        }
        if (purchaseOrderLineItem != null) {
            throw new BusinessException(ItemMessageText.ITEM_REMOVE_ERROR);
        }
        DirectDeliveryCarton carton = deliveryLineItem.getCarton();
        if (carton != null) {
            carton.removeLineItem(deliveryLineItem);
        }
        deliveryLineItem = null;
    }

    public String getItemIdDescription() {
        return getItemIdDescription(SimConfigManager.isItemShortDescription());
    }

    public String getItemIdDescription(boolean shortDescription) {
        if (deliveryLineItem == null) {
            return null;
        }
        return deliveryLineItem.getStockItem().getId() + " - " + getStockItemDescription(shortDescription);
    }

    public boolean isExpected() {
        return deliveryLineItem != null && deliveryLineItem.isExpected();
    }

    public Quantity getQuantityExpectedOrZero() {
        return deliveryLineItem != null ? deliveryLineItem.getQuantityExpectedOrZero() : Quantity.ZERO;
    }

    public Quantity getQuantityExpectedBasedOnUom() {
        return deliveryLineItem != null ? rationalizeQuantityBasedOnUom(deliveryLineItem.getQuantityExpected()) : null;
    }

    public Quantity getQuantityReceivedOrZero() {
        return deliveryLineItem != null ? deliveryLineItem.getQuantityReceivedOrZero() : Quantity.ZERO;
    }

    public Quantity getQuantityReceivedBasedOnUom() {
        return deliveryLineItem != null ? rationalizeQuantityBasedOnUom(deliveryLineItem.getQuantityReceived()) : null;
    }

    public void setQuantityReceived(Quantity quantity) throws BusinessException {
        if (deliveryLineItem != null) {
            checkForNullParameter("Quantity Received", quantity);
            QuantityCannotBeNegativeRule.execute(quantity);
            QuantityMustConformToUOMRule.execute(this, quantity);
            executeRule("setQuantityReceived", quantity);
            doSetQuantityReceived(quantity);
        }
    }

    public void setQuantityReceivedBasedOnUom(Quantity quantity) throws BusinessException {
        if (deliveryLineItem != null) {
            checkForNullParameter("Quantity Received Based On UOM", quantity);
            QuantityCannotBeNegativeRule.execute(quantity);
            QuantityMustConformToUOMRule.execute(this, quantity);
            executeRule("setQuantityReceivedBasedOnUom", quantity);
            doSetQuantityReceived(quantity.multiply(getCaseSize()));
        }
    }

    private void doSetQuantityReceived(Quantity quantity) throws BusinessException {
        if (isAGSNEnabled()) {
            //Check AGSN quantity limit
            if (quantity.compareTo(SimConfigManager.MAX_AGSN_QTY) > 0) {
                throw new BusinessException(CommonMessageText.NUMBER_NOT_1_TO_999);
            }
            if (delivery.isAllowAdjustment()) {
                //Check if new quantity requires user to remove existing confirmed UINs
                int received = 0;
                for (SerialNumberValue serialNumber : deliveryLineItem.getSerialNumbers()) {
                    if (!serialNumber.isDamaged()) {
                        received++;
                    }
                }
                if (new Quantity(received).compareTo(quantity) > 0) {
                    String[] values = new String[2];
                    values[0] = deliveryLineItem.getStockItem().getUINType().toString();
                    values[1] = getStockItemDescription();
                    throw new BusinessException(CommonMessageText.UIN_RECEIVE_QUANTITY_MISMATCH, values);
                }
            }
        }
        //Restrict over receiving
        if (hasPurchaseOrder() && !PermissionManager.hasPermission(PermissionKey.PC_OVER_RECEIVE_DIRECT_DELIVERY)) {
            if (quantity.add(deliveryLineItem.getQuantityDamagedOrZero()).compareTo(deliveryLineItem.getQuantityExpectedOrZero()) > 0) {
                throw new BusinessException(DirectDeliveryMessageText.OVER_RECEIVING_NOT_ALLOWED);
            }
        }
        deliveryLineItem.setQuantityReceived(quantity);
    }

    public Quantity getQuantityDamagedOrZero() {
        return deliveryLineItem != null ? deliveryLineItem.getQuantityDamagedOrZero() : Quantity.ZERO;
    }

    public Quantity getQuantityDamagedBasedOnUom() {
        return deliveryLineItem != null ? rationalizeQuantityBasedOnUom(deliveryLineItem.getQuantityDamaged()) : null;
    }

    public void setQuantityDamaged(Quantity quantity) throws BusinessException {
        if (deliveryLineItem != null) {
            checkForNullParameter("Quantity Damaged", quantity);
            QuantityCannotBeNegativeRule.execute(quantity);
            QuantityMustConformToUOMRule.execute(this, quantity);
            executeRule("setQuantityDamaged", quantity);
            doSetQuantityDamaged(quantity);
        }
    }

    public void setQuantityDamagedBasedOnUom(Quantity quantity) throws BusinessException {
        if (deliveryLineItem != null) {
            checkForNullParameter("Quantity Damaged Based On UOM", quantity);
            QuantityCannotBeNegativeRule.execute(quantity);
            QuantityMustConformToUOMRule.execute(this, quantity);
            executeRule("setQuantityDamagedBasedOnUom", quantity);
            doSetQuantityDamaged(quantity.multiply(getCaseSize()));
        }
    }

    private void doSetQuantityDamaged(Quantity quantity) throws BusinessException {
        if (isAGSNEnabled()) {
            //Check AGSN quantity limit
            if (quantity.compareTo(SimConfigManager.MAX_AGSN_QTY) > 0) {
                throw new BusinessException(CommonMessageText.NUMBER_NOT_1_TO_999);
            }
            //Check if new quantity requires user to remove existing confirmed UINs
            if (delivery.isAllowAdjustment()) {
                int damaged = 0;
                for (SerialNumberValue serialNumber : deliveryLineItem.getSerialNumbers()) {
                    if (serialNumber.isDamaged()) {
                        damaged++;
                    }
                }
                if (new Quantity(damaged).compareTo(quantity) > 0) {
                    String[] values = new String[2];
                    values[0] = deliveryLineItem.getStockItem().getUINType().toString();
                    values[1] = getStockItemDescription();
                    throw new BusinessException(CommonMessageText.UIN_RECEIVE_QUANTITY_MISMATCH, values);
                }
            }
        }
        //Restrict over receiving
        if (hasPurchaseOrder() && !PermissionManager.hasPermission(PermissionKey.PC_OVER_RECEIVE_DIRECT_DELIVERY)) {
            if (quantity.add(deliveryLineItem.getQuantityReceivedOrZero()).compareTo(deliveryLineItem.getQuantityExpectedOrZero()) > 0) {
                throw new BusinessException(DirectDeliveryMessageText.OVER_RECEIVING_NOT_ALLOWED);
            }
        }
        deliveryLineItem.setQuantityDamaged(quantity);
    }

    public Quantity getQuantityOrderedBasedOnUom() {
        return purchaseOrderLineItem != null ? rationalizeQuantityBasedOnUom(purchaseOrderLineItem.getQuantityOrdered()) : Quantity.ZERO;
    }

    public Quantity getQuantityOverageOrZero() {
        if (deliveryLineItem == null) {
            return Quantity.ZERO;
        }
        return deliveryLineItem.getQuantityReceivedOrZero().add(deliveryLineItem.getQuantityDamagedOrZero()).subtract(deliveryLineItem.getQuantityExpectedOrZero());
    }

    public UOMMode getUnitOfMeasureMode() {
        return deliveryLineItem != null ? super.getUnitOfMeasureMode() : getDefaultUomMode();
    }

    public Quantity getCaseSize() {
        if (deliveryLineItem == null || !isCasesMode()) {
            return Quantity.ONE;
        }
        return deliveryLineItem.getCaseSize();
    }

    public void setCaseSize(Quantity caseSize) throws BusinessException {
        if (deliveryLineItem != null && isCasesMode()) {
            CaseSizeAndQtyValidForUomRule.execute(this, caseSize, getQuantityExpectedOrZero());
            CaseSizeAndQtyValidForUomRule.execute(this, caseSize, getQuantityReceivedOrZero());
            CaseSizeAndQtyValidForUomRule.execute(this, caseSize, getQuantityDamagedOrZero());
            deliveryLineItem.setCaseSize(caseSize);
        }
    }

    public SimMoney getUnitCost() {
        return deliveryLineItem != null ? deliveryLineItem.getUnitCost() : null;
    }

    public void setUnitCost(SimMoney unitCost) throws BusinessException {
        if (deliveryLineItem != null) {
            if (unitCost != null) {
                String currencyCode = getCurrencyCodeForNewUnitCost();
                if (!unitCost.getCurrencyCode().equals(currencyCode)) {
                    unitCost = new SimMoney(unitCost.getAmount(), Currency.getInstance(currencyCode));
                }
            }
            deliveryLineItem.setUnitCost(unitCost);
        }
    }

    public void addSerialNumber(SerialNumberValue value) throws BusinessException {
        if (deliveryLineItem != null) {
            deliveryLineItem.addSerialNumber(value);
        }
    }

    public void removeSerialNumber(SerialNumberValue value) throws BusinessException {
        if (deliveryLineItem != null) {
            deliveryLineItem.removeSerialNumber(value.getUin());
        }
    }

    public void removeSerialNumbers() throws BusinessException {
        if (deliveryLineItem != null) {
            deliveryLineItem.removeSerialNumbers();
        }
    }

    public void setQuantitiesBasedOnSerialNumbers() throws BusinessException {
        if (deliveryLineItem != null) {
            deliveryLineItem.resetSerialNumberQuantities();
        }
    }

    public List<SerialNumberValue> getSerialNumbers() {
        if (deliveryLineItem == null) {
            return Collections.emptyList();
        }
        return deliveryLineItem.getSerialNumbers();
    }

    public Integer getSerialNumberCount() {
        return deliveryLineItem != null ? deliveryLineItem.getSerialNumbers().size() : 0;
    }

    private boolean isAGSNEnabled() {
        if (deliveryLineItem == null) {
            return false;
        }
        StockItem stockItem = deliveryLineItem.getStockItem();
        return stockItem.isSerialNumberRequired() && stockItem.isAgsnEnabled();
    }

    public boolean isMissingRequiredUins() {
        if (deliveryLineItem == null) {
            return false;
        }
        if (!isSerialNumberRequired() || isAGSNEnabled()) {
            return false;
        }
        Quantity quantity = deliveryLineItem.getQuantityReceivedOrZero().add(deliveryLineItem.getQuantityDamagedOrZero());
        return quantity.intValue() != deliveryLineItem.getSerialNumbers().size();
    }

    public String getPreferredUnitOfMeasure() {
        return purchaseOrderLineItem != null ? purchaseOrderLineItem.getPreferredUom() : null;
    }

    public BigDecimal getPreferredUomConversionFactor() {
        return preferredUomConversionFactor;
    }

    public void setPreferredUomConversionFactor(BigDecimal preferredUomConversionFactor) {
        //Used to construct existing line item
        if (this.preferredUomConversionFactor != null) {
            throw new IllegalArgumentException("Value is immutable");
        }
        this.preferredUomConversionFactor = preferredUomConversionFactor;
    }

    public boolean isPropertyModifiable(String name) throws Exception {
        if (DirectDeliveryProperty.SERIAL_NUMBER_COUNT.equals(name)) {
            if (isNew() || !isSerialNumberRequired()) {
                return false;
            }
            if (delivery.isAllowAdjustment() || DirectDeliveryStatus.getClosedSet().contains(delivery.getStatus())) {
                //enable so you can see which uins were created
                return true;
            }
            return !isAGSNEnabled();
        }
        if (DirectDeliveryProperty.UOM_MODE.equals(name)) {
            return !isNew() && (deliveryLineItem.getCaseSize().isPositive() || isPreferredUomConversionAvailable());
        }
        if (isViewOnly() || DirectDeliveryStatus.getClosedSet().contains(delivery.getStatus())) {
            return false;
        }
        if (DirectDeliveryProperty.STOCK_ITEM.equals(name)) {
            return isNew() || isDeleteAllowed();
        }
        if (isNew()) {
            return false;
        }
        if (DirectDeliveryProperty.UNIT_COST.equals(name)) {
            return !hasPurchaseOrder() || !isExpected();
        }
        if (DirectDeliveryProperty.QUANTITY_RECEIVED_UOM.equals(name)) {
            return !isSerialNumberRequired() || isAGSNEnabled();
        }
        if (DirectDeliveryProperty.QUANTITY_DAMAGED_UOM.equals(name)) {
            if (SimConfigManager.getBoolean(SimConfigManager.DISABLE_DAMAGES)) {
                return false;
            }
            if (!PermissionManager.hasPermission(PermissionKey.PC_RECEIVE_DAMAGES_DIRECT_DELIVERY)) {
                return false;
            }
            return !isSerialNumberRequired() || isAGSNEnabled();
        }
        if (DirectDeliveryProperty.CASE_SIZE.equals(name)) {
            if (SimConfigManager.getBoolean(SimConfigManager.DISABLE_CASE_SIZE)) {
                return false;
            }
            return isCasesMode();
        }
        return false;
    }
}
