package com.github.mrak2017.salarycalculation.utils;

import java.time.LocalDate;

public class DateUtil {

	public static boolean IsBeforeOrEqual(LocalDate a, LocalDate b) {
		CheckUtil.AssertNotNull(a, "Ошибка сравнения дат на меньше либо равно. Не передана первая дата");
		CheckUtil.AssertNotNull(b, "Ошибка сравнения дат на меньше либо равно. Не передана вторая дата");
		return a.isBefore(b) || a.isEqual(b);
	}

	public static boolean IsAfterOrEqual(LocalDate a, LocalDate b) {
		CheckUtil.AssertNotNull(a, "Ошибка сравнения дат на больше либо равно. Не передана первая дата");
		CheckUtil.AssertNotNull(b, "Ошибка сравнения дат на больше либо равно. Не передана вторая дата");
		return a.isAfter(b) || a.isEqual(b);
	}
}
