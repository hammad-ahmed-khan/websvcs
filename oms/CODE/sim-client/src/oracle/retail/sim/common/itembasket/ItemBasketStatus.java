package oracle.retail.sim.common.itembasket;

import oracle.retail.sim.common.core.SimEnum;

public enum ItemBasketStatus implements SimEnum<Integer> {
  ACTIVE(0, "Active"),
  DELETED(1, "Delete"),
  LOCKED(2, "Locked");
  
  private final int code;
  
  private final String description;
  
  ItemBasketStatus(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static ItemBasketStatus toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (ItemBasketStatus itemBasketStatus : values()) {
        if (itemBasketStatus.code == paramInteger.intValue())
          return itemBasketStatus; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\itembasket\ItemBasketStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */