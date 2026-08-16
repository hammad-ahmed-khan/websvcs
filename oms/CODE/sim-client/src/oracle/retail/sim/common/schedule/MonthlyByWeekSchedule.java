package oracle.retail.sim.common.schedule;

import java.util.Date;
import java.util.Set;
import oracle.retail.sim.common.date.SimDateUtil;

public class MonthlyByWeekSchedule extends Schedule {
  private static final long serialVersionUID = -4971121264040985044L;
  
  public MonthlyByWeekSchedule() {
    setMonthlyFrequency(1);
    setWeekOfTheMonth(Integer.valueOf(1));
    setDayOfTheWeek(Integer.valueOf(2));
  }
  
  public MonthlyByWeekSchedule(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3) {
    setMonthlyFrequency(paramInteger1.intValue());
    setWeekOfTheMonth(paramInteger2);
    setDayOfTheWeek(paramInteger3);
  }
  
  public ScheduleType getType() {
    return ScheduleType.MONTHLY_BY_WEEK;
  }
  
  public void setDaysOfTheWeek(Set<Integer> paramSet) {
    super.setDaysOfTheWeek(paramSet);
  }
  
  public void setDayOfTheWeek(Integer paramInteger) {
    super.setDayOfTheWeek(paramInteger);
  }
  
  public void setWeeksOfTheMonth(Set<Integer> paramSet) {
    super.setWeeksOfTheMonth(paramSet);
  }
  
  public void setWeekOfTheMonth(Integer paramInteger) {
    super.setWeekOfTheMonth(paramInteger);
  }
  
  public void setMonthlyFrequency(int paramInt) {
    super.setMonthlyFrequency(paramInt);
  }
  
  public Set getDaysOfTheWeek() {
    return super.getDaysOfTheWeek();
  }
  
  public Integer getDayOfTheWeek() {
    return super.getDayOfTheWeek();
  }
  
  public Set getWeeksOfTheMonth() {
    return super.getWeeksOfTheMonth();
  }
  
  public Integer getWeekOfTheMonth() {
    return super.getWeekOfTheMonth();
  }
  
  public Integer getMonthlyFrequency() {
    return super.getMonthlyFrequency();
  }
  
  protected Date getNextDate(Date paramDate) {
    Date date = (Date)getStartDate().clone();
    while (true) {
      if (date.compareTo(paramDate) < 0 || !isValidDayOfTheWeek(date) || !isMatchingOccurance(date)) {
        boolean bool = SimDateUtil.isLastDayOfMonth(SimDateUtil.getGMTTimeZone(), date);
        int i = SimDateUtil.getMonth(SimDateUtil.getGMTTimeZone(), date);
        int j = SimDateUtil.getMonth(SimDateUtil.getGMTTimeZone(), paramDate);
        if (bool || i < j) {
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
  
  private boolean isMatchingOccurance(Date paramDate) {
    Double double_1 = Double.valueOf(SimDateUtil.getDayOfMonth(SimDateUtil.getGMTTimeZone(), paramDate));
    Double double_2 = Double.valueOf(double_1.doubleValue() / 7.0D);
    if (Math.IEEEremainder(double_1.doubleValue(), 7.0D) != 0.0D)
      double_2 = Double.valueOf(Math.floor(double_2.doubleValue() + 1.0D)); 
    return (double_2.intValue() == getWeekOfTheMonth().intValue());
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("MonthlyByDay Schedule: Start(");
    stringBuilder.append(getStartDate());
    stringBuilder.append(") End(");
    stringBuilder.append(getEndDate());
    stringBuilder.append(") Frequency(");
    stringBuilder.append(getMonthlyFrequency());
    stringBuilder.append(") Day of Week(");
    stringBuilder.append(getDayOfTheWeek());
    stringBuilder.append(") Week(");
    stringBuilder.append(getWeekOfTheMonth());
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\schedule\MonthlyByWeekSchedule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */