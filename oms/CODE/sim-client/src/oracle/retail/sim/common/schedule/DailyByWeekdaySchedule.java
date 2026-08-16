package oracle.retail.sim.common.schedule;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import oracle.retail.sim.common.date.SimDateUtil;

public class DailyByWeekdaySchedule extends Schedule {
  private static final long serialVersionUID = -1714081943062787418L;
  
  public DailyByWeekdaySchedule() {
    HashSet<Integer> hashSet = new HashSet(5);
    hashSet.add(Integer.valueOf(2));
    hashSet.add(Integer.valueOf(3));
    hashSet.add(Integer.valueOf(4));
    hashSet.add(Integer.valueOf(5));
    hashSet.add(Integer.valueOf(6));
    setDaysOfTheWeek(hashSet);
    setWeeklyFrequency(1);
  }
  
  public ScheduleType getType() {
    return ScheduleType.DAILY_BY_WEEKDAY;
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
    stringBuilder.append("Daily-By-Weekday Schedule:");
    stringBuilder.append(" Start(").append(getStartDate()).append(")");
    stringBuilder.append(" End(").append(getEndDate()).append(")");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\schedule\DailyByWeekdaySchedule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */