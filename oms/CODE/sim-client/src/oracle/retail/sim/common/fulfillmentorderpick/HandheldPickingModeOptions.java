package oracle.retail.sim.common.fulfillmentorderpick;

import oracle.retail.sim.common.core.SimEnum;

public enum HandheldPickingModeOptions implements SimEnum<Integer> {
  ENTER_PICK_QUANTITY(0, "Enter Pick Quantity"),
  SCAN_EVERY_ITEM(1, "Scan Every Item");
  
  private final int code;
  
  private final String description;
  
  HandheldPickingModeOptions(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static HandheldPickingModeOptions toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (HandheldPickingModeOptions handheldPickingModeOptions : values()) {
        if (handheldPickingModeOptions.code == paramInteger.intValue())
          return handheldPickingModeOptions; 
      }  
    return null;
  }
  
  public static HandheldPickingModeOptions toValue(String paramString) {
    if (paramString != null)
      for (HandheldPickingModeOptions handheldPickingModeOptions : values()) {
        if (handheldPickingModeOptions.description.equals(paramString))
          return handheldPickingModeOptions; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderpick\HandheldPickingModeOptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */