package oracle.retail.sim.common.schedule;

import java.util.Date;
import java.util.Set;
import oracle.retail.sim.common.date.SimDateUtil;

public class WeeklySchedule extends Schedule {
  private static final long serialVersionUID = 1003641292848592427L;
  
  public WeeklySchedule() {
    setWeeklyFrequency(1);
    setDayOfTheWeek(Integer.valueOf(2));
  }
  
  public WeeklySchedule(Integer paramInteger, Set<Integer> paramSet) {
    setWeeklyFrequency(paramInteger.intValue());
    setDaysOfTheWeek(paramSet);
  }
  
  public WeeklySchedule(Integer paramInteger1, Integer paramInteger2) {
    setWeeklyFrequency(paramInteger1.intValue());
    setDaysOfTheWeekBitmap(paramInteger2.intValue());
  }
  
  public ScheduleType getType() {
    return ScheduleType.WEEKLY;
  }
  
  public void setDaysOfTheWeek(Set<Integer> paramSet) {
    super.setDaysOfTheWeek(paramSet);
  }
  
  public Set<Integer> getDaysOfTheWeek() {
    return super.getDaysOfTheWeek();
  }
  
  public Integer getDayOfTheWeek() {
    return super.getDayOfTheWeek();
  }
  
  public void setDayOfTheWeek(Integer paramInteger) {
    super.setDayOfTheWeek(paramInteger);
  }
  
  public void setWeeklyFrequency(int paramInt) {
    super.setWeeklyFrequency(paramInt);
  }
  
  public Integer getWeeklyFrequency() {
    return super.getWeeklyFrequency();
  }
  
  public int getCombinedDaysOfTheWeekAsInteger() {
    return super.getCombinedDaysOfTheWeekAsInteger();
  }
  
  public void setDaysOfTheWeekBitmap(int paramInt) {
    super.setDaysOfTheWeekBitmap(paramInt);
  }
  
  public boolean isDayOfTheWeekValid(Integer paramInteger) {
    return super.isDayOfTheWeekValid(paramInteger);
  }
  
  protected Date getNextDate(Date paramDate) {
    Date date = (Date)getStartDate().clone();
    while (!isValidDayOfTheWeek(date)) {
      date = SimDateUtil.addDays(SimDateUtil.getGMTTimeZone(), date, Integer.valueOf(1));
      if (date.after(getEndDate()))
        return null; 
    } 
    while (date.before(paramDate)) {
      if (SimDateUtil.isLastDayOfWeek(SimDateUtil.getGMTTimeZone(), date))
        date = SimDateUtil.addWeeks(SimDateUtil.getGMTTimeZone(), date, Integer.valueOf(getWeeklyFrequency().intValue() - 1)); 
      for (date = SimDateUtil.addDays(SimDateUtil.getGMTTimeZone(), date, Integer.valueOf(1)); !isValidDayOfTheWeek(date); date = SimDateUtil.addDays(SimDateUtil.getGMTTimeZone(), date, Integer.valueOf(1))) {
        if (SimDateUtil.isLastDayOfWeek(SimDateUtil.getGMTTimeZone(), date))
          date = SimDateUtil.addWeeks(SimDateUtil.getGMTTimeZone(), date, Integer.valueOf(getWeeklyFrequency().intValue() - 1)); 
      } 
    } 
    return date.after(getEndDate()) ? null : date;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Weekly Schedule: Start(");
    stringBuilder.append(getStartDate());
    stringBuilder.append(") End(");
    stringBuilder.append(getEndDate());
    stringBuilder.append(") Frequency(");
    stringBuilder.append(getWeeklyFrequency());
    stringBuilder.append(") Days of Week(");
    stringBuilder.append(getDaysOfTheWeek());
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\schedule\WeeklySchedule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */