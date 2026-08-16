package oracle.retail.sim.common.fulfillmentorder;

import java.io.Serializable;
import java.util.Date;

public class FulfillmentOrderMgmtListVO implements Serializable {
  private static final long serialVersionUID = -1922351267929270673L;
  
  private String tranId;
  
  private FulfillmentOrderTranType tranType;
  
  private String customerOrderId;
  
  private String fromLocation;
  
  private String fromLocationName;
  
  private String toLocation;
  
  private String toLocationName;
  
  private Date createDate;
  
  private String tranStatus;
  
  private Long totalSKUs;
  
  private String user;
  
  public String getTranId() {
    return this.tranId;
  }
  
  public void doSetTranId(String paramString) {
    this.tranId = paramString;
  }
  
  public FulfillmentOrderTranType getTranType() {
    return this.tranType;
  }
  
  public void doSetTranType(FulfillmentOrderTranType paramFulfillmentOrderTranType) {
    this.tranType = paramFulfillmentOrderTranType;
  }
  
  public String getCustomerOrderId() {
    return this.customerOrderId;
  }
  
  public void doSetCustomerOrderId(String paramString) {
    this.customerOrderId = paramString;
  }
  
  public String getFromLocation() {
    return this.fromLocation;
  }
  
  public void doSetFromLocation(String paramString) {
    this.fromLocation = paramString;
  }
  
  public String getFromLocationName() {
    return this.fromLocationName;
  }
  
  public void doSetFromLocationName(String paramString) {
    this.fromLocationName = paramString;
  }
  
  public String getToLocation() {
    return this.toLocation;
  }
  
  public void doSetToLocation(String paramString) {
    this.toLocation = paramString;
  }
  
  public String getToLocationName() {
    return this.toLocationName;
  }
  
  public void doSetToLocationName(String paramString) {
    this.toLocationName = paramString;
  }
  
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public void doSetCreateDate(Date paramDate) {
    this.createDate = paramDate;
  }
  
  public String getTranStatus() {
    return this.tranStatus;
  }
  
  public void doSetTranStatus(String paramString) {
    this.tranStatus = paramString;
  }
  
  public Long getTotalSKUs() {
    return this.totalSKUs;
  }
  
  public void doSetTotalSKUs(Long paramLong) {
    this.totalSKUs = paramLong;
  }
  
  public String getUser() {
    return this.user;
  }
  
  public void doSetUser(String paramString) {
    this.user = paramString;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\FulfillmentOrderMgmtListVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */