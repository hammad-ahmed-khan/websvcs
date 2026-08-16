package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries( { @NamedQuery(name = "XxItemCustomInfo.findAll", query = "select o from XxItemCustomInfo o"),
                 @NamedQuery(name = "XxItemCustomInfo.findCourierValue", query = "select o.courier from XxItemCustomInfo o where o.item=:item")})
@Table(name = "XX_ITEM_CUSTOM_INFO")
public class XxItemCustomInfo implements Serializable {
    @Column(name = "CASH_ON_DELIVERY_IND", nullable = false, length = 1)
    private String cashOnDeliveryInd;
    @Column(name = "COLLECT_FROM_STORE", nullable = false, length = 1)
    private String collectFromStore;
    @Column(name = "COMING_SOON_IND", nullable = false, length = 1)
    private String comingSoonInd;
    @Column(length = 1)
    private String courier;
    @Column(name = "DESC_AR", nullable = false, length = 2000)
    private String descAr;
    @Column(name = "HD_FLAG", nullable = false, length = 1)
    private String hdFlag;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_DATETIME")
    private Date lastDatetime;
    @Column(length = 4000)
    private String meta;
    @Column(name = "NAME_AR", nullable = false, length = 100)
    private String nameAr;
    @Column(name = "NAME_EN", nullable = false, length = 100)
    private String nameEn;
    @Column(name = "PICKUP_FLAG", nullable = false, length = 1)
    private String pickupFlag;
    @Column(name = "PRE_ORDER_IND", nullable = false, length = 1)
    private String preOrderInd;
    @Column(name = "SELLABLE_IND", nullable = false, length = 1)
    private String sellableInd;
    @Column(name = "SHOW_BUY_NOW_BUTTON_IND", nullable = false, length = 1)
    private String showBuyNowButtonInd;
    @Column(name = "SOLD_OUT_IND", nullable = false, length = 1)
    private String soldOutInd;
    @Column(name = "UP_DIMENSION_UOM", length = 4)
    private String upDimensionUom;
    @Column(name = "UP_HEIGHT")
    private BigDecimal upHeight;
    @Column(name = "UP_LENGTH")
    private BigDecimal upLength;
    @Column(name = "UP_WEIGHT")
    private BigDecimal upWeight;
    @Column(name = "UP_WEIGHT_UOM", length = 4)
    private String upWeightUom;
    @Column(name = "UP_WIDTH")
    private BigDecimal upWidth;
    @Column(name = "UPS_AR_1", length = 2000)
    private String upsAr1;
    @Column(name = "UPS_EN_1", length = 2000)
    private String upsEn1;
    @Column(name = "VIDEOURL_AR", length = 2000)
    private String videourlAr;
    @Column(name = "VIDEOURL_EN", length = 2000)
    private String videourlEn;
    @Column(name = "WEB_ENABLED", nullable = false, length = 1)
    private String webEnabled;

    public XxItemCustomInfo() {
    }

    public XxItemCustomInfo(String cashOnDeliveryInd, String collectFromStore, String comingSoonInd, String courier,
                            String descAr, String hdFlag, String item, Date lastDatetime, String meta, String nameAr,
                            String nameEn, String pickupFlag, String preOrderInd, String sellableInd,
                            String showBuyNowButtonInd, String soldOutInd, String upDimensionUom, BigDecimal upHeight,
                            BigDecimal upLength, BigDecimal upWeight, String upWeightUom, BigDecimal upWidth,
                            String upsAr1, String upsEn1, String videourlAr, String videourlEn, String webEnabled) {
        this.cashOnDeliveryInd = cashOnDeliveryInd;
        this.collectFromStore = collectFromStore;
        this.comingSoonInd = comingSoonInd;
        this.courier = courier;
        this.descAr = descAr;
        this.hdFlag = hdFlag;
        this.item = item;
        this.lastDatetime = lastDatetime;
        this.meta = meta;
        this.nameAr = nameAr;
        this.nameEn = nameEn;
        this.pickupFlag = pickupFlag;
        this.preOrderInd = preOrderInd;
        this.sellableInd = sellableInd;
        this.showBuyNowButtonInd = showBuyNowButtonInd;
        this.soldOutInd = soldOutInd;
        this.upDimensionUom = upDimensionUom;
        this.upHeight = upHeight;
        this.upLength = upLength;
        this.upWeight = upWeight;
        this.upWeightUom = upWeightUom;
        this.upWidth = upWidth;
        this.upsAr1 = upsAr1;
        this.upsEn1 = upsEn1;
        this.videourlAr = videourlAr;
        this.videourlEn = videourlEn;
        this.webEnabled = webEnabled;
    }

    public String getCashOnDeliveryInd() {
        return cashOnDeliveryInd;
    }

    public void setCashOnDeliveryInd(String cashOnDeliveryInd) {
        this.cashOnDeliveryInd = cashOnDeliveryInd;
    }

    public String getCollectFromStore() {
        return collectFromStore;
    }

    public void setCollectFromStore(String collectFromStore) {
        this.collectFromStore = collectFromStore;
    }

    public String getComingSoonInd() {
        return comingSoonInd;
    }

    public void setComingSoonInd(String comingSoonInd) {
        this.comingSoonInd = comingSoonInd;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getDescAr() {
        return descAr;
    }

    public void setDescAr(String descAr) {
        this.descAr = descAr;
    }

    public String getHdFlag() {
        return hdFlag;
    }

    public void setHdFlag(String hdFlag) {
        this.hdFlag = hdFlag;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Date getLastDatetime() {
        return lastDatetime;
    }

    public void setLastDatetime(Date lastDatetime) {
        this.lastDatetime = lastDatetime;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getPickupFlag() {
        return pickupFlag;
    }

    public void setPickupFlag(String pickupFlag) {
        this.pickupFlag = pickupFlag;
    }

    public String getPreOrderInd() {
        return preOrderInd;
    }

    public void setPreOrderInd(String preOrderInd) {
        this.preOrderInd = preOrderInd;
    }

    public String getSellableInd() {
        return sellableInd;
    }

    public void setSellableInd(String sellableInd) {
        this.sellableInd = sellableInd;
    }

    public String getShowBuyNowButtonInd() {
        return showBuyNowButtonInd;
    }

    public void setShowBuyNowButtonInd(String showBuyNowButtonInd) {
        this.showBuyNowButtonInd = showBuyNowButtonInd;
    }

    public String getSoldOutInd() {
        return soldOutInd;
    }

    public void setSoldOutInd(String soldOutInd) {
        this.soldOutInd = soldOutInd;
    }

    public String getUpDimensionUom() {
        return upDimensionUom;
    }

    public void setUpDimensionUom(String upDimensionUom) {
        this.upDimensionUom = upDimensionUom;
    }

    public BigDecimal getUpHeight() {
        return upHeight;
    }

    public void setUpHeight(BigDecimal upHeight) {
        this.upHeight = upHeight;
    }

    public BigDecimal getUpLength() {
        return upLength;
    }

    public void setUpLength(BigDecimal upLength) {
        this.upLength = upLength;
    }

    public BigDecimal getUpWeight() {
        return upWeight;
    }

    public void setUpWeight(BigDecimal upWeight) {
        this.upWeight = upWeight;
    }

    public String getUpWeightUom() {
        return upWeightUom;
    }

    public void setUpWeightUom(String upWeightUom) {
        this.upWeightUom = upWeightUom;
    }

    public BigDecimal getUpWidth() {
        return upWidth;
    }

    public void setUpWidth(BigDecimal upWidth) {
        this.upWidth = upWidth;
    }

    public String getUpsAr1() {
        return upsAr1;
    }

    public void setUpsAr1(String upsAr1) {
        this.upsAr1 = upsAr1;
    }

    public String getUpsEn1() {
        return upsEn1;
    }

    public void setUpsEn1(String upsEn1) {
        this.upsEn1 = upsEn1;
    }

    public String getVideourlAr() {
        return videourlAr;
    }

    public void setVideourlAr(String videourlAr) {
        this.videourlAr = videourlAr;
    }

    public String getVideourlEn() {
        return videourlEn;
    }

    public void setVideourlEn(String videourlEn) {
        this.videourlEn = videourlEn;
    }

    public String getWebEnabled() {
        return webEnabled;
    }

    public void setWebEnabled(String webEnabled) {
        this.webEnabled = webEnabled;
    }
}
