package oracle.retail.sim.common.schedule;

import java.util.Date;
import oracle.retail.sim.common.date.SimDateUtil;

public class DailySchedule extends Schedule {
  private static final long serialVersionUID = -2910466828936294680L;
  
  public DailySchedule() {
    setDailyFrequency(1);
  }
  
  public DailySchedule(Integer paramInteger) {
    setDailyFrequency(paramInteger.intValue());
  }
  
  public ScheduleType getType() {
    return ScheduleType.DAILY;
  }
  
  public void setDailyFrequency(int paramInt) {
    super.setDailyFrequency(paramInt);
  }
  
  public Integer getDailyFrequency() {
    return super.getDailyFrequency();
  }
  
  protected Date getNextDate(Date paramDate) {
    if (paramDate.compareTo(getStartDate()) <= 0)
      return getStartDate(); 
    Date date;
    for (date = (Date)getStartDate().clone(); date.before(paramDate); date = SimDateUtil.addDays(SimDateUtil.getGMTTimeZone(), date, getDailyFrequency()));
    return date.after(getEndDate()) ? null : date;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Daily Schedule [StartDate=").append(getStartDate());
    stringBuilder.append(", End=").append(getEndDate());
    stringBuilder.append(", Frequency=").append(getDailyFrequency());
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\schedule\DailySchedule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */