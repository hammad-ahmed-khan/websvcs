package oracle.retail.sim.client.screen.security;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.deliverytimeslot.DeliveryTimeSlot;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.security.RoleType;
import oracle.retail.sim.common.security.UserType;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.stockcount.StockCountingMethod;
import oracle.retail.sim.common.stockreturn.ReturnReason;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/********************************************************************************************************
 * Data Permissions Wrapper - This wraps a data permission and an associated parameter value.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class DataPermissionWrapper {
    private final String name;
    private final Object value;

    public DataPermissionWrapper(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Return the data permission name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the data permission parameter key.
     * All data permissions use the same parameter key.
     */
    public String getKey() {
        return PermissionKey.DATA_VALUE_KEY;
    }

    /**
     * Returns the data permission parameter value.
     */
    public String getValue() {
        if (StringHelper.isNullOrEmpty(name) || value == null) {
            return StringConstants.EMPTY;
        }
        if (PermissionKey.DATA_INV_ADJUSTMENT_REASON.equals(name)) {
            return String.valueOf(((InventoryAdjustmentReason) value).getId());
        } else if (PermissionKey.DATA_ITEM_REQUEST_DELIVERY_TIMESLOT.equals(name)) {
            return String.valueOf(((DeliveryTimeSlot) value).getId());
        } else if (PermissionKey.DATA_PRODUCT_GROUP_TYPE.equals(name)) {
            return String.valueOf(((ProductGroupType) value).getCode());
        } else if (PermissionKey.DATA_RETURN_SOURCE.equals(name)) {
            return String.valueOf(((SourceType) value).getCode());
        } else if (PermissionKey.DATA_RETURN_REASON_CODE.equals(name)) {
            return String.valueOf(((ReturnReason) value).getId());
        } else if (PermissionKey.DATA_COUNTING_METHOD.equals(name)) {
            return String.valueOf(((StockCountingMethod) value).getCode());
        } else if (PermissionKey.DATA_ROLE_TYPE.equals(name)) {
            return String.valueOf(((RoleType) value).getId());
        } else if (PermissionKey.DATA_USER_TYPE.equals(name)) {
            return String.valueOf(((UserType) value).getCode());
        }
        return StringConstants.EMPTY;
    }

    /**
     * Return a string that describes the data permission parameter value.
     */
    public String getValueDescription() {
        if (StringHelper.isNullOrEmpty(name) || value == null) {
            return StringConstants.EMPTY;
        }
        if (PermissionKey.DATA_INV_ADJUSTMENT_REASON.equals(name)) {
            return ((InventoryAdjustmentReason) value).getDescription();
        } else if (PermissionKey.DATA_ITEM_REQUEST_DELIVERY_TIMESLOT.equals(name)) {
            return String.valueOf(((DeliveryTimeSlot) value).getDescription());
        } else if (PermissionKey.DATA_PRODUCT_GROUP_TYPE.equals(name)) {
            return ((ProductGroupType) value).toString();
        } else if (PermissionKey.DATA_RETURN_SOURCE.equals(name)) {
            return ((SourceType) value).toString();
        } else if (PermissionKey.DATA_RETURN_REASON_CODE.equals(name)) {
            return buildReturnReasonDescription((ReturnReason) value);
        } else if (PermissionKey.DATA_COUNTING_METHOD.equals(name)) {
            return ((StockCountingMethod) value).toString();
        } else if (PermissionKey.DATA_ROLE_TYPE.equals(name)) {
            return ((RoleType) value).getDescription();
        } else if (PermissionKey.DATA_USER_TYPE.equals(name)) {
            return ((UserType) value).toString();
        }
        return StringConstants.EMPTY;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        DataPermissionWrapper that = (DataPermissionWrapper) object;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(name, that.name);
        builder.append(value, that.value);
        return builder.isEquals();
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(name);
        builder.append(value);
        return builder.toHashCode();
    }

    private String buildReturnReasonDescription(ReturnReason returnReason) {
        String source = Translator.getText(returnReason.getType().toString());
        String reasonDescription = Translator.getText(returnReason.getDescription());
        return source + " - " + reasonDescription;
    }
}
