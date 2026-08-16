package oracle.retail.sim.common.schedule;

import oracle.retail.sim.common.core.SimEnum;

public enum ScheduleType implements SimEnum<Integer> {
  NONE(0),
  DAILY(1),
  DAILY_BY_WEEKDAY(2),
  WEEKLY(3),
  MONTHLY_BY_DAY(4),
  MONTHLY_BY_WEEK(5),
  YEARLY_BY_DAY(6),
  YEARLY_BY_WEEK(7);
  
  private final int code;
  
  ScheduleType(int paramInt1) {
    this.code = paramInt1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public static ScheduleType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (ScheduleType scheduleType : values()) {
        if (scheduleType.code == paramInteger.intValue())
          return scheduleType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\schedule\ScheduleType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */