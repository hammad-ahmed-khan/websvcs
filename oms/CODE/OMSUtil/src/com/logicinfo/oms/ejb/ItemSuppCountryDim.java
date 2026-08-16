package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "ItemSuppCountryDim.findAll", query = "select o from ItemSuppCountryDim o"),
                @NamedQuery(name = "ItemSuppCountryDim.findByItemId", query = "select o from ItemSuppCountryDim o where o.item=:item")
             })

@Table(name = "ITEM_SUPP_COUNTRY_DIM")
@IdClass(ItemSuppCountryDimPK.class)
public class ItemSuppCountryDim implements Serializable {
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Date createDatetime;
    @Column(name = "CREATE_ID", nullable = false, length = 30)
    private String createId;
    @Id
    @Column(name = "DIM_OBJECT", nullable = false, length = 6)
    private String dimObject;
    private BigDecimal height;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_UPDATE_DATETIME", nullable = false)
    private Date lastUpdateDatetime;
    @Column(name = "LAST_UPDATE_ID", nullable = false, length = 30)
    private String lastUpdateId;
    private BigDecimal length;
    @Column(name = "LIQUID_VOLUME")
    private BigDecimal liquidVolume;
    @Column(name = "LIQUID_VOLUME_UOM", length = 4)
    private String liquidVolumeUom;
    @Column(name = "LWH_UOM", length = 4)
    private String lwhUom;
    @Column(name = "NET_WEIGHT")
    private BigDecimal netWeight;
    @Id
    @Column(name = "ORIGIN_COUNTRY", nullable = false, length = 3)
    private String originCountry;
    @Column(name = "PRESENTATION_METHOD", length = 6)
    private String presentationMethod;
    @Column(name = "STAT_CUBE")
    private BigDecimal statCube;
    @Id
    @Column(nullable = false)
    private BigDecimal supplier;
    @Column(name = "TARE_TYPE", length = 6)
    private String tareType;
    @Column(name = "TARE_WEIGHT")
    private BigDecimal tareWeight;
    private BigDecimal weight;
    @Column(name = "WEIGHT_UOM", length = 4)
    private String weightUom;
    private BigDecimal width;

    public ItemSuppCountryDim() {
    }

    public ItemSuppCountryDim(Date createDatetime, String createId, String dimObject, BigDecimal height, String item,
                              Date lastUpdateDatetime, String lastUpdateId, BigDecimal length, BigDecimal liquidVolume,
                              String liquidVolumeUom, String lwhUom, BigDecimal netWeight, String originCountry,
                              String presentationMethod, BigDecimal statCube, BigDecimal supplier, String tareType,
                              BigDecimal tareWeight, BigDecimal weight, String weightUom, BigDecimal width) {
        this.createDatetime = createDatetime;
        this.createId = createId;
        this.dimObject = dimObject;
        this.height = height;
        this.item = item;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.lastUpdateId = lastUpdateId;
        this.length = length;
        this.liquidVolume = liquidVolume;
        this.liquidVolumeUom = liquidVolumeUom;
        this.lwhUom = lwhUom;
        this.netWeight = netWeight;
        this.originCountry = originCountry;
        this.presentationMethod = presentationMethod;
        this.statCube = statCube;
        this.supplier = supplier;
        this.tareType = tareType;
        this.tareWeight = tareWeight;
        this.weight = weight;
        this.weightUom = weightUom;
        this.width = width;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getDimObject() {
        return dimObject;
    }

    public void setDimObject(String dimObject) {
        this.dimObject = dimObject;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Date getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(Date lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public String getLastUpdateId() {
        return lastUpdateId;
    }

    public void setLastUpdateId(String lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getLiquidVolume() {
        return liquidVolume;
    }

    public void setLiquidVolume(BigDecimal liquidVolume) {
        this.liquidVolume = liquidVolume;
    }

    public String getLiquidVolumeUom() {
        return liquidVolumeUom;
    }

    public void setLiquidVolumeUom(String liquidVolumeUom) {
        this.liquidVolumeUom = liquidVolumeUom;
    }

    public String getLwhUom() {
        return lwhUom;
    }

    public void setLwhUom(String lwhUom) {
        this.lwhUom = lwhUom;
    }

    public BigDecimal getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(BigDecimal netWeight) {
        this.netWeight = netWeight;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getPresentationMethod() {
        return presentationMethod;
    }

    public void setPresentationMethod(String presentationMethod) {
        this.presentationMethod = presentationMethod;
    }

    public BigDecimal getStatCube() {
        return statCube;
    }

    public void setStatCube(BigDecimal statCube) {
        this.statCube = statCube;
    }

    public BigDecimal getSupplier() {
        return supplier;
    }

    public void setSupplier(BigDecimal supplier) {
        this.supplier = supplier;
    }

    public String getTareType() {
        return tareType;
    }

    public void setTareType(String tareType) {
        this.tareType = tareType;
    }

    public BigDecimal getTareWeight() {
        return tareWeight;
    }

    public void setTareWeight(BigDecimal tareWeight) {
        this.tareWeight = tareWeight;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getWeightUom() {
        return weightUom;
    }

    public void setWeightUom(String weightUom) {
        this.weightUom = weightUom;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }
}
