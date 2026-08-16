package oracle.retail.sim.common.schedule;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.date.SimDateUtil;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public abstract class Schedule extends BusinessObject {
  private static final long serialVersionUID = 6018950044759574649L;
  
  private Date startDate;
  
  private Date endDate;
  
  private Set<Integer> daysOfTheWeek;
  
  private Set<Integer> monthsOfTheYear;
  
  private Set<Integer> weeksOfTheMonth;
  
  private Set<Integer> daysOfTheMonth;
  
  private Integer monthlyFrequency;
  
  private Integer dailyFrequency;
  
  private Integer weeklyFrequency;
  
  public abstract ScheduleType getType();
  
  protected abstract Date getNextDate(Date paramDate);
  
  public void setStartDate(TimeZone paramTimeZone, Date paramDate) {
    doSetStartDate(SimDateUtil.convertDateToNoonUTC(paramTimeZone, paramDate));
  }
  
  public void setEndDate(TimeZone paramTimeZone, Date paramDate) {
    doSetEndDate(SimDateUtil.convertDateToNoonUTC(paramTimeZone, paramDate));
  }
  
  public void doSetStartDate(Date paramDate) {
    this.startDate = paramDate;
  }
  
  public void doSetEndDate(Date paramDate) {
    this.endDate = paramDate;
  }
  
  public Date getStartDate() {
    return this.startDate;
  }
  
  public Date getEndDate() {
    return this.endDate;
  }
  
  public Date getNextDate(TimeZone paramTimeZone) {
    return getNextDate(paramTimeZone, SimDateUtil.getCurrentDate());
  }
  
  public Date getPrevDate(TimeZone paramTimeZone) {
    return getPrevDate(paramTimeZone, SimDateUtil.getCurrentDate());
  }
  
  public Date getNextDate(TimeZone paramTimeZone, Date paramDate) {
    if (!isNextDateAvailable(paramTimeZone, paramDate))
      return null; 
    Date date = SimDateUtil.convertDateToNoonUTC(paramTimeZone, paramDate);
    return getNextDate(date);
  }
  
  public Date getPrevDate(TimeZone paramTimeZone, Date paramDate) {
    if (!isPrevDateAvailable(paramTimeZone, paramDate))
      return null; 
    Date date = SimDateUtil.convertDateToNoonUTC(paramTimeZone, paramDate);
    return getPrevDate(date);
  }
  
  private Date getPrevDate(Date paramDate) {
    Date date = (Date)paramDate.clone();
    while (date.compareTo(this.startDate) >= 0) {
      date = SimDateUtil.addDays(SimDateUtil.getGMTTimeZone(), date, Integer.valueOf(-1));
      Date date1 = getNextDate(SimDateUtil.getGMTTimeZone(), date);
      if (date1 != null && date1.before(paramDate))
        return date1; 
    } 
    return null;
  }
  
  public final Date getFirstDate() {
    if (this.startDate != null) {
      Date date = SimDateUtil.addDays(SimDateUtil.getGMTTimeZone(), this.startDate, Integer.valueOf(-1));
      return getNextDate(SimDateUtil.getGMTTimeZone(), date);
    } 
    return null;
  }
  
  public final Date getLastDate() {
    if (this.endDate != null) {
      Date date = SimDateUtil.addDays(SimDateUtil.getGMTTimeZone(), this.endDate, Integer.valueOf(1));
      return getPrevDate(SimDateUtil.getGMTTimeZone(), date);
    } 
    return null;
  }
  
  protected Set<Integer> getWeeksOfTheMonth() {
    return this.weeksOfTheMonth;
  }
  
  protected void setWeeksOfTheMonth(Set<Integer> paramSet) {
    this.weeksOfTheMonth = paramSet;
  }
  
  protected void setMonthlyFrequency(int paramInt) {
    if (paramInt > 0)
      this.monthlyFrequency = Integer.valueOf(paramInt); 
  }
  
  protected Integer getMonthlyFrequency() {
    return this.monthlyFrequency;
  }
  
  protected void setDailyFrequency(int paramInt) {
    if (paramInt > 0)
      this.dailyFrequency = Integer.valueOf(paramInt); 
  }
  
  protected Integer getDailyFrequency() {
    return this.dailyFrequency;
  }
  
  protected void setDaysOfTheWeek(Set<Integer> paramSet) {
    this.daysOfTheWeek = paramSet;
  }
  
  protected boolean isValidDayOfTheWeek(Date paramDate) {
    return this.daysOfTheWeek.contains(Integer.valueOf(SimDateUtil.getDayOfWeek(SimDateUtil.getGMTTimeZone(), paramDate)));
  }
  
  protected boolean isValidWeekOfTheMonth(Date paramDate) {
    return this.weeksOfTheMonth.contains(Integer.valueOf(SimDateUtil.getWeekOfMonth(SimDateUtil.getGMTTimeZone(), paramDate)));
  }
  
  protected Integer getWeeklyFrequency() {
    return this.weeklyFrequency;
  }
  
  protected Set<Integer> getDaysOfTheWeek() {
    return this.daysOfTheWeek;
  }
  
  protected void setWeeklyFrequency(int paramInt) {
    if (paramInt > 0)
      this.weeklyFrequency = Integer.valueOf(paramInt); 
  }
  
  protected boolean isValidMonthOfTheYear(Date paramDate) {
    return this.monthsOfTheYear.contains(Integer.valueOf(SimDateUtil.getMonth(SimDateUtil.getGMTTimeZone(), paramDate)));
  }
  
  protected Set<Integer> getMonthsOfTheYear() {
    return this.monthsOfTheYear;
  }
  
  protected void setMonthsOfTheYear(Set<Integer> paramSet) {
    this.monthsOfTheYear = paramSet;
  }
  
  protected boolean isValidDayOfTheMonth(Date paramDate) {
    return this.daysOfTheMonth.contains(Integer.valueOf(SimDateUtil.getDayOfMonth(SimDateUtil.getGMTTimeZone(), paramDate)));
  }
  
  protected Set<Integer> getDaysOfTheMonth() {
    return this.daysOfTheMonth;
  }
  
  protected void setDaysOfTheMonth(Set<Integer> paramSet) {
    this.daysOfTheMonth = paramSet;
  }
  
  protected void setDayOfTheMonth(Integer paramInteger) {
    HashSet<Integer> hashSet = new HashSet(1);
    hashSet.add(paramInteger);
    setDaysOfTheMonth(hashSet);
  }
  
  protected Integer getMonthOfTheYear() {
    Set<Integer> set = getMonthsOfTheYear();
    return (set != null) ? (Integer)set.toArray()[0] : null;
  }
  
  protected void setMonthOfTheYear(Integer paramInteger) {
    HashSet<Integer> hashSet = new HashSet(1);
    hashSet.add(paramInteger);
    setMonthsOfTheYear(hashSet);
  }
  
  protected Integer getDayOfTheMonth() {
    Set<Integer> set = getDaysOfTheMonth();
    return (set != null) ? (Integer)set.toArray()[0] : null;
  }
  
  protected Integer getWeekOfTheMonth() {
    return (Integer)getWeeksOfTheMonth().toArray()[0];
  }
  
  protected Integer getDayOfTheWeek() {
    return (Integer)getDaysOfTheWeek().toArray()[0];
  }
  
  protected void setWeekOfTheMonth(Integer paramInteger) {
    HashSet<Integer> hashSet = new HashSet(1);
    hashSet.add(paramInteger);
    setWeeksOfTheMonth(hashSet);
  }
  
  protected void setDayOfTheWeek(Integer paramInteger) {
    HashSet<Integer> hashSet = new HashSet(1);
    hashSet.add(paramInteger);
    setDaysOfTheWeek(hashSet);
  }
  
  protected int getCombinedDaysOfTheWeekAsInteger() {
    StringBuilder stringBuilder = new StringBuilder();
    Object[] arrayOfObject = getDaysOfTheWeek().toArray();
    for (Object object : arrayOfObject)
      stringBuilder.append(String.valueOf(object)); 
    return Integer.parseInt(stringBuilder.toString());
  }
  
  protected void setDaysOfTheWeekBitmap(int paramInt) {
    HashSet<Integer> hashSet = new HashSet();
    char[] arrayOfChar = String.valueOf(paramInt).toCharArray();
    for (char c : arrayOfChar)
      hashSet.add(Integer.valueOf(String.valueOf(c))); 
    setDaysOfTheWeek(hashSet);
  }
  
  protected boolean isDayOfTheWeekValid(Integer paramInteger) {
    return getDaysOfTheWeek().contains(paramInteger);
  }
  
  protected boolean isNextDateAvailable(TimeZone paramTimeZone, Date paramDate) {
    if (this.startDate == null || this.endDate == null)
      return false; 
    if (paramTimeZone == null || paramDate == null)
      return false; 
    Date date = SimDateUtil.convertDateToNoonUTC(paramTimeZone, paramDate);
    return !date.after(getEndDate());
  }
  
  protected boolean isPrevDateAvailable(TimeZone paramTimeZone, Date paramDate) {
    if (this.startDate == null || this.endDate == null)
      return false; 
    if (paramTimeZone == null || paramDate == null)
      return false; 
    Date date = SimDateUtil.convertDateToNoonUTC(paramTimeZone, paramDate);
    return !date.before(this.startDate);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    Schedule schedule = (Schedule)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(getStartDate(), schedule.getStartDate());
    equalsBuilder.append(getEndDate(), schedule.getEndDate());
    equalsBuilder.append(getDailyFrequency(), schedule.getDailyFrequency());
    equalsBuilder.append(getWeeklyFrequency(), schedule.getWeeklyFrequency());
    equalsBuilder.append(getMonthlyFrequency(), schedule.getMonthlyFrequency());
    equalsBuilder.append(getDaysOfTheWeek(), schedule.getDaysOfTheWeek());
    equalsBuilder.append(getWeeksOfTheMonth(), schedule.getWeeksOfTheMonth());
    equalsBuilder.append(getMonthsOfTheYear(), schedule.getMonthsOfTheYear());
    equalsBuilder.append(getDaysOfTheMonth(), schedule.getDaysOfTheMonth());
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(getStartDate());
    hashCodeBuilder.append(getEndDate());
    hashCodeBuilder.append(getDailyFrequency());
    hashCodeBuilder.append(getWeeklyFrequency());
    hashCodeBuilder.append(getMonthlyFrequency());
    hashCodeBuilder.append(getDaysOfTheWeek());
    hashCodeBuilder.append(getWeeksOfTheMonth());
    hashCodeBuilder.append(getMonthsOfTheYear());
    hashCodeBuilder.append(getDaysOfTheMonth());
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\schedule\Schedule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */