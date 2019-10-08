package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.BaseTest;
import com.github.mrak2017.salarycalculation.model.person.OrganizationStructure;
import com.github.mrak2017.salarycalculation.model.person.Person;
import org.hibernate.Hibernate;
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

    /**
     * - group = Employee
     * - 1 year of job experience
     * - salary base part = 100
     * <p>
     * Expected salary = 100 * 1.03 = 103
     */
    @Test
    void testCalcSalaryOnDateEmployee1Year() {
        BigDecimal baseSalaryPart = BigDecimal.valueOf(100);
        int yearsCount = 1;

        checkEmployeeSalary(baseSalaryPart, yearsCount, BigDecimal.valueOf(103.00));
    }

    /**
     * - group = Employee
     * - 3 years of job experience
     * - salary base part = 200
     * <p>
     * Expected salary = 200 * 1.09 = 218
     */
    @Test
    void testCalcSalaryOnDateEmployee3Years() {
        BigDecimal baseSalaryPart = BigDecimal.valueOf(200);
        int yearsCount = 3;

        checkEmployeeSalary(baseSalaryPart, yearsCount, BigDecimal.valueOf(218.00));
    }

    /**
     * - group = Employee
     * - 11 years of job experience
     * - salary base part = 300
     * <p>
     * Expected salary = 300 * 1.3 = 390
     */
    @Test
    void testCalcSalaryOnDateEmployee11Years() {
        BigDecimal baseSalaryPart = BigDecimal.valueOf(300);
        int yearsCount = 11;

        checkEmployeeSalary(baseSalaryPart, yearsCount, BigDecimal.valueOf(390.00));
    }

    private void checkEmployeeSalary(BigDecimal baseSalaryPart, int yearsCount, BigDecimal expectedSalary) {
        int startYear = LocalDate.now().getYear() - yearsCount + 1;

        Person employee = createEmployee(baseSalaryPart, LocalDate.of(startYear, 1, 1));
        BigDecimal salary = calculator.getSalaryOnDate(employee, LocalDate.now());

        assertEquals(expectedSalary.setScale(2), salary);
    }

    /**
     * - group = Manager
     * - 1 year of job experience
     * - salary base part = 100
     * - no subordinates
     * <p>
     * Expected salary = 100 * 1.05 = 105
     */
    @Test
    void testCalcSalaryOnDateManager1Year0Subordinates() {
        BigDecimal baseSalaryPart = BigDecimal.valueOf(100);
        int yearsCount = 1;

        int startYear = LocalDate.now().getYear() - yearsCount + 1;
        Person manager = createManager(baseSalaryPart, LocalDate.of(startYear, 1, 1));
        BigDecimal salary = calculator.getSalaryOnDate(manager, LocalDate.now());

        assertEquals(BigDecimal.valueOf(105.00).setScale(2), salary);
    }

    /**
     * - group = Manager
     * - 3 years of job experience
     * - salary base part = 200
     * - 2 first level subordinates with salary 103 each
     * <p>
     * expected salary = (200 * 1.15) + (206 * 0.005) = 231.03
     */
    @Test
    void testCalcSalaryOnDateManager3Years2Subordinates() {
        BigDecimal baseSalaryPart = new BigDecimal(200);
        int yearsCount = 3;

        int startYear = LocalDate.now().getYear() - yearsCount + 1;
        Person manager = createManager(baseSalaryPart, LocalDate.of(startYear, 1, 1));

        Person subordinate1 = createEmployee();
        controller.updateChief(subordinate1.getId(), manager.getId());

        Person subordinate2 = createEmployee();
        controller.updateChief(subordinate2.getId(), manager.getId());

        BigDecimal salary = calculator.getSalaryOnDate(manager, LocalDate.now());

        assertEquals(BigDecimal.valueOf(231.03).setScale(2), salary);
    }

    /**
     * - group = Manager
     * - 9 years of job experience
     * - salary base part = 300
     * - no subordinates
     * <p>
     * expected salary = 300 * 1.40 = 420
     */
    @Test
    void testCalcSalaryOnDateManager9Years0Subordinates() {
        BigDecimal baseSalaryPart = BigDecimal.valueOf(300);
        int yearsCount = 9;

        int startYear = LocalDate.now().getYear() - yearsCount + 1;
        Person manager = createManager(baseSalaryPart, LocalDate.of(startYear, 1, 1));
        BigDecimal salary = calculator.getSalaryOnDate(manager, LocalDate.now());

        assertEquals(BigDecimal.valueOf(420).setScale(2), salary);
    }

    /**
     * - group = Salesman
     * - 1 year of job experience
     * - salary base part = 100
     * - no subordinates
     * <p>
     * expected salary = 100 * 1.01 = 101
     */
    @Test
    void testCalcSalaryOnDateSalesman1Year0Subordinates() {
		/*BigDecimal baseSalaryPart = BigDecimal.valueOf(100);
		int yearsCount = 1;

		int startYear = LocalDate.now().getYear() - yearsCount + 1;
		Person salesman = createS(baseSalaryPart, LocalDate.of(startYear, 1, 1));
		BigDecimal salary = calculator.getSalaryOnDate(manager, LocalDate.now());

		assertEquals(BigDecimal.valueOf(420).setScale(2), salary);*/
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
