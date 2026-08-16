package oracle.retail.sim.client.screen.uin;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.uin.UINCaptureTime;
import oracle.retail.sim.common.uin.UINLabelVO;
import oracle.retail.sim.common.uin.UINStoreDept;
import oracle.retail.sim.common.uin.UINStoreDeptProperty;
import oracle.retail.sim.common.uin.UINType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/********************************************************************************************************
 * A business object representing the uin type, label, and capture timestore associated with a
 * merchandise hierarchy at a store.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UINStoreDeptWrapper extends Wrapper {
    private TicketTypeFormat ticketTypeFormat;
    private UINStoreDept uinStoreDept;

    public UINStoreDeptWrapper(UINStoreDept uinStoreDept) {
        this.uinStoreDept = uinStoreDept;
    }

    public Long getId() {
        return uinStoreDept.getId();
    }

    public String getIdAsString() {
        if (uinStoreDept.getId() != null) {
            return uinStoreDept.getId().toString();
        }
        return null;
    }

    public UINStoreDept getUINStoreDept() {
        return uinStoreDept;
    }

    public String getFullDepartmentName() {
        return uinStoreDept.getNode().getDepartmentId() + " - " + uinStoreDept.getNode().getDepartmentName();
    }

    public String getFullClassName() {
        return uinStoreDept.getNode().getClassId() + " - " + uinStoreDept.getNode().getClassName();
    }

    public Long getStoreId() {
        return uinStoreDept.getStoreId();
    }

    public void setStoreId(Long storeId) throws BusinessException {
        uinStoreDept.setStoreId(storeId);
    }

    public UINType getType() {
        return uinStoreDept.getType();
    }

    public void setType(UINType type) throws BusinessException {
        if (getType() != type) {
            setTicketTypeFormat(null);
            setCaptureTime(null);
            setLabel(null);
            setExternalCreateAllowed(false);
        }
        uinStoreDept.setType(type);
    }

    public UINLabelVO getLabel() {
        return uinStoreDept.getLabel();
    }

    public void setLabel(UINLabelVO label) throws BusinessException {
        uinStoreDept.setLabel(label);
    }

    public UINCaptureTime getCaptureTime() {
        return uinStoreDept.getCaptureTime();
    }

    public TicketTypeFormat getTicketTypeFormat() {
        return ticketTypeFormat;
    }

    public void setTicketTypeFormat(TicketTypeFormat ticketTypeFormat) throws BusinessException {
        this.ticketTypeFormat = ticketTypeFormat;
        if (ticketTypeFormat == null) {
            uinStoreDept.setReportFormatId(null);
        } else {
            uinStoreDept.setReportFormatId(Long.valueOf(ticketTypeFormat.getId()));
        }
    }

    public void setCaptureTime(UINCaptureTime captureTime) throws BusinessException {
        uinStoreDept.setCaptureTime(captureTime);
    }

    public boolean isExternalCreateAllowed() {
        return uinStoreDept.getCaptureTime() != UINCaptureTime.SALE ? uinStoreDept.isExternalCreateAllowed() : false;
    }

    public void setExternalCreateAllowed(boolean externalCreate) throws BusinessException {
        uinStoreDept.setExternalCreateAllowed(externalCreate);
    }

    public MdseHierarchyNode getNode() {
        return uinStoreDept.getNode();
    }

    public void setNode(MdseHierarchyNode node) throws BusinessException {
        uinStoreDept.setNode(node);
    }

    public boolean isDirty() {
        return uinStoreDept.isDirty();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        UINStoreDeptWrapper that = (UINStoreDeptWrapper) object;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(uinStoreDept.getId(), that.uinStoreDept.getId());
        return builder.isEquals();
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(uinStoreDept.getId());
        return builder.hashCode();
    }

    /**
     * This function should be used after getting rid off circular object references. Overloaded function
     * with header object (i.e. StockEvent) passed into Rule-class as well.
     */
    public boolean isPropertyModifiable(String property) {
        if (property.equals(UINStoreDeptProperty.EXTERNAL_CREATE_ALLOWED) && uinStoreDept.getCaptureTime() == UINCaptureTime.SALE) {
            return false;
        }
        if (property.equals(UINStoreDeptProperty.UIN_TICKET_TYPE_FORMAT) && uinStoreDept.getType() != UINType.AGSN) {
            return false;
        }
        try {
            executeRule("isPropertyModifiable", property);
        } catch (BusinessException bre) {
            return false;
        }
        return true;
    }
}
