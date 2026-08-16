package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = "OmsCustOrdTracking.findAll", query = "select o from OmsCustOrdTracking o") })
@Table(name = "OMS_CUST_ORD_TRACKING")
public class OmsCustOrdTracking implements Serializable {
    @SuppressWarnings("compatibility:7929367777459649069")
    private static final long serialVersionUID = -4087531793399527837L;
    
    @Column(name = "ACTUAL_DELIVERY_DATETIME", nullable = false)
       private Timestamp actualDeliveryDatetime;
    
       @Column(nullable = false, length = 100)
       private String awb;
    
       @Column(nullable = false, length = 50)
       private String carrier;
    
       @Column(name = "CREATED_DATETIME")
       private Timestamp createdDatetime;
    
       @Column(nullable = false)
       private BigDecimal loc;
    
       @Id
       @Column(name = "TRACK_ID", nullable = false)
       @SequenceGenerator( name = "omsTrackIdSeq", sequenceName = "TRACK_ID_SEQ", allocationSize = 1, initialValue = 1 ) 
        @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "omsTrackIdSeq" )
       private BigDecimal trackId;
    

    public OmsCustOrdTracking() {
    }

    public OmsCustOrdTracking(BigDecimal trackId,Timestamp actualDeliveryDatetime, String awb, String carrier, Timestamp createdDatetime,
                              BigDecimal loc) {
        this.actualDeliveryDatetime = actualDeliveryDatetime;
        this.awb = awb;
        this.carrier = carrier;
        this.createdDatetime = createdDatetime;
        this.loc = loc;
        this.trackId = trackId;
    }

    public Timestamp getActualDeliveryDatetime() {
        return actualDeliveryDatetime;
    }

    public void setActualDeliveryDatetime(Timestamp actualDeliveryDatetime) {
        this.actualDeliveryDatetime = actualDeliveryDatetime;
    }

    public String getAwb() {
        return awb;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public Timestamp getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Timestamp createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public BigDecimal getLoc() {
        return loc;
    }

    public void setLoc(BigDecimal loc) {
        this.loc = loc;
    }

    public BigDecimal getTrackId() {
        return trackId;
    }

    public void setTrackId(BigDecimal trackId) {
        this.trackId = trackId;
    }
}
