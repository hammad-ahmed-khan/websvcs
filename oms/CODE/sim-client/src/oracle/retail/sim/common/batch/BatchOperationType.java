package oracle.retail.sim.common.batch;

import oracle.retail.sim.common.core.SimEnum;

public enum BatchOperationType implements SimEnum<Integer> {
  IMPORT(1, "Import"),
  EXPORT(2, "Export");
  
  private final int code;
  
  private final String description;
  
  BatchOperationType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static BatchOperationType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (BatchOperationType batchOperationType : values()) {
        if (batchOperationType.code == paramInteger.intValue())
          return batchOperationType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\batch\BatchOperationType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */