package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.model.person.Person;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class SalaryCalculatorImpl implements SalaryCalculator {

    @Override
    public BigDecimal getTotalSalaryOnDate(LocalDate onDate) {
        //TODO
        return BigDecimal.ZERO;
    }

    /** employee
	 *
	 * 		int yearsCountLessThen10 = Math.min(yearsCount, 10); // Ratio must be less then 30%, 30% / 3% = 10 years
	 * 		BigDecimal yearExperienceAddition = this.employeeYearRatio.multiply(BigDecimal.valueOf(yearsCountLessThen10))
	 * 				.setScale(2, RoundingMode.HALF_UP);
	 *
	 * 		BigDecimal expectedSalary2 = baseSalaryPart.add(
	 * 				baseSalaryPart.multiply(yearExperienceAddition).setScale(2, RoundingMode.HALF_UP));
	 * */

    /**	manager
	 *
	 * 			private BigDecimal getManagerSalary(BigDecimal baseSalaryPart, int yearsCount, Person manager) {
	 * 		int yearsCountLessThen8 = Math.min(yearsCount, 8); // Ratio must be less then 40%, 40% / 5% = 8 years
	 * 		BigDecimal yearExperienceAddition = this.managerYearRatio.multiply(BigDecimal.valueOf(yearsCountLessThen8))
	 * 				.setScale(2, RoundingMode.HALF_UP);
	 *
	 * 		BigDecimal firstLevelSubordinatesSalary = controller.getFirstLevelSubordinates(manager)
	 * 				.stream()
	 * 				.map(person -> calculator.getSalaryOnDate(person, LocalDate.now()))
	 * 				.reduce(BigDecimal.ZERO, BigDecimal::add)
	 * 				.multiply(this.managerSubordinatesRatio)
	 * 				.setScale(2, RoundingMode.HALF_UP);
	 *
	 * 		return baseSalaryPart.add(
	 * 				baseSalaryPart.multiply(yearExperienceAddition).setScale(2, RoundingMode.HALF_UP))
	 * 				.add(firstLevelSubordinatesSalary);
	 *        }
	 * */

    @Override
    public BigDecimal getSalaryOnDate(Person person, LocalDate onDate) {
        //TODO
        return BigDecimal.ZERO;
    }
}
