package oracle.retail.sim.common.batch;

import oracle.retail.sim.common.core.SimEnum;

public enum BatchImportStatus implements SimEnum<Integer> {
  NEW(0, "New"),
  FILE_TO_STAGE_COMPLETED(1, "File load stage completed."),
  COMPLETED(2, "Import completed."),
  FILE_TO_STAGE_FAILED(3, "Failed to load file into staging table."),
  FAILED(4, "Import failed"),
  RETRY_READY(5, "Retry Ready"),
  COMPLETED_WITH_WARNING(6, "Completed with warning.");
  
  private final int code;
  
  private final String description;
  
  BatchImportStatus(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static BatchImportStatus toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (BatchImportStatus batchImportStatus : values()) {
        if (batchImportStatus.code == paramInteger.intValue())
          return batchImportStatus; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\batch\BatchImportStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */