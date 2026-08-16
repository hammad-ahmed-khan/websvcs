package oracle.retail.sim.common.date;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import oracle.retail.sim.common.core.JvmLocation;
import oracle.retail.sim.common.logging.LogService;

public class SimDateUtil {
  public static final String GMT_ID = "GMT";
  
  private static long clientServerOffsetMillis;
  
  private static ThreadLocal<GregorianCalendar> GMT_CAL = new ThreadLocal<GregorianCalendar>() {
      protected synchronized GregorianCalendar initialValue() {
        return new GregorianCalendar(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);
      }
    };
  
  public static TimeZone getGMTTimeZone() {
    return getGMTCalendar().getTimeZone();
  }
  
  public static Calendar getGMTCalendar() {
    return GMT_CAL.get();
  }
  
  private static Calendar getCalendar(TimeZone paramTimeZone) {
    if (paramTimeZone == null)
      return null; 
    if (isGMT(paramTimeZone))
      return getGMTCalendar(); 
    if (LogService.isDebugEnabled(SimDateUtil.class)) {
      String str1 = paramTimeZone.getDisplayName(paramTimeZone.inDaylightTime(getCurrentDate()), 1);
      String str2 = paramTimeZone.getID();
      int i = paramTimeZone.getRawOffset();
      int j = i / 3600000;
      int k = Math.abs(i / 60000) % 60;
      LogService.debug(SimDateUtil.class, "TimeZone Info: " + str2 + ": " + str1 + " " + j + ":" + k);
    } 
    return new GregorianCalendar(paramTimeZone, Locale.ENGLISH);
  }
  
  public static boolean isGMT(TimeZone paramTimeZone) {
    return (paramTimeZone != null && paramTimeZone.getRawOffset() == 0 && !paramTimeZone.useDaylightTime());
  }
  
  public static void setClientServerOffsetMillis(Long paramLong) {
    if (paramLong == null || JvmLocation.isServer()) {
      clientServerOffsetMillis = 0L;
    } else {
      clientServerOffsetMillis = paramLong.longValue() - System.currentTimeMillis();
    } 
  }
  
  public static long getClientServerOffsetMillis() {
    return JvmLocation.isServer() ? 0L : clientServerOffsetMillis;
  }
  
  public static Date getCurrentDate() {
    long l = System.currentTimeMillis() + getClientServerOffsetMillis();
    return new Date(l / 1000L * 1000L);
  }
  
  public static Date addHours(Date paramDate, Integer paramInteger) {
    return (paramDate == null) ? null : new Date(paramDate.getTime() + paramInteger.intValue() * 3600L * 1000L);
  }
  
  public static Date addMinutes(Date paramDate, Integer paramInteger) {
    return (paramDate == null) ? null : new Date(paramDate.getTime() + paramInteger.intValue() * 60L * 1000L);
  }
  
  public static Date addSeconds(Date paramDate, Integer paramInteger) {
    return (paramDate == null) ? null : new Date(paramDate.getTime() + paramInteger.intValue() * 1000L);
  }
  
  public static Date subtractHours(Date paramDate, Integer paramInteger) {
    return (paramDate == null) ? null : new Date(paramDate.getTime() - paramInteger.intValue() * 3600L * 1000L);
  }
  
  @Deprecated
  public static Date getCurrentDateAtNoonUTC() {
    LogService.warn(SimDateUtil.class, "Warning: Using deprecated method SimDateUtil.getCurrentDateAtNoon()!");
    return addHours(getCurrentDateAtStartOfDay(getGMTTimeZone()), Integer.valueOf(12));
  }
  
  public static Date getCurrentDateAtStartOfDay(TimeZone paramTimeZone) {
    return (paramTimeZone == null) ? null : getCurrentDateAtStartOfDay(getCalendar(paramTimeZone));
  }
  
  public static Date getTomorrowAtStartOfDay(TimeZone paramTimeZone) {
    return (paramTimeZone == null) ? null : getTomorrowAtStartOfDay(getCalendar(paramTimeZone));
  }
  
  public static Date getYesterdayAtStartOfDay(TimeZone paramTimeZone) {
    return (paramTimeZone == null) ? null : getYesterdayAtStartOfDay(getCalendar(paramTimeZone));
  }
  
  public static Date addYears(TimeZone paramTimeZone, Date paramDate, Integer paramInteger) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? null : addYears(getCalendar(paramTimeZone), paramDate, paramInteger);
  }
  
  public static Date addMonths(TimeZone paramTimeZone, Date paramDate, Integer paramInteger) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? null : addMonths(getCalendar(paramTimeZone), paramDate, paramInteger);
  }
  
  public static Date addWeeks(TimeZone paramTimeZone, Date paramDate, Integer paramInteger) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? null : addWeeks(getCalendar(paramTimeZone), paramDate, paramInteger);
  }
  
  public static Date addDays(TimeZone paramTimeZone, Date paramDate, Integer paramInteger) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? null : addDays(getCalendar(paramTimeZone), paramDate, paramInteger);
  }
  
  public static int getYear(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? -1 : getYear(getCalendar(paramTimeZone), paramDate);
  }
  
  public static int getMonth(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? -1 : getMonth(getCalendar(paramTimeZone), paramDate);
  }
  
  public static int getWeekOfMonth(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? -1 : getWeekOfMonth(getCalendar(paramTimeZone), paramDate);
  }
  
  public static int getDayOfMonth(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? -1 : getDayOfMonth(getCalendar(paramTimeZone), paramDate);
  }
  
  public static int getDayOfWeek(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? -1 : getDayOfWeek(getCalendar(paramTimeZone), paramDate);
  }
  
  public static int getHourOfDay(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? -1 : getHourOfDay(getCalendar(paramTimeZone), paramDate);
  }
  
  public static int getMinutes(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? -1 : getMinutes(getCalendar(paramTimeZone), paramDate);
  }
  
  public static int getSeconds(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? -1 : getSeconds(getCalendar(paramTimeZone), paramDate);
  }
  
  public static boolean isSunday(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? false : ((getDayOfWeek(getCalendar(paramTimeZone), paramDate) == 1));
  }
  
  public static boolean isMonday(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? false : ((getDayOfWeek(getCalendar(paramTimeZone), paramDate) == 2));
  }
  
  public static boolean isTuesday(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? false : ((getDayOfWeek(getCalendar(paramTimeZone), paramDate) == 3));
  }
  
  public static boolean isWednesday(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? false : ((getDayOfWeek(getCalendar(paramTimeZone), paramDate) == 4));
  }
  
  public static boolean isThursday(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? false : ((getDayOfWeek(getCalendar(paramTimeZone), paramDate) == 5));
  }
  
  public static boolean isFriday(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? false : ((getDayOfWeek(getCalendar(paramTimeZone), paramDate) == 6));
  }
  
  public static boolean isSaturday(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? false : ((getDayOfWeek(getCalendar(paramTimeZone), paramDate) == 7));
  }
  
  public static boolean isSameDay(TimeZone paramTimeZone, Date paramDate1, Date paramDate2) {
    return isNull(new Object[] { paramTimeZone, paramDate1, paramDate2 }) ? false : isSameDay(getCalendar(paramTimeZone), paramDate1, paramDate2);
  }
  
  public static Date getDate(TimeZone paramTimeZone, Integer paramInteger1, Integer paramInteger2, Integer paramInteger3) {
    return (paramTimeZone == null) ? null : getDate(getCalendar(paramTimeZone), paramInteger1, paramInteger2, paramInteger3, Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
  }
  
  public static Date getDate(TimeZone paramTimeZone, Integer paramInteger1, Integer paramInteger2, Integer paramInteger3, Integer paramInteger4, Integer paramInteger5, Integer paramInteger6) {
    return (paramTimeZone == null) ? null : getDate(getCalendar(paramTimeZone), paramInteger1, paramInteger2, paramInteger3, paramInteger4, paramInteger5, paramInteger6);
  }
  
  public static Date getDateAtStartOfDay(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? null : getDateAtStartOfDay(getCalendar(paramTimeZone), paramDate);
  }
  
  public static Date getDateAtEndOfDay(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? null : getDateAtEndOfDay(getCalendar(paramTimeZone), paramDate);
  }
  
  public static Date resetDateToStartOfMonth(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? null : resetDateToStartOfMonth(getCalendar(paramTimeZone), paramDate);
  }
  
  public static boolean isLastDayOfMonth(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? false : isLastDayOfMonth(getCalendar(paramTimeZone), paramDate);
  }
  
  public static boolean isLastDayOfWeek(TimeZone paramTimeZone, Date paramDate) {
    return isNull(new Object[] { paramTimeZone, paramDate }) ? false : isLastDayOfWeek(getCalendar(paramTimeZone), paramDate);
  }
  
  public static Date convertDateFromUTC(TimeZone paramTimeZone, Date paramDate) {
    if (isNull(new Object[] { paramTimeZone, paramDate }))
      return null; 
    Calendar calendar = getGMTCalendar();
    Integer integer1 = Integer.valueOf(getYear(calendar, paramDate));
    Integer integer2 = Integer.valueOf(getMonth(calendar, paramDate));
    Integer integer3 = Integer.valueOf(getDayOfMonth(calendar, paramDate));
    return getDate(paramTimeZone, integer1, integer2, integer3);
  }
  
  public static Date convertDateToNoonUTC(TimeZone paramTimeZone, Date paramDate) {
    if (isNull(new Object[] { paramTimeZone, paramDate }))
      return null; 
    Calendar calendar = getCalendar(paramTimeZone);
    Integer integer1 = Integer.valueOf(getYear(calendar, paramDate));
    Integer integer2 = Integer.valueOf(getMonth(calendar, paramDate));
    Integer integer3 = Integer.valueOf(getDayOfMonth(calendar, paramDate));
    return getDate(getGMTTimeZone(), integer1, integer2, integer3, Integer.valueOf(12), Integer.valueOf(0), Integer.valueOf(0));
  }
  
  protected static Date getCurrentDateAtStartOfDay(Calendar paramCalendar) {
    return (paramCalendar == null) ? null : getDateAtStartOfDay(paramCalendar, getCurrentDate());
  }
  
  protected static Date getTomorrowAtStartOfDay(Calendar paramCalendar) {
    return (paramCalendar == null) ? null : addDays(paramCalendar, getCurrentDateAtStartOfDay(paramCalendar), Integer.valueOf(1));
  }
  
  protected static Date getYesterdayAtStartOfDay(Calendar paramCalendar) {
    return (paramCalendar == null) ? null : addDays(paramCalendar, getCurrentDateAtStartOfDay(paramCalendar), Integer.valueOf(-1));
  }
  
  protected static Date addYears(Calendar paramCalendar, Date paramDate, Integer paramInteger) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return null; 
    paramCalendar.setTime(paramDate);
    paramCalendar.add(1, paramInteger.intValue());
    return paramCalendar.getTime();
  }
  
  protected static Date addMonths(Calendar paramCalendar, Date paramDate, Integer paramInteger) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return null; 
    paramCalendar.setTime(paramDate);
    paramCalendar.add(2, paramInteger.intValue());
    return paramCalendar.getTime();
  }
  
  protected static Date addWeeks(Calendar paramCalendar, Date paramDate, Integer paramInteger) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return null; 
    paramCalendar.setTime(paramDate);
    paramCalendar.add(3, paramInteger.intValue());
    return paramCalendar.getTime();
  }
  
  protected static Date addDays(Calendar paramCalendar, Date paramDate, Integer paramInteger) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return null; 
    paramCalendar.setTime(paramDate);
    paramCalendar.add(6, paramInteger.intValue());
    return paramCalendar.getTime();
  }
  
  protected static int getYear(Calendar paramCalendar, Date paramDate) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return -1; 
    paramCalendar.setTime(paramDate);
    return paramCalendar.get(1);
  }
  
  protected static int getWeekOfMonth(Calendar paramCalendar, Date paramDate) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return -1; 
    paramCalendar.setTime(paramDate);
    return paramCalendar.get(4);
  }
  
  protected static int getDayOfMonth(Calendar paramCalendar, Date paramDate) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return -1; 
    paramCalendar.setTime(paramDate);
    return paramCalendar.get(5);
  }
  
  protected static int getDayOfWeek(Calendar paramCalendar, Date paramDate) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return -1; 
    paramCalendar.setTime(paramDate);
    return paramCalendar.get(7);
  }
  
  protected static int getHourOfDay(Calendar paramCalendar, Date paramDate) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return -1; 
    paramCalendar.setTime(paramDate);
    return paramCalendar.get(11);
  }
  
  protected static int getMinutes(Calendar paramCalendar, Date paramDate) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return -1; 
    paramCalendar.setTime(paramDate);
    return paramCalendar.get(12);
  }
  
  protected static int getSeconds(Calendar paramCalendar, Date paramDate) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return -1; 
    paramCalendar.setTime(paramDate);
    return paramCalendar.get(13);
  }
  
  protected static boolean isSunday(Calendar paramCalendar, Date paramDate) {
    return isNull(new Object[] { paramCalendar, paramDate }) ? false : ((getDayOfWeek(paramCalendar, paramDate) == 1));
  }
  
  protected static boolean isMonday(Calendar paramCalendar, Date paramDate) {
    return isNull(new Object[] { paramCalendar, paramDate }) ? false : ((getDayOfWeek(paramCalendar, paramDate) == 2));
  }
  
  protected static boolean isTuesday(Calendar paramCalendar, Date paramDate) {
    return isNull(new Object[] { paramCalendar, paramDate }) ? false : ((getDayOfWeek(paramCalendar, paramDate) == 3));
  }
  
  protected static boolean isWednesday(Calendar paramCalendar, Date paramDate) {
    return isNull(new Object[] { paramCalendar, paramDate }) ? false : ((getDayOfWeek(paramCalendar, paramDate) == 4));
  }
  
  protected static boolean isThursday(Calendar paramCalendar, Date paramDate) {
    return isNull(new Object[] { paramCalendar, paramDate }) ? false : ((getDayOfWeek(paramCalendar, paramDate) == 5));
  }
  
  protected static boolean isFriday(Calendar paramCalendar, Date paramDate) {
    return isNull(new Object[] { paramCalendar, paramDate }) ? false : ((getDayOfWeek(paramCalendar, paramDate) == 6));
  }
  
  protected static boolean isSaturday(Calendar paramCalendar, Date paramDate) {
    return isNull(new Object[] { paramCalendar, paramDate }) ? false : ((getDayOfWeek(paramCalendar, paramDate) == 7));
  }
  
  protected static boolean isSameDay(Calendar paramCalendar, Date paramDate1, Date paramDate2) {
    if (isNull(new Object[] { paramCalendar, paramDate1, paramDate2 }))
      return false; 
    boolean bool1 = (getYear(paramCalendar, paramDate1) == getYear(paramCalendar, paramDate2)) ? true : false;
    boolean bool2 = (getMonth(paramCalendar, paramDate1) == getMonth(paramCalendar, paramDate2)) ? true : false;
    boolean bool3 = (getDayOfMonth(paramCalendar, paramDate1) == getDayOfMonth(paramCalendar, paramDate2)) ? true : false;
    return (bool1 && bool2 && bool3);
  }
  
  protected static Date getDate(Calendar paramCalendar, Integer paramInteger1, Integer paramInteger2, Integer paramInteger3) {
    return (paramCalendar == null) ? null : getDate(paramCalendar, paramInteger1, paramInteger2, paramInteger3, Integer.valueOf(12), Integer.valueOf(0), Integer.valueOf(0));
  }
  
  protected static Date getDate(Calendar paramCalendar, Integer paramInteger1, Integer paramInteger2, Integer paramInteger3, Integer paramInteger4, Integer paramInteger5, Integer paramInteger6) {
    if (paramCalendar == null)
      return null; 
    paramCalendar.set(1, paramInteger1.intValue());
    paramCalendar.set(2, paramInteger2.intValue());
    paramCalendar.set(5, paramInteger3.intValue());
    paramCalendar.set(11, paramInteger4.intValue());
    paramCalendar.set(12, paramInteger5.intValue());
    paramCalendar.set(13, paramInteger6.intValue());
    paramCalendar.set(14, 0);
    sanityCheck(paramCalendar, paramInteger1, paramInteger2, paramInteger3, paramInteger4, paramInteger5, paramInteger6);
    return paramCalendar.getTime();
  }
  
  protected static Date getDateAtStartOfDay(Calendar paramCalendar, Date paramDate) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return null; 
    paramCalendar.setTime(paramDate);
    paramCalendar.set(11, 0);
    paramCalendar.set(12, 0);
    paramCalendar.set(13, 0);
    paramCalendar.set(14, 0);
    return paramCalendar.getTime();
  }
  
  protected static Date getDateAtEndOfDay(Calendar paramCalendar, Date paramDate) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return null; 
    paramCalendar.setTime(paramDate);
    paramCalendar.set(11, 23);
    paramCalendar.set(12, 59);
    paramCalendar.set(13, 59);
    paramCalendar.set(14, 999);
    return paramCalendar.getTime();
  }
  
  protected static boolean isLastDayOfMonth(Calendar paramCalendar, Date paramDate) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return false; 
    paramCalendar.setTime(paramDate);
    return (paramCalendar.getActualMaximum(5) == paramCalendar.get(5));
  }
  
  protected static Date resetDateToStartOfMonth(Calendar paramCalendar, Date paramDate) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return null; 
    paramCalendar.setTime(paramDate);
    paramCalendar.set(5, 1);
    return paramCalendar.getTime();
  }
  
  protected static int getMonth(Calendar paramCalendar, Date paramDate) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return -1; 
    paramCalendar.setTime(paramDate);
    return paramCalendar.get(2);
  }
  
  protected static boolean isLastDayOfWeek(Calendar paramCalendar, Date paramDate) {
    if (isNull(new Object[] { paramCalendar, paramDate }))
      return false; 
    paramCalendar.setTime(paramDate);
    return (paramCalendar.get(7) == 7);
  }
  
  public static boolean isValidDayOfMonth(int paramInt1, int paramInt2) {
    Calendar calendar = getGMTCalendar();
    calendar.set(2, paramInt1);
    int i = calendar.getActualMaximum(5);
    return (paramInt2 >= 1 && paramInt2 <= i);
  }
  
  private static void sanityCheck(Calendar paramCalendar, Integer paramInteger1, Integer paramInteger2, Integer paramInteger3, Integer paramInteger4, Integer paramInteger5, Integer paramInteger6) {
    checkValueRange("year", paramInteger1, Integer.valueOf(paramCalendar.getActualMinimum(1)), Integer.valueOf(paramCalendar.getActualMaximum(1)));
    checkValueRange("month", paramInteger2, Integer.valueOf(paramCalendar.getActualMinimum(2)), Integer.valueOf(paramCalendar.getActualMaximum(2)));
    checkValueRange("dayOfMonth", paramInteger3, Integer.valueOf(paramCalendar.getActualMinimum(5)), Integer.valueOf(paramCalendar.getActualMaximum(5)));
    checkValueRange("hourOfDay", paramInteger4, Integer.valueOf(paramCalendar.getActualMinimum(11)), Integer.valueOf(paramCalendar.getActualMaximum(11)));
    checkValueRange("minute", paramInteger5, Integer.valueOf(paramCalendar.getActualMinimum(12)), Integer.valueOf(paramCalendar.getActualMaximum(12)));
    checkValueRange("second", paramInteger6, Integer.valueOf(paramCalendar.getActualMinimum(13)), Integer.valueOf(paramCalendar.getActualMaximum(13)));
  }
  
  private static void checkValueRange(String paramString, Integer paramInteger1, Integer paramInteger2, Integer paramInteger3) {
    if (paramInteger1.intValue() < paramInteger2.intValue() || paramInteger1.intValue() > paramInteger3.intValue())
      LogService.error(SimDateUtil.class, paramString + " is invalid: " + paramInteger1 + " outside range [" + paramInteger2 + " - " + paramInteger3 + "]"); 
  }
  
  private static boolean isNull(Object... paramVarArgs) {
    for (Object object : paramVarArgs) {
      if (object == null)
        return true; 
    } 
    return false;
  }
  
  public static boolean isValidDateRange(Date paramDate1, Date paramDate2) {
    return (paramDate1 == null || paramDate2 == null || paramDate1.compareTo(paramDate2) <= 0);
  }
  
  public static boolean isValidDateRange(Date paramDate1, Date paramDate2, boolean paramBoolean) {
    if (paramDate1 != null && paramDate2 != null) {
      int i = paramDate1.compareTo(paramDate2);
      if (i == 0)
        return paramBoolean; 
      if (i > 0)
        return false; 
    } 
    return true;
  }
  
  public static int getDaysBetweenDates(TimeZone paramTimeZone, Date paramDate1, Date paramDate2) {
    if (isNull(new Object[] { paramTimeZone, paramDate1, paramDate2 }))
      return 0; 
    Calendar calendar = getCalendar(paramTimeZone);
    return (int)((getDateAtStartOfDay(calendar, paramDate2).getTime() - getDateAtStartOfDay(calendar, paramDate1).getTime()) / 86400000L);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\date\SimDateUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */