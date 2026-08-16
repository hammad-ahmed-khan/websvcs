package oracle.retail.sim.common.deals;

import java.math.BigDecimal;
import java.util.Date;
import oracle.retail.sim.common.business.BusinessObject;

public class Deal extends BusinessObject {
  static final long serialVersionUID = 5776593445595156117L;
  
  private String id;
  
  private String type;
  
  private Date activeDate;
  
  private Date closeDate;
  
  private String detailId;
  
  private String applicationOrder;
  
  private String billingType;
  
  private String dealClass;
  
  private String limitType;
  
  private String limitUOM;
  
  private String valueType;
  
  private String buyItem;
  
  private BigDecimal buyQuantity;
  
  private String getType;
  
  private String getItem;
  
  private BigDecimal getQuantity;
  
  private BigDecimal freeItemUnitCost;
  
  private BigDecimal lowerLimit;
  
  private BigDecimal upperLimit;
  
  private BigDecimal value;
  
  public Date getActiveDate() {
    return this.activeDate;
  }
  
  public void setActiveDate(Date paramDate) {
    this.activeDate = paramDate;
  }
  
  public String getApplicationOrder() {
    return this.applicationOrder;
  }
  
  public void setApplicationOrder(String paramString) {
    this.applicationOrder = paramString;
  }
  
  public String getBillingType() {
    return this.billingType;
  }
  
  public void setBillingType(String paramString) {
    this.billingType = paramString;
  }
  
  public String getBuyItem() {
    return this.buyItem;
  }
  
  public void setBuyItem(String paramString) {
    this.buyItem = paramString;
  }
  
  public BigDecimal getBuyQuantity() {
    return this.buyQuantity;
  }
  
  public void setBuyQuantity(BigDecimal paramBigDecimal) {
    this.buyQuantity = paramBigDecimal;
  }
  
  public Date getCloseDate() {
    return this.closeDate;
  }
  
  public void setCloseDate(Date paramDate) {
    this.closeDate = paramDate;
  }
  
  public String getDealClass() {
    return this.dealClass;
  }
  
  public void setDealClass(String paramString) {
    this.dealClass = paramString;
  }
  
  public String getDetailId() {
    return this.detailId;
  }
  
  public void setDetailId(String paramString) {
    this.detailId = paramString;
  }
  
  public BigDecimal getFreeItemUnitCost() {
    return this.freeItemUnitCost;
  }
  
  public void setFreeItemUnitCost(BigDecimal paramBigDecimal) {
    this.freeItemUnitCost = paramBigDecimal;
  }
  
  public String getGetItem() {
    return this.getItem;
  }
  
  public void setGetItem(String paramString) {
    this.getItem = paramString;
  }
  
  public BigDecimal getGetQuantity() {
    return this.getQuantity;
  }
  
  public void setGetQuantity(BigDecimal paramBigDecimal) {
    this.getQuantity = paramBigDecimal;
  }
  
  public String getGetType() {
    return this.getType;
  }
  
  public void setGetType(String paramString) {
    this.getType = paramString;
  }
  
  public String getId() {
    return this.id;
  }
  
  public void setId(String paramString) {
    this.id = paramString;
  }
  
  public String getLimitType() {
    return this.limitType;
  }
  
  public void setLimitType(String paramString) {
    this.limitType = paramString;
  }
  
  public String getLimitUOM() {
    return this.limitUOM;
  }
  
  public void setLimitUOM(String paramString) {
    this.limitUOM = paramString;
  }
  
  public BigDecimal getLowerLimit() {
    return this.lowerLimit;
  }
  
  public void setLowerLimit(BigDecimal paramBigDecimal) {
    this.lowerLimit = paramBigDecimal;
  }
  
  public String getType() {
    return this.type;
  }
  
  public void setType(String paramString) {
    this.type = paramString;
  }
  
  public BigDecimal getUpperLimit() {
    return this.upperLimit;
  }
  
  public void setUpperLimit(BigDecimal paramBigDecimal) {
    this.upperLimit = paramBigDecimal;
  }
  
  public BigDecimal getValue() {
    return this.value;
  }
  
  public void setValue(BigDecimal paramBigDecimal) {
    this.value = paramBigDecimal;
  }
  
  public String getValueType() {
    return this.valueType;
  }
  
  public void setValueType(String paramString) {
    this.valueType = paramString;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\deals\Deal.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */