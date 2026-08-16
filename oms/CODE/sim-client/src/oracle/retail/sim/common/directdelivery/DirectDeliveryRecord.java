package oracle.retail.sim.common.directdelivery;

public class DirectDeliveryRecord {
  private Long id;
  
  private Long storeId;
  
  private String supplierId;
  
  private DirectDeliveryStatus status;
  
  private Long purchaseOrderId;
  
  private String asnId;
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long paramLong) {
    this.id = paramLong;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void setStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public String getSupplierId() {
    return this.supplierId;
  }
  
  public void setSupplierId(String paramString) {
    this.supplierId = paramString;
  }
  
  public DirectDeliveryStatus getStatus() {
    return this.status;
  }
  
  public void setStatus(DirectDeliveryStatus paramDirectDeliveryStatus) {
    this.status = paramDirectDeliveryStatus;
  }
  
  public Long getPurchaseOrderId() {
    return this.purchaseOrderId;
  }
  
  public void setPurchaseOrderId(Long paramLong) {
    this.purchaseOrderId = paramLong;
  }
  
  public String getAsnId() {
    return this.asnId;
  }
  
  public void setAsnId(String paramString) {
    this.asnId = paramString;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\DirectDeliveryRecord.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */