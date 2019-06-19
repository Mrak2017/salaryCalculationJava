package com.github.mrak2017.salarycalculation.utils;

import java.util.List;
import java.util.function.Function;

public class CheckUtil {

	public static void AssertNotNull(Object obj, String message) {
		if (obj == null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static <T> boolean listContainsByFunction(final List<T> list, final Function<T, Boolean> func) {
		return list.stream().anyMatch(func::apply);
	}
}
