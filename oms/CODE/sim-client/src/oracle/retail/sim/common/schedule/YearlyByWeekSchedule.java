package oracle.retail.sim.common.schedule;

import java.util.Date;
import java.util.Set;
import oracle.retail.sim.common.date.SimDateUtil;

public class YearlyByWeekSchedule extends Schedule {
  private static final long serialVersionUID = 6149494086373477689L;
  
  public YearlyByWeekSchedule() {
    setMonthOfTheYear(Integer.valueOf(1));
    setWeekOfTheMonth(Integer.valueOf(1));
    setDayOfTheWeek(Integer.valueOf(2));
  }
  
  public YearlyByWeekSchedule(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3) {
    setMonthOfTheYear(paramInteger1);
    setWeekOfTheMonth(paramInteger2);
    setDayOfTheWeek(paramInteger3);
  }
  
  public ScheduleType getType() {
    return ScheduleType.YEARLY_BY_WEEK;
  }
  
  public void setDaysOfTheWeek(Set<Integer> paramSet) {
    super.setDaysOfTheWeek(paramSet);
  }
  
  public void setWeeksOfTheMonth(Set<Integer> paramSet) {
    super.setWeeksOfTheMonth(paramSet);
  }
  
  public void setMonthsOfTheYear(Set<Integer> paramSet) {
    super.setMonthsOfTheYear(paramSet);
  }
  
  public void setMonthOfTheYear(Integer paramInteger) {
    super.setMonthOfTheYear(paramInteger);
  }
  
  public Set getDaysOfTheWeek() {
    return super.getDaysOfTheWeek();
  }
  
  public Set getWeeksOfTheMonth() {
    return super.getWeeksOfTheMonth();
  }
  
  public Set getMonthsOfTheYear() {
    return super.getMonthsOfTheYear();
  }
  
  public Integer getMonthOfTheYear() {
    return super.getMonthOfTheYear();
  }
  
  public Integer getWeekOfTheMonth() {
    return super.getWeekOfTheMonth();
  }
  
  public Integer getDayOfTheWeek() {
    return super.getDayOfTheWeek();
  }
  
  public void setWeekOfTheMonth(Integer paramInteger) {
    super.setWeekOfTheMonth(paramInteger);
  }
  
  public void setDayOfTheWeek(Integer paramInteger) {
    super.setDayOfTheWeek(paramInteger);
  }
  
  protected Integer getDayOfTheMonth() {
    Set<Integer> set = getDaysOfTheMonth();
    return (set != null) ? (Integer)set.toArray()[0] : null;
  }
  
  protected Date getNextDate(Date paramDate) {
    Date date = (Date)paramDate.clone();
    int i = getDayOfWeekForValidYearAndMonth(paramDate);
    if (i > getDayOfTheWeek().intValue() && getWeekOfTheMonth().intValue() == 1)
      setWeekOfTheMonth(Integer.valueOf(getWeekOfTheMonth().intValue() + 1)); 
    while (true) {
      if (!isValidMonthOfTheYear(date) || !isValidWeekOfTheMonth(date) || !isValidDayOfTheWeek(date)) {
        if (!isValidMonthOfTheYear(date)) {
          date = SimDateUtil.addMonths(SimDateUtil.getGMTTimeZone(), date, Integer.valueOf(1));
          date = SimDateUtil.resetDateToStartOfMonth(SimDateUtil.getGMTTimeZone(), date);
        } else {
          date = SimDateUtil.addDays(SimDateUtil.getGMTTimeZone(), date, Integer.valueOf(1));
        } 
        if (date.after(getEndDate()))
          return null; 
        continue;
      } 
      return date;
    } 
  }
  
  private int getDayOfWeekForValidYearAndMonth(Date paramDate) {
    Date date;
    for (date = (Date)paramDate.clone(); !isValidMonthOfTheYear(date); date = SimDateUtil.resetDateToStartOfMonth(SimDateUtil.getGMTTimeZone(), date))
      date = SimDateUtil.addMonths(SimDateUtil.getGMTTimeZone(), date, Integer.valueOf(1)); 
    return SimDateUtil.getDayOfWeek(SimDateUtil.getGMTTimeZone(), date);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("YearlyByWeek Schedule: Start(");
    stringBuilder.append(getStartDate());
    stringBuilder.append(") buffer.append( End(");
    stringBuilder.append(getEndDate());
    stringBuilder.append(") Month(");
    stringBuilder.append(getMonthOfTheYear());
    stringBuilder.append(") Week(");
    stringBuilder.append(getWeekOfTheMonth());
    stringBuilder.append(") Day Of Week(");
    stringBuilder.append(getDayOfTheWeek());
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\schedule\YearlyByWeekSchedule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */