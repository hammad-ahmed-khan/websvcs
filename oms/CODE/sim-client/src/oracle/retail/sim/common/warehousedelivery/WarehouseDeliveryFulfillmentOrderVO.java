package oracle.retail.sim.common.warehousedelivery;

import java.io.Serializable;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;

public class WarehouseDeliveryFulfillmentOrderVO implements Serializable {
  private static final long serialVersionUID = -2441452719576215860L;
  
  private String customerOrderId;
  
  private String fulfillmentOrderExternalId;
  
  private FulfillmentOrderStatus status;
  
  private String comments;
  
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
  
  public FulfillmentOrderStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(FulfillmentOrderStatus paramFulfillmentOrderStatus) {
    this.status = paramFulfillmentOrderStatus;
  }
  
  public String getComments() {
    return this.comments;
  }
  
  public void doSetComments(String paramString) {
    this.comments = paramString;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDeliveryFulfillmentOrderVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */