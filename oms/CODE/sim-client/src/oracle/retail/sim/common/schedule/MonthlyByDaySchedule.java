package oracle.retail.sim.common.schedule;

import java.util.Date;
import java.util.Set;
import oracle.retail.sim.common.date.SimDateUtil;

public class MonthlyByDaySchedule extends Schedule {
  private static final long serialVersionUID = -914556207491840898L;
  
  public MonthlyByDaySchedule() {
    setMonthlyFrequency(1);
    setDayOfTheMonth(Integer.valueOf(1));
  }
  
  public MonthlyByDaySchedule(Integer paramInteger1, Integer paramInteger2) {
    setMonthlyFrequency(paramInteger1.intValue());
    setDayOfTheMonth(paramInteger2);
  }
  
  public ScheduleType getType() {
    return ScheduleType.MONTHLY_BY_DAY;
  }
  
  public void setDayOfTheMonth(Integer paramInteger) {
    super.setDayOfTheMonth(paramInteger);
  }
  
  public Integer getDayOfTheMonth() {
    return super.getDayOfTheMonth();
  }
  
  public void setDaysOfTheMonth(Set<Integer> paramSet) {
    super.setDaysOfTheMonth(paramSet);
  }
  
  public Set<Integer> getDaysOfTheMonth() {
    return super.getDaysOfTheMonth();
  }
  
  public void setMonthlyFrequency(int paramInt) {
    super.setMonthlyFrequency(paramInt);
  }
  
  public Integer getMonthlyFrequency() {
    return super.getMonthlyFrequency();
  }
  
  protected Date getNextDate(Date paramDate) {
    Date date = (Date)getStartDate().clone();
    while (true) {
      if (!date.after(paramDate) || !isValidDayOfTheMonth(date)) {
        if (SimDateUtil.isLastDayOfMonth(SimDateUtil.getGMTTimeZone(), date)) {
          date = SimDateUtil.resetDateToStartOfMonth(SimDateUtil.getGMTTimeZone(), date);
          date = SimDateUtil.addMonths(SimDateUtil.getGMTTimeZone(), date, getMonthlyFrequency());
          continue;
        } 
        date = SimDateUtil.addDays(SimDateUtil.getGMTTimeZone(), date, Integer.valueOf(1));
        continue;
      } 
      return date.after(getEndDate()) ? null : date;
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("MonthlyByDay Schedule: Start(");
    stringBuilder.append(getStartDate());
    stringBuilder.append(") End(");
    stringBuilder.append(getEndDate());
    stringBuilder.append(") Frequency(");
    stringBuilder.append(getMonthlyFrequency());
    stringBuilder.append(") Day of Month(");
    stringBuilder.append(getDayOfTheMonth());
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\schedule\MonthlyByDaySchedule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */