package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = "OmsFulfillMatrixExtDetail.findAll",
                             query = "select o from OmsFulfillMatrixExtDetail o"),
                 @NamedQuery(name = "OmsFulfillMatrixExtDetail.findDetail",
                             query = "select o from OmsFulfillMatrixExtDetail o where o.combinationId=:combID and o.priority =:priority"),
                 @NamedQuery(name = "OmsFulfillMatrixExtDetail.findPriority",
                             query = "select o from OmsFulfillMatrixExtDetail o where o.combinationId=:combID and o.location =:location"),
                                  @NamedQuery(name = "OmsFulfillMatrixExtDetail.findByCombId",
                             query = "select o from OmsFulfillMatrixExtDetail o where o.combinationId=:combID ")

})
@Table(name = "OMS_FULFILL_MATRIX_EXT_DETAIL")
@IdClass(OmsFulfillMatrixExtDetailPK.class)
public class OmsFulfillMatrixExtDetail implements Serializable {
    @Id
    @Column(name = "COMBINATION_ID", nullable = false)
    private BigDecimal combinationId;
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "CREATED_BY", nullable = false, length = 40)
    private String createdBy;
    @Column(name = "DELIVERY_FROM_LOC", nullable = false)
    private BigDecimal deliveryFromLoc;
    @Column(name = "DELIVERY_FROM_LOC_TYPE", nullable = false, length = 20)
    private String deliveryFromLocType;
    @Column(name = "LAST_UPDATE_DATETIME")
    private Timestamp lastUpdateDatetime;
    @Column(nullable = false)
    private BigDecimal location;
    @Column(name = "LOCATION_TYPE", nullable = false, length = 20)
    private String locationType;
    @Column(name = "MAX_DLY_DAY")
    private BigDecimal maxDlyDay;
    @Column(name = "MIN_DLY_DAY")
    private BigDecimal minDlyDay;
    @Id
    @Column(nullable = false)
    private BigDecimal priority;
    @Column(name = "UPDATED_BY", length = 40)
    private String updatedBy;

    public OmsFulfillMatrixExtDetail() {
    }

    public OmsFulfillMatrixExtDetail(BigDecimal combinationId, Timestamp createDatetime, String createdBy,
                                     BigDecimal deliveryFromLoc, String deliveryFromLocType,
                                     Timestamp lastUpdateDatetime, BigDecimal location, String locationType,
                                     BigDecimal maxDlyDay, BigDecimal minDlyDay, BigDecimal priority,
                                     String updatedBy) {
        this.combinationId = combinationId;
        this.createDatetime = createDatetime;
        this.createdBy = createdBy;
        this.deliveryFromLoc = deliveryFromLoc;
        this.deliveryFromLocType = deliveryFromLocType;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.location = location;
        this.locationType = locationType;
        this.maxDlyDay = maxDlyDay;
        this.minDlyDay = minDlyDay;
        this.priority = priority;
        this.updatedBy = updatedBy;
    }

    public BigDecimal getCombinationId() {
        return combinationId;
    }

    public void setCombinationId(BigDecimal combinationId) {
        this.combinationId = combinationId;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public BigDecimal getDeliveryFromLoc() {
        return deliveryFromLoc;
    }

    public void setDeliveryFromLoc(BigDecimal deliveryFromLoc) {
        this.deliveryFromLoc = deliveryFromLoc;
    }

    public String getDeliveryFromLocType() {
        return deliveryFromLocType;
    }

    public void setDeliveryFromLocType(String deliveryFromLocType) {
        this.deliveryFromLocType = deliveryFromLocType;
    }

    public Timestamp getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public BigDecimal getLocation() {
        return location;
    }

    public void setLocation(BigDecimal location) {
        this.location = location;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public BigDecimal getMaxDlyDay() {
        return maxDlyDay;
    }

    public void setMaxDlyDay(BigDecimal maxDlyDay) {
        this.maxDlyDay = maxDlyDay;
    }

    public BigDecimal getMinDlyDay() {
        return minDlyDay;
    }

    public void setMinDlyDay(BigDecimal minDlyDay) {
        this.minDlyDay = minDlyDay;
    }

    public BigDecimal getPriority() {
        return priority;
    }

    public void setPriority(BigDecimal priority) {
        this.priority = priority;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
