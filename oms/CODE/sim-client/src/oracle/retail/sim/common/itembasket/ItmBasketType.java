package oracle.retail.sim.common.itembasket;

import oracle.retail.sim.common.core.SimEnum;

public enum ItmBasketType implements SimEnum<Integer> {
  ITEM_BASKET(1, "Item Basket"),
  CUSTOMER_ORDER(2, "Customer Order");
  
  private final int code;
  
  private final String description;
  
  ItmBasketType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static ItmBasketType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (ItmBasketType itmBasketType : values()) {
        if (itmBasketType.code == paramInteger.intValue())
          return itmBasketType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\itembasket\ItmBasketType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */