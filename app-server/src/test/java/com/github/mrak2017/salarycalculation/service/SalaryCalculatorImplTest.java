package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.BaseTest;
import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SalaryCalculatorImplTest extends BaseTest {

	@Autowired
	private SalaryCalculator calculator;

	private final BigDecimal employeeYearRatio = new BigDecimal(0.3);

	/**- group = Employee
	 * - 1 year of job experience
	 * - salary base part = 100
	 * */
	@Test
	void testCalcSalaryOnDateEmployee1Year() {
		BigDecimal baseSalaryPart = new BigDecimal(100);
		int yearsCount = 1;

		checkEmployeeSalary(baseSalaryPart, yearsCount);
	}

	/**- group = Employee
	 * - 3 year of job experience
	 * - salary base part = 200
	 * */
	@Test
	void testCalcSalaryOnDateEmployee3Years() {
		BigDecimal baseSalaryPart = new BigDecimal(200);
		int yearsCount = 3;

		checkEmployeeSalary(baseSalaryPart, yearsCount);
	}

	/**- group = Employee
	 * - 11 year of job experience
	 * - salary base part = 300
	 * */
	@Test
	void testCalcSalaryOnDateEmployee11Years() {
		BigDecimal baseSalaryPart = new BigDecimal(300);
		int yearsCount = 11;

		checkEmployeeSalary(baseSalaryPart, yearsCount);
	}

	private void checkEmployeeSalary(BigDecimal baseSalaryPart, int yearsCount) {
		int startYear = LocalDate.now().getYear() - yearsCount + 1;

		Person employee = createEmployee(baseSalaryPart, LocalDate.of(startYear, 1, 1));
		BigDecimal salary = calculator.getSalaryOnDate(employee, LocalDate.now());
		int yearsCountLessThen10 = Math.min(yearsCount, 10);
		BigDecimal expectedSalary = baseSalaryPart.add(
				baseSalaryPart.multiply(this.employeeYearRatio.multiply(new BigDecimal(yearsCountLessThen10))));

		assertEquals(expectedSalary, salary);
	}

	@Test
	void testCalcSalaryOnDateManager1Year0Subordinates() {
		// TODO
	}

	@Test
	void testCalcSalaryOnDateManager3Years2Subordinates() {
		// TODO
	}

	@Test
	void testCalcSalaryOnDateManager9Years0Subordinates() {
		// TODO
	}

	@Test
	void testCalcSalaryOnDateSalesman1Year0Subordinates() {
		// TODO
	}

	@Test
	void testCalcSalaryOnDateSalesman3Years2Subordinates() {
		// TODO
	}

	@Test
	void testCalcSalaryOnDateSales36Years0Subordinates() {
		// TODO
	}

	@Test
	void testCalcTotalSalaryOnDate() {
		// TODO
	}
}
