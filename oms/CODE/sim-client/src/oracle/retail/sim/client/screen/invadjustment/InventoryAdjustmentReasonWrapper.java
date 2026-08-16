package oracle.retail.sim.client.screen.invadjustment;

import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.item.InventoryDisposition;
import oracle.retail.sim.common.item.NonSellableQtyType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Wraps the InventoryAdjustmentReason for the PC UI Admin screen.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class InventoryAdjustmentReasonWrapper {
    private InventoryAdjustmentReason reason;

    public InventoryAdjustmentReasonWrapper(InventoryAdjustmentReason reason) {
        this.reason = reason;
    }

    public InventoryAdjustmentReason getReason() {
        return reason;
    }

    public Long getId() {
        return reason.getId();
    }

    public Integer getCode() {
        return reason.getCode();
    }

    public void setCode(Integer code) throws BusinessException {
        reason.setCode(code);
    }

    public String getDescription() {
        return reason.getDescription();
    }

    public void setDescription(String description) throws BusinessException {
        reason.setDescription(description);
    }

    public InventoryDisposition getDisposition() {
        return reason.getDisposition();
    }

    public void setDisposition(InventoryDisposition disposition) throws BusinessException {
        reason.setDisposition(disposition);
    }

    public NonSellableQtyType getToNonSellableQtyType() throws Exception {
        Long typeId = reason.getToNonSellableQtyTypeId();
        if (typeId != null) {
            for (NonSellableQtyType nonSellableType : ClientDataCacheUtility.getNonSellableQtyTypes()) {
                if (nonSellableType.getId().equals(typeId)) {
                    return nonSellableType;
                }
            }
        }
        return null;
    }

    public void setToNonSellableQtyType(NonSellableQtyType type) {
        reason.setToNonSellableQtyType(type);
    }

    public NonSellableQtyType getFromNonSellableQtyType() throws Exception {
        Long typeId = reason.getFromNonSellableQtyTypeId();
        if (typeId != null) {
            for (NonSellableQtyType nonSellableType : ClientDataCacheUtility.getNonSellableQtyTypes()) {
                if (nonSellableType.getId().equals(typeId)) {
                    return nonSellableType;
                }
            }
        }
        return null;
    }

    public void setFromNonSellableQtyType(NonSellableQtyType type) {
        reason.setFromNonSellableQtyType(type);
    }

    public boolean isDisplayable() {
        return reason.isDisplayable();
    }

    public void setDisplayable(boolean isDisplayable) throws BusinessException {
        reason.setDisplayable(isDisplayable);
    }

    public boolean isPublish() {
        return reason.isPublish();
    }

    public void setPublish(boolean publish) throws BusinessException {
        reason.setPublish(publish);
    }

    public boolean isSystemRequired() {
        return reason.isSystemRequired();
    }

    public boolean isPropertyModifiable(String property) {
        return reason.isPropertyModifiable(property);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }
        InventoryAdjustmentReasonWrapper that = (InventoryAdjustmentReasonWrapper) object;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.reason, that.reason);
        return builder.isEquals();
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(reason);
        return builder.toHashCode();
    }
}
