package oracle.retail.sim.common.schedule;

import oracle.retail.sim.common.core.SimEnum;

public enum ScheduleStatus implements SimEnum<String> {
  OPEN("O", "Open"),
  CLOSED("C", "Closed");
  
  private final String code;
  
  private final String description;
  
  ScheduleStatus(String paramString1, String paramString2) {
    this.code = paramString1;
    this.description = paramString2;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public String toString() {
    return this.description;
  }
  
  public static ScheduleStatus toValue(String paramString) {
    if (paramString != null)
      for (ScheduleStatus scheduleStatus : values()) {
        if (scheduleStatus.code.equals(paramString))
          return scheduleStatus; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\schedule\ScheduleStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */