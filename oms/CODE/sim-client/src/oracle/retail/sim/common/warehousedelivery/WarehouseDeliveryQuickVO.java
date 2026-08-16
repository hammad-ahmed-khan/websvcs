package oracle.retail.sim.common.warehousedelivery;

import java.io.Serializable;

public class WarehouseDeliveryQuickVO implements Serializable {
  private static final long serialVersionUID = -300969318108589827L;
  
  private Long id;
  
  private Long storeId;
  
  private String asnId;
  
  private boolean fulfillmentOrderRelated;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public String getAsnId() {
    return this.asnId;
  }
  
  public void doSetAsnId(String paramString) {
    this.asnId = paramString;
  }
  
  public boolean isFulfillmentOrderRelated() {
    return this.fulfillmentOrderRelated;
  }
  
  public void doSetFulfillmentOrderRelated(boolean paramBoolean) {
    this.fulfillmentOrderRelated = paramBoolean;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDeliveryQuickVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */