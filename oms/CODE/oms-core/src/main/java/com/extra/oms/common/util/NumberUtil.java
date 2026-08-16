package com.extra.oms.common.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public final class NumberUtil {

	public static BigDecimal zeroIfNull(BigDecimal value) {
		return value != null ? value : BigDecimal.ZERO;
	}

	public static Date getStartDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		return cal.getTime();
	}

	public static Date getEndDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MILLISECOND, 999);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		return cal.getTime();
	}
}
