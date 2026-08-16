package oracle.retail.sim.client.screen.uin;

import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINStatus;
import oracle.retail.sim.common.uin.UINType;
import oracle.retail.sim.common.uin.UINUserAction;

/********************************************************************************************************
 * UI Wrapper. Wraps a single UINValue object associated with an item and functional area. This is used
 * to display rows in the UIDialog.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SerialNumberWrapper {

    private String itemId;
    private FunctionalArea area;
    private String uinLabel;
    private UINType type;
    private SerialNumberValue value;
    private UINUserAction userAction = UINUserAction.ADDED;
    private boolean validated;
    private boolean damaged;
    private boolean damagedEditable = true;
    private boolean selected;

    public SerialNumberWrapper(FunctionalArea area, String itemId, UINType type, String uinLabel) {
        this.area = area;
        this.itemId = itemId;
        this.type = type;
        this.uinLabel = uinLabel;
    }

    public FunctionalArea getFunctionalArea() {
        return area;
    }

    public String getItemId() {
        return itemId;
    }

    public UINType getType() {
        return type;
    }

    public String getUINLabel() {
        return uinLabel;
    }

    public UINStatus getStatus() {
        if (value != null) {
            return value.getStatus();
        }
        return null;
    }

    public SerialNumberValue getSerialNumberValue() {
        return value;
    }

    public void setSerialNumberValue(SerialNumberValue newValue) {
        if (newValue != null) {
            value = newValue;
            damaged = newValue.isDamaged();
        }
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated() {
        validated = true;
    }

    public Boolean isDamaged() {
        return damaged;
    }

    public void setDamaged(Boolean damaged) {
        this.damaged = damaged;
    }

    public boolean isDamagedEditable() {
        return damagedEditable;
    }

    public void setDamagedEditable(boolean damagedEditable) {
        this.damagedEditable = damagedEditable;
    }

    // Returns whether or not chosen for pending inventory adjustment processing
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public UINUserAction getUserAction() {
        return userAction;
    }

    public void setUserAction(UINUserAction userAction) {
        this.userAction = userAction;
    }

    public void setDefaultAction() {
        if (value != null && value.getId() == null) {
            setUserAction(UINUserAction.ADDED);
        } else {
            setUserAction(UINUserAction.CONFIRMED);
        }
    }
    
    public boolean isAdded() {
        return userAction == UINUserAction.ADDED;
    }

    public boolean isDeleted() {
        return userAction == UINUserAction.REMOVED;
    }
    
    public boolean isConfirmed() {
        return userAction == UINUserAction.CONFIRMED;
    }

    public boolean isPropertyModifiable(String propertyName) {
        if (propertyName.equals(SerialNumberProperty.SERIAL_NUMBER)) {
            return value == null;
        }
        if (propertyName.equals(SerialNumberProperty.DAMAGED)) {
            return value != null && damagedEditable;
        }
        if (propertyName.equals(SerialNumberProperty.USER_ACTION)) {
            return false;
        }
        return true;
    }
}
