package oracle.retail.sim.common.fulfillmentorder;

import oracle.retail.sim.common.core.SimEnum;

public enum FulfillmentOrderTranType implements SimEnum<Integer> {
  DIRECT_DELIVERY(1, "Direct Delivery"),
  WAREHOUSE_DELIVERY(2, "Warehouse Delivery"),
  TRANSFER(3, "Transfer"),
  RETURN(4, "Return"),
  CUSTOMER_ORDER(5, "Customer Order"),
  PICK(6, "Pick"),
  CUSTOMER_ORDER_DELIVERY(7, "Delivery"),
  REVERSE_PICK(8, "Reverse Pick");
  
  private final int code;
  
  private final String description;
  
  FulfillmentOrderTranType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static FulfillmentOrderTranType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (FulfillmentOrderTranType fulfillmentOrderTranType : values()) {
        if (fulfillmentOrderTranType.code == paramInteger.intValue())
          return fulfillmentOrderTranType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\FulfillmentOrderTranType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */