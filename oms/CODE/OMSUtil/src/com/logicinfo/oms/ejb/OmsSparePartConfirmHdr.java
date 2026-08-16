package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsSparePartConfirmHdr.findAll",
                             query = "select o from OmsSparePartConfirmHdr o"),
                 @NamedQuery(name = "OmsSparePartConfirmHdr.findByKey",
                             query = "select o from OmsSparePartConfirmHdr o where o.serviceConfirmId = :serviceConfirmId and o.serviceRequestId = :serviceRequestId") })
@Table(name = "OMS_SPARE_PART_CONFIRM_HDR")
@IdClass(OmsSparePartConfirmHdrPK.class)
public class OmsSparePartConfirmHdr implements Serializable {
    @Column(name = "CREATED_BY", nullable = false, length = 40)
    private String createdBy;
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Id
    @Column(name = "SERVICE_CONFIRM_ID", nullable = false, length = 20)
    private String serviceConfirmId;
    @Id
    @Column(name = "SERVICE_REQUEST_ID", nullable = false, length = 20)
    private String serviceRequestId;
    @Column(nullable = false, length = 2)
    private String status;

    public OmsSparePartConfirmHdr() {
    }

    public OmsSparePartConfirmHdr(Timestamp createDatetime, String createdBy, String serviceConfirmId,
                                  String serviceRequestId, String status) {
        this.createDatetime = createDatetime;
        this.createdBy = createdBy;
        this.serviceConfirmId = serviceConfirmId;
        this.serviceRequestId = serviceRequestId;
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getServiceConfirmId() {
        return serviceConfirmId;
    }

    public void setServiceConfirmId(String serviceConfirmId) {
        this.serviceConfirmId = serviceConfirmId;
    }

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(String serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
