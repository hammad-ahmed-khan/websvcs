package oracle.retail.sim.common.productgroup;

import oracle.retail.sim.common.core.SimEnum;

public enum ProductGroupStatus implements SimEnum<Integer> {
  ACTIVE(0, "Active"),
  CANCELED(1, "Canceled");
  
  private final int code;
  
  private final String description;
  
  ProductGroupStatus(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static ProductGroupStatus toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (ProductGroupStatus productGroupStatus : values()) {
        if (productGroupStatus.code == paramInteger.intValue())
          return productGroupStatus; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\productgroup\ProductGroupStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */