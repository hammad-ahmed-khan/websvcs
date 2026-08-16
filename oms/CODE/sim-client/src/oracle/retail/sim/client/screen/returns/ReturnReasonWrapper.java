package oracle.retail.sim.client.screen.returns;

import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.item.NonSellableQtyType;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.stockreturn.ReturnReason;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Wraps the Return Reason for the PC UI Admin screen.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public class ReturnReasonWrapper {
    private ReturnReason reason;

    public ReturnReasonWrapper(ReturnReason reason) {
        this.reason = reason;
    }

    public ReturnReason getReason() {
        return reason;
    }

    public SourceType getType() {
        return reason.getType();
    }

    public void setType(SourceType sourceType) {
        reason.setType(sourceType);
    }

    public String getCode() {
        return reason.getCode();
    }

    public void setCode(String code) throws BusinessException {
        reason.setCode(code);
    }

    public String getDescription() {
        return reason.getDescription();
    }

    public void setDescription(String description) throws BusinessException {
        reason.setDescription(description);
    }

    public NonSellableQtyType getNonSellableQtyType() throws Exception {
        Long typeId = reason.getNonSellableQtyTypeId();
        if (typeId != null) {
            for (NonSellableQtyType nonSellableType : ClientDataCacheUtility.getNonSellableQtyTypes()) {
                if (nonSellableType.getId().equals(typeId)) {
                    return nonSellableType;
                }
            }
        }
        return null;
    }

    public void setNonSellableQtyType(NonSellableQtyType type) {
        reason.setNonSellableQtyType(type);
    }

    public Boolean isUseAvailable() {
        return reason.isUseAvailable();
    }

    public void setUseAvailable(Boolean isUseAvailable) {
        reason.setUseAvailable(isUseAvailable);
    }

    public Boolean isSystemRequired() {
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
        ReturnReasonWrapper that = (ReturnReasonWrapper) object;
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
