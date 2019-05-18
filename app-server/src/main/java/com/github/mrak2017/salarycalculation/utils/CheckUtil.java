package com.github.mrak2017.salarycalculation.utils;

public class CheckUtil {

	public static void AssertNotNull(Object obj, String message) {
		if (obj == null) {
			throw new IllegalArgumentException(message);
		}
	}
}
