package oracle.retail.sim.common.warehousedelivery;

import oracle.retail.sim.common.source.SourceType;

public class WarehouseDeliveryRecord {
  private Long id;
  
  private Long storeId;
  
  private String sourceId;
  
  private SourceType sourceType;
  
  private WarehouseDeliveryStatus status;
  
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
  
  public String getSourceId() {
    return this.sourceId;
  }
  
  public void setSourceId(String paramString) {
    this.sourceId = paramString;
  }
  
  public SourceType getSourceType() {
    return this.sourceType;
  }
  
  public void setSourceType(SourceType paramSourceType) {
    this.sourceType = paramSourceType;
  }
  
  public WarehouseDeliveryStatus getStatus() {
    return this.status;
  }
  
  public void setStatus(WarehouseDeliveryStatus paramWarehouseDeliveryStatus) {
    this.status = paramWarehouseDeliveryStatus;
  }
  
  public String getAsnId() {
    return this.asnId;
  }
  
  public void setAsnId(String paramString) {
    this.asnId = paramString;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDeliveryRecord.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */