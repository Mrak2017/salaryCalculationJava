package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.BaseTest;
import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SalaryCalculatorImplTest extends BaseTest {

	/*@Test
	public void getCurrentSalaryTest() {
		Person person = new Person();

		Person2Group person2Group1 = new Person2Group();
		person2Group1.setPerson(person);
		person2Group1.setPeriodStart(LocalDate.of(2019,1,1));
		person2Group1.setPeriodEnd(LocalDate.of(2019,2,1));
		person2Group1.setGroupType(GroupType.Employee);

		Person2Group person2Group2 = new Person2Group();
		person2Group2.setPerson(person);
		person2Group2.setPeriodStart(LocalDate.of(2019,2,2));
		person2Group2.setGroupType(GroupType.Manager);
	}*/

	/**- group = Employee
	 * - 1 year of job experience
	 * - salary base part = 100
	 * */
	@Test
	void testCalcSalaryOnDateEmployee1Year() {
		// TODO
	}

	@Test
	void testCalcSalaryOnDateEmployee3Years() {
		// TODO
	}

	@Test
	void testCalcSalaryOnDateEmployee11Years() {
		// TODO
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
