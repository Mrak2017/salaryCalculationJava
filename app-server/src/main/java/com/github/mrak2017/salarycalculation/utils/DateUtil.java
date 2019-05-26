package com.github.mrak2017.salarycalculation.utils;

import java.time.LocalDate;

public class DateUtil {

	public static boolean IsBeforeOrEqual(LocalDate a, LocalDate b) {
		CheckUtil.AssertNotNull(a, "Error on date comparison 'IsBeforeOrEqual'. First date is null");
		CheckUtil.AssertNotNull(b, "Error on date comparison 'IsBeforeOrEqual'. Second date is null");
		return a.isBefore(b) || a.isEqual(b);
	}

	public static boolean IsAfterOrEqual(LocalDate a, LocalDate b) {
		CheckUtil.AssertNotNull(a, "Error on date comparison 'IsAfterOrEqual'. First date is null");
		CheckUtil.AssertNotNull(b, "Error on date comparison 'IsAfterOrEqual'. Second date is null");
		return a.isAfter(b) || a.isEqual(b);
	}
}
