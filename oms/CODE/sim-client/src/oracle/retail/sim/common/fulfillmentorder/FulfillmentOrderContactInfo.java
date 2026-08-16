package oracle.retail.sim.common.fulfillmentorder;

import java.io.Serializable;

public class FulfillmentOrderContactInfo implements Serializable {
  private static final long serialVersionUID = 2778032888570472194L;
  
  private CustomerAddress billingAddress;
  
  private CustomerAddress deliveryAddress;
  
  public CustomerAddress getBillingAddress() {
    return this.billingAddress;
  }
  
  public void doSetBillingAddress(CustomerAddress paramCustomerAddress) {
    this.billingAddress = paramCustomerAddress;
  }
  
  public CustomerAddress getDeliveryAddress() {
    return this.deliveryAddress;
  }
  
  public void doSetDeliveryAddress(CustomerAddress paramCustomerAddress) {
    this.deliveryAddress = paramCustomerAddress;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\FulfillmentOrderContactInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */