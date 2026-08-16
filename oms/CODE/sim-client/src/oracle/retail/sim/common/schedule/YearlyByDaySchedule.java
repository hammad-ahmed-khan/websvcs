package oracle.retail.sim.common.schedule;

import java.util.Date;
import java.util.Set;
import oracle.retail.sim.common.date.SimDateUtil;

public class YearlyByDaySchedule extends Schedule {
  private static final long serialVersionUID = -7438221261794044186L;
  
  public YearlyByDaySchedule() {
    setMonthOfTheYear(Integer.valueOf(1));
    setDayOfTheMonth(Integer.valueOf(1));
  }
  
  public YearlyByDaySchedule(Integer paramInteger1, Integer paramInteger2) {
    setMonthOfTheYear(paramInteger1);
    setDayOfTheMonth(paramInteger2);
  }
  
  public ScheduleType getType() {
    return ScheduleType.YEARLY_BY_DAY;
  }
  
  public void setDaysOfTheMonth(Set<Integer> paramSet) {
    super.setDaysOfTheMonth(paramSet);
  }
  
  public Set getDaysOfTheMonth() {
    return super.getDaysOfTheMonth();
  }
  
  public void setMonthsOfTheYear(Set<Integer> paramSet) {
    super.setMonthsOfTheYear(paramSet);
  }
  
  public Set getMonthsOfTheYear() {
    return super.getMonthsOfTheYear();
  }
  
  public void setDayOfTheMonth(Integer paramInteger) {
    super.setDayOfTheMonth(paramInteger);
  }
  
  public Integer getMonthOfTheYear() {
    return super.getMonthOfTheYear();
  }
  
  public void setMonthOfTheYear(Integer paramInteger) {
    super.setMonthOfTheYear(paramInteger);
  }
  
  public Integer getDayOfTheMonth() {
    return super.getDayOfTheMonth();
  }
  
  protected Date getNextDate(Date paramDate) {
    Date date = (Date)paramDate.clone();
    while (true) {
      if (!isValidMonthOfTheYear(date) || !isValidDayOfTheMonth(date)) {
        if (!isValidMonthOfTheYear(date)) {
          date = SimDateUtil.addMonths(SimDateUtil.getGMTTimeZone(), date, Integer.valueOf(1));
          date = SimDateUtil.resetDateToStartOfMonth(SimDateUtil.getGMTTimeZone(), date);
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
    stringBuilder.append("YearlyByDay Schedule[Start=").append(getStartDate());
    stringBuilder.append(", End=").append(getEndDate());
    stringBuilder.append(", Month=").append(getMonthOfTheYear());
    stringBuilder.append(", Day of Month=").append(getDayOfTheMonth());
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\schedule\YearlyByDaySchedule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */