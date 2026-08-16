package oracle.retail.sim.common.warehousedelivery;

import java.io.Serializable;

public class WarehouseDeliveryFulfillmentOrderLineItemVO implements Serializable {
  private static final long serialVersionUID = -2441452719576215860L;
  
  private String customerOrderId;
  
  private String fulfillmentOrderExternalId;
  
  private String itemId;
  
  private String preferredUom;
  
  public String getCustomerOrderId() {
    return this.customerOrderId;
  }
  
  public void doSetCustomerOrderId(String paramString) {
    this.customerOrderId = paramString;
  }
  
  public String getFulfillmentOrderExternalId() {
    return this.fulfillmentOrderExternalId;
  }
  
  public void doSetFulfillmentOrderExternalId(String paramString) {
    this.fulfillmentOrderExternalId = paramString;
  }
  
  public String getItemId() {
    return this.itemId;
  }
  
  public void doSetItemId(String paramString) {
    this.itemId = paramString;
  }
  
  public String getPreferredUom() {
    return this.preferredUom;
  }
  
  public void doSetPreferredUom(String paramString) {
    this.preferredUom = paramString;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDeliveryFulfillmentOrderLineItemVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */