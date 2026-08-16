package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsOrposCustOrdItm.findAll", query = "select o from OmsOrposCustOrdItm o"),
                 @NamedQuery(name = "OmsOrposCustOrdItm.findByOmsOrposCustOrdId", query = "select o from OmsOrposCustOrdItm o where o.omsOrposCustOrderId=:omsOrposCustOrderId"),
                 @NamedQuery(name = "OmsOrposCustOrdItm.findByOmsOrposCustOrdIdAndItem", query = "select o from OmsOrposCustOrdItm o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.itemId=:itemId and o.lineItemNo=:lineItemNo"),
                  @NamedQuery(name = "OmsOrposCustOrdItm.findColumns",
                             query = "select o from OmsOrposCustOrdItm o where o.omsOrposCustOrderId=:omsOrposCustOrderId"),
                  @NamedQuery(name = "OmsOrposCustOrdItm.findByOmsOrposCustOrdIdAndLineItemNo", query = "select o from OmsOrposCustOrdItm o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.lineItemNo=:lineItemNo")})
@Table(name = "OMS_ORPOS_CUST_ORD_ITM")
@IdClass(OmsOrposCustOrdItmPK.class)
public class OmsOrposCustOrdItm implements Serializable {
    @Column(name = "ALLOW_NEW_SERIAL_NUMBER_FLAG", length = 1)
    private String allowNewSerialNumberFlag;
    @Column(name = "AVAILABLE_QUANTITY", nullable = false)
    private BigDecimal availableQuantity;
    @Column(name = "CANCELLED_AMOUNT", nullable = false)
    private BigDecimal cancelledAmount;
    @Column(name = "CANCELLED_DISCOUNT_AMOUNT")
    private BigDecimal cancelledDiscountAmount;
    @Column(name = "CANCELLED_INCLUSIVE_TAX_AMOUNT")
    private BigDecimal cancelledInclusiveTaxAmount;
    @Column(name = "CANCELLED_QUANTITY", nullable = false)
    private BigDecimal cancelledQuantity;
    @Column(name = "CANCELLED_TAX_AMOUNT")
    private BigDecimal cancelledTaxAmount;
    @Id
    @Column(name = "CAPTURED_LINE_ITEM_NO", nullable = false)
    private BigDecimal capturedLineItemNo;
    @Column(name = "COMPLETED_AMOUNT", nullable = false)
    private BigDecimal completedAmount;
    @Column(name = "COMPLETED_DISCOUNT_AMOUNT")
    private BigDecimal completedDiscountAmount;
    @Column(name = "COMPLETED_INCLUSIVE_TAX_AMOUNT")
    private BigDecimal completedInclusiveTaxAmount;
    @Column(name = "COMPLETED_QUANTITY", nullable = false)
    private BigDecimal completedQuantity;
    @Column(name = "COMPLETED_TAX_AMOUNT")
    private BigDecimal completedTaxAmount;
    @Column(name = "CURRENCY_CODE", nullable = false, length = 3)
    private String currencyCode;
    @Column(name = "DAMAGE_DISCOUNTABLE_FLAG", length = 1)
    private String damageDiscountableFlag;
    @Column(name = "DEPARTMENT_ID", nullable = false, length = 14)
    private String departmentId;
    @Column(name = "DISCOUNT_TOTAL")
    private BigDecimal discountTotal;
    @Column(name = "DISCOUNTABLE_FLAG", length = 1)
    private String discountableFlag;
    @Column(name = "EMPLOYEE_DISCOUNTABLE_FLAG", length = 1)
    private String employeeDiscountableFlag;
    @Column(name = "ENTRY_METHOD", length = 7)
    private String entryMethod;
    @Column(name = "FULFILLMENT_SEQ_NO", nullable = false)
    private BigDecimal fulfillmentSeqNo;
    @Column(name = "GIFT_RECEIPTED_ITEM_FLAG", length = 1)
    private String giftReceiptedItemFlag;
    @Column(name = "GIFT_REGISTRY_ID", length = 14)
    private String giftRegistryId;
    @Column(name = "INCLUSIVE_TAX_TOTAL")
    private BigDecimal inclusiveTaxTotal;
    @Column(name = "ITEM_DESCRIPTION", nullable = false, length = 250)
    private String itemDescription;
    @Id
    @Column(name = "ITEM_ID", nullable = false, length = 25)
    private String itemId;
    @Column(name = "ITEM_TOTAL")
    private BigDecimal itemTotal;
    @Column(name = "ITEM_TYPE", length = 4)
    private String itemType;
    @Column(name = "ITEM_UPC", nullable = false, length = 25)
    private String itemUpc;
    @Column(name = "LINE_ITEM_NO")
    private BigDecimal lineItemNo;
    @Column(name = "MANUFACTURER_UPC", length = 14)
    private String manufacturerUpc;
    @Column(name = "MERCHANDISE_HIERARCHY_GROUP_ID", length = 14)
    private String merchandiseHierarchyGroupId;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PAID_AMOUNT", nullable = false)
    private BigDecimal paidAmount;
    @Column(name = "PRODUCT_GROUP", length = 4)
    private String productGroup;
    @Column(nullable = false)
    private BigDecimal quantity;
    @Column(name = "RESTOCKING_FEE_FLAG", length = 1)
    private String restockingFeeFlag;
    @Column(name = "RESTRICTIVE_AGE", nullable = false)
    private BigDecimal restrictiveAge;
    @Column(name = "RETURN_ELIGIBLE_FLAG", length = 1)
    private String returnEligibleFlag;
    @Column(name = "RETURNED_AMOUNT", nullable = false)
    private BigDecimal returnedAmount;
    @Column(name = "RETURNED_DISCOUNT_AMOUNT")
    private BigDecimal returnedDiscountAmount;
    @Column(name = "RETURNED_INCLUSIVE_TAX_AMOUNT")
    private BigDecimal returnedInclusiveTaxAmount;
    @Column(name = "RETURNED_QUANTITY", nullable = false)
    private BigDecimal returnedQuantity;
    @Column(name = "RETURNED_TAX_AMOUNT")
    private BigDecimal returnedTaxAmount;
    @Column(name = "SERIAL_NUMBER", length = 40)
    private String serialNumber;
    @Column(name = "SERIALIZED_ITEM_FLAG", length = 1)
    private String serializedItemFlag;
    @Column(name = "SHIPPING_CHARGE_FLAG", length = 1)
    private String shippingChargeFlag;
    @Column(name = "SIZE_CODE", length = 10)
    private String sizeCode;
    @Column(name = "SIZE_REQUIRED_FLAG", length = 1)
    private String sizeRequiredFlag;
    @Column(name = "TAX_GROUP_ID", nullable = false)
    private BigDecimal taxGroupId;
    @Column(name = "TAX_TOTAL")
    private BigDecimal taxTotal;
    @Column(name = "TAXABLE_FLAG", length = 1)
    private String taxableFlag;
    @Column(name = "UNIT_OF_MEASURE", length = 4)
    private String unitOfMeasure;
    @Column(name = "UNIT_REGULAR_PRICE", nullable = false)
    private BigDecimal unitRegularPrice;
    @Column(name = "UNIT_SELL_PRICE", nullable = false)
    private BigDecimal unitSellPrice;
    @Column(name = "VALIDATE_SERIAL_NUMBER_FLAG", length = 1)
    private String validateSerialNumberFlag;
    private BigDecimal weight;

    public OmsOrposCustOrdItm() {
    }

    public OmsOrposCustOrdItm(String allowNewSerialNumberFlag, BigDecimal availableQuantity,
                              BigDecimal cancelledAmount, BigDecimal cancelledDiscountAmount,
                              BigDecimal cancelledInclusiveTaxAmount, BigDecimal cancelledQuantity,
                              BigDecimal cancelledTaxAmount, BigDecimal capturedLineItemNo, BigDecimal completedAmount,
                              BigDecimal completedDiscountAmount, BigDecimal completedInclusiveTaxAmount,
                              BigDecimal completedQuantity, BigDecimal completedTaxAmount, String currencyCode,
                              String damageDiscountableFlag, String departmentId, BigDecimal discountTotal,
                              String discountableFlag, String employeeDiscountableFlag, String entryMethod,
                              BigDecimal fulfillmentSeqNo, String giftReceiptedItemFlag, String giftRegistryId,
                              BigDecimal inclusiveTaxTotal, String itemDescription, String itemId,
                              BigDecimal itemTotal, String itemType, String itemUpc, BigDecimal lineItemNo,
                              String manufacturerUpc, String merchandiseHierarchyGroupId,
                              BigDecimal omsOrposCustOrderId, BigDecimal paidAmount, String productGroup,
                              BigDecimal quantity, String restockingFeeFlag, BigDecimal restrictiveAge,
                              String returnEligibleFlag, BigDecimal returnedAmount, BigDecimal returnedDiscountAmount,
                              BigDecimal returnedInclusiveTaxAmount, BigDecimal returnedQuantity,
                              BigDecimal returnedTaxAmount, String serialNumber, String serializedItemFlag,
                              String shippingChargeFlag, String sizeCode, String sizeRequiredFlag,
                              BigDecimal taxGroupId, BigDecimal taxTotal, String taxableFlag, String unitOfMeasure,
                              BigDecimal unitRegularPrice, BigDecimal unitSellPrice, String validateSerialNumberFlag,
                              BigDecimal weight) {
        this.allowNewSerialNumberFlag = allowNewSerialNumberFlag;
        this.availableQuantity = availableQuantity;
        this.cancelledAmount = cancelledAmount;
        this.cancelledDiscountAmount = cancelledDiscountAmount;
        this.cancelledInclusiveTaxAmount = cancelledInclusiveTaxAmount;
        this.cancelledQuantity = cancelledQuantity;
        this.cancelledTaxAmount = cancelledTaxAmount;
        this.capturedLineItemNo = capturedLineItemNo;
        this.completedAmount = completedAmount;
        this.completedDiscountAmount = completedDiscountAmount;
        this.completedInclusiveTaxAmount = completedInclusiveTaxAmount;
        this.completedQuantity = completedQuantity;
        this.completedTaxAmount = completedTaxAmount;
        this.currencyCode = currencyCode;
        this.damageDiscountableFlag = damageDiscountableFlag;
        this.departmentId = departmentId;
        this.discountTotal = discountTotal;
        this.discountableFlag = discountableFlag;
        this.employeeDiscountableFlag = employeeDiscountableFlag;
        this.entryMethod = entryMethod;
        this.fulfillmentSeqNo = fulfillmentSeqNo;
        this.giftReceiptedItemFlag = giftReceiptedItemFlag;
        this.giftRegistryId = giftRegistryId;
        this.inclusiveTaxTotal = inclusiveTaxTotal;
        this.itemDescription = itemDescription;
        this.itemId = itemId;
        this.itemTotal = itemTotal;
        this.itemType = itemType;
        this.itemUpc = itemUpc;
        this.lineItemNo = lineItemNo;
        this.manufacturerUpc = manufacturerUpc;
        this.merchandiseHierarchyGroupId = merchandiseHierarchyGroupId;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.paidAmount = paidAmount;
        this.productGroup = productGroup;
        this.quantity = quantity;
        this.restockingFeeFlag = restockingFeeFlag;
        this.restrictiveAge = restrictiveAge;
        this.returnEligibleFlag = returnEligibleFlag;
        this.returnedAmount = returnedAmount;
        this.returnedDiscountAmount = returnedDiscountAmount;
        this.returnedInclusiveTaxAmount = returnedInclusiveTaxAmount;
        this.returnedQuantity = returnedQuantity;
        this.returnedTaxAmount = returnedTaxAmount;
        this.serialNumber = serialNumber;
        this.serializedItemFlag = serializedItemFlag;
        this.shippingChargeFlag = shippingChargeFlag;
        this.sizeCode = sizeCode;
        this.sizeRequiredFlag = sizeRequiredFlag;
        this.taxGroupId = taxGroupId;
        this.taxTotal = taxTotal;
        this.taxableFlag = taxableFlag;
        this.unitOfMeasure = unitOfMeasure;
        this.unitRegularPrice = unitRegularPrice;
        this.unitSellPrice = unitSellPrice;
        this.validateSerialNumberFlag = validateSerialNumberFlag;
        this.weight = weight;
    }

    public String getAllowNewSerialNumberFlag() {
        return allowNewSerialNumberFlag;
    }

    public void setAllowNewSerialNumberFlag(String allowNewSerialNumberFlag) {
        this.allowNewSerialNumberFlag = allowNewSerialNumberFlag;
    }

    public BigDecimal getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(BigDecimal availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public BigDecimal getCancelledAmount() {
        return cancelledAmount;
    }

    public void setCancelledAmount(BigDecimal cancelledAmount) {
        this.cancelledAmount = cancelledAmount;
    }

    public BigDecimal getCancelledDiscountAmount() {
        return cancelledDiscountAmount;
    }

    public void setCancelledDiscountAmount(BigDecimal cancelledDiscountAmount) {
        this.cancelledDiscountAmount = cancelledDiscountAmount;
    }

    public BigDecimal getCancelledInclusiveTaxAmount() {
        return cancelledInclusiveTaxAmount;
    }

    public void setCancelledInclusiveTaxAmount(BigDecimal cancelledInclusiveTaxAmount) {
        this.cancelledInclusiveTaxAmount = cancelledInclusiveTaxAmount;
    }

    public BigDecimal getCancelledQuantity() {
        return cancelledQuantity;
    }

    public void setCancelledQuantity(BigDecimal cancelledQuantity) {
        this.cancelledQuantity = cancelledQuantity;
    }

    public BigDecimal getCancelledTaxAmount() {
        return cancelledTaxAmount;
    }

    public void setCancelledTaxAmount(BigDecimal cancelledTaxAmount) {
        this.cancelledTaxAmount = cancelledTaxAmount;
    }

    public BigDecimal getCapturedLineItemNo() {
        return capturedLineItemNo;
    }

    public void setCapturedLineItemNo(BigDecimal capturedLineItemNo) {
        this.capturedLineItemNo = capturedLineItemNo;
    }

    public BigDecimal getCompletedAmount() {
        return completedAmount;
    }

    public void setCompletedAmount(BigDecimal completedAmount) {
        this.completedAmount = completedAmount;
    }

    public BigDecimal getCompletedDiscountAmount() {
        return completedDiscountAmount;
    }

    public void setCompletedDiscountAmount(BigDecimal completedDiscountAmount) {
        this.completedDiscountAmount = completedDiscountAmount;
    }

    public BigDecimal getCompletedInclusiveTaxAmount() {
        return completedInclusiveTaxAmount;
    }

    public void setCompletedInclusiveTaxAmount(BigDecimal completedInclusiveTaxAmount) {
        this.completedInclusiveTaxAmount = completedInclusiveTaxAmount;
    }

    public BigDecimal getCompletedQuantity() {
        return completedQuantity;
    }

    public void setCompletedQuantity(BigDecimal completedQuantity) {
        this.completedQuantity = completedQuantity;
    }

    public BigDecimal getCompletedTaxAmount() {
        return completedTaxAmount;
    }

    public void setCompletedTaxAmount(BigDecimal completedTaxAmount) {
        this.completedTaxAmount = completedTaxAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDamageDiscountableFlag() {
        return damageDiscountableFlag;
    }

    public void setDamageDiscountableFlag(String damageDiscountableFlag) {
        this.damageDiscountableFlag = damageDiscountableFlag;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(BigDecimal discountTotal) {
        this.discountTotal = discountTotal;
    }

    public String getDiscountableFlag() {
        return discountableFlag;
    }

    public void setDiscountableFlag(String discountableFlag) {
        this.discountableFlag = discountableFlag;
    }

    public String getEmployeeDiscountableFlag() {
        return employeeDiscountableFlag;
    }

    public void setEmployeeDiscountableFlag(String employeeDiscountableFlag) {
        this.employeeDiscountableFlag = employeeDiscountableFlag;
    }

    public String getEntryMethod() {
        return entryMethod;
    }

    public void setEntryMethod(String entryMethod) {
        this.entryMethod = entryMethod;
    }

    public BigDecimal getFulfillmentSeqNo() {
        return fulfillmentSeqNo;
    }

    public void setFulfillmentSeqNo(BigDecimal fulfillmentSeqNo) {
        this.fulfillmentSeqNo = fulfillmentSeqNo;
    }

    public String getGiftReceiptedItemFlag() {
        return giftReceiptedItemFlag;
    }

    public void setGiftReceiptedItemFlag(String giftReceiptedItemFlag) {
        this.giftReceiptedItemFlag = giftReceiptedItemFlag;
    }

    public String getGiftRegistryId() {
        return giftRegistryId;
    }

    public void setGiftRegistryId(String giftRegistryId) {
        this.giftRegistryId = giftRegistryId;
    }

    public BigDecimal getInclusiveTaxTotal() {
        return inclusiveTaxTotal;
    }

    public void setInclusiveTaxTotal(BigDecimal inclusiveTaxTotal) {
        this.inclusiveTaxTotal = inclusiveTaxTotal;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(BigDecimal itemTotal) {
        this.itemTotal = itemTotal;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemUpc() {
        return itemUpc;
    }

    public void setItemUpc(String itemUpc) {
        this.itemUpc = itemUpc;
    }

    public BigDecimal getLineItemNo() {
        return lineItemNo;
    }

    public void setLineItemNo(BigDecimal lineItemNo) {
        this.lineItemNo = lineItemNo;
    }

    public String getManufacturerUpc() {
        return manufacturerUpc;
    }

    public void setManufacturerUpc(String manufacturerUpc) {
        this.manufacturerUpc = manufacturerUpc;
    }

    public String getMerchandiseHierarchyGroupId() {
        return merchandiseHierarchyGroupId;
    }

    public void setMerchandiseHierarchyGroupId(String merchandiseHierarchyGroupId) {
        this.merchandiseHierarchyGroupId = merchandiseHierarchyGroupId;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getRestockingFeeFlag() {
        return restockingFeeFlag;
    }

    public void setRestockingFeeFlag(String restockingFeeFlag) {
        this.restockingFeeFlag = restockingFeeFlag;
    }

    public BigDecimal getRestrictiveAge() {
        return restrictiveAge;
    }

    public void setRestrictiveAge(BigDecimal restrictiveAge) {
        this.restrictiveAge = restrictiveAge;
    }

    public String getReturnEligibleFlag() {
        return returnEligibleFlag;
    }

    public void setReturnEligibleFlag(String returnEligibleFlag) {
        this.returnEligibleFlag = returnEligibleFlag;
    }

    public BigDecimal getReturnedAmount() {
        return returnedAmount;
    }

    public void setReturnedAmount(BigDecimal returnedAmount) {
        this.returnedAmount = returnedAmount;
    }

    public BigDecimal getReturnedDiscountAmount() {
        return returnedDiscountAmount;
    }

    public void setReturnedDiscountAmount(BigDecimal returnedDiscountAmount) {
        this.returnedDiscountAmount = returnedDiscountAmount;
    }

    public BigDecimal getReturnedInclusiveTaxAmount() {
        return returnedInclusiveTaxAmount;
    }

    public void setReturnedInclusiveTaxAmount(BigDecimal returnedInclusiveTaxAmount) {
        this.returnedInclusiveTaxAmount = returnedInclusiveTaxAmount;
    }

    public BigDecimal getReturnedQuantity() {
        return returnedQuantity;
    }

    public void setReturnedQuantity(BigDecimal returnedQuantity) {
        this.returnedQuantity = returnedQuantity;
    }

    public BigDecimal getReturnedTaxAmount() {
        return returnedTaxAmount;
    }

    public void setReturnedTaxAmount(BigDecimal returnedTaxAmount) {
        this.returnedTaxAmount = returnedTaxAmount;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSerializedItemFlag() {
        return serializedItemFlag;
    }

    public void setSerializedItemFlag(String serializedItemFlag) {
        this.serializedItemFlag = serializedItemFlag;
    }

    public String getShippingChargeFlag() {
        return shippingChargeFlag;
    }

    public void setShippingChargeFlag(String shippingChargeFlag) {
        this.shippingChargeFlag = shippingChargeFlag;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getSizeRequiredFlag() {
        return sizeRequiredFlag;
    }

    public void setSizeRequiredFlag(String sizeRequiredFlag) {
        this.sizeRequiredFlag = sizeRequiredFlag;
    }

    public BigDecimal getTaxGroupId() {
        return taxGroupId;
    }

    public void setTaxGroupId(BigDecimal taxGroupId) {
        this.taxGroupId = taxGroupId;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public String getTaxableFlag() {
        return taxableFlag;
    }

    public void setTaxableFlag(String taxableFlag) {
        this.taxableFlag = taxableFlag;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public BigDecimal getUnitRegularPrice() {
        return unitRegularPrice;
    }

    public void setUnitRegularPrice(BigDecimal unitRegularPrice) {
        this.unitRegularPrice = unitRegularPrice;
    }

    public BigDecimal getUnitSellPrice() {
        return unitSellPrice;
    }

    public void setUnitSellPrice(BigDecimal unitSellPrice) {
        this.unitSellPrice = unitSellPrice;
    }

    public String getValidateSerialNumberFlag() {
        return validateSerialNumberFlag;
    }

    public void setValidateSerialNumberFlag(String validateSerialNumberFlag) {
        this.validateSerialNumberFlag = validateSerialNumberFlag;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
