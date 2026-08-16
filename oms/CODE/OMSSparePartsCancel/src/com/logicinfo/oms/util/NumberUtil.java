/**
 * 
 */
package com.logicinfo.oms.util;

import java.math.BigDecimal;

/**
 * @author aibrahim
 *
 */
public class NumberUtil {

	public static BigDecimal defaultIfNull(BigDecimal value) {
		if (value == null) {
			return BigDecimal.ZERO;
		}
		return value;
	}
}
