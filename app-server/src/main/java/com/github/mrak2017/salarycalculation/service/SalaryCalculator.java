package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.model.person.Person;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SalaryCalculator {
	BigDecimal getTotalSalaryOnDate(LocalDate onDate);

	BigDecimal getSalaryOnDate(Person person, LocalDate onDate);
}
