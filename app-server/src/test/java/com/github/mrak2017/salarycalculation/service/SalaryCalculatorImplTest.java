package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.BaseTest;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SalaryCalculatorImplTest extends BaseTest {

    @Mock
    private PersonController personControllerMock;

    @InjectMocks
    private SalaryCalculatorImpl calculator;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

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
        Person employee = createWithExperience(baseSalaryPart, yearsCount, this::createEmployee);
        LocalDate onDate = LocalDate.now();
        Optional<Person2Group> group = controller.getGroupOnDate(employee, onDate);
        when(personControllerMock.getGroupOnDate(employee, onDate)).thenReturn(group);

        BigDecimal salary = calculator.getSalaryOnDate(employee, onDate);

        assertEquals(expectedSalary.setScale(2, RoundingMode.HALF_UP), salary);
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
        Person manager = createWithExperience(BigDecimal.valueOf(100), 1, this::createManager);

        LocalDate onDate = LocalDate.now();
        Optional<Person2Group> group = controller.getGroupOnDate(manager, onDate);
        when(personControllerMock.getGroupOnDate(manager, onDate)).thenReturn(group);

        BigDecimal salary = calculator.getSalaryOnDate(manager, onDate);

        assertEquals(BigDecimal.valueOf(105.00).setScale(2, RoundingMode.HALF_UP), salary);
    }

    /**
     * - group = Manager
     * - 3 years of job experience
     * - salary base part = 200
     * - 2 first level subordinates with salary 103 each
     * <p>
     * Expected salary = (200 * 1.15) + (206 * 0.005) = 231.03
     */
    @Test
    void testCalcSalaryOnDateManager3Years2Subordinates() {
        Person manager = createWithExperience(BigDecimal.valueOf(200), 3, this::createManager);
        LocalDate onDate = LocalDate.now();

        Person subordinate1 = createEmployee();
        controller.updateChief(subordinate1.getId(), manager.getId());

        Person subordinate2 = createEmployee();
        controller.updateChief(subordinate2.getId(), manager.getId());

        Optional<Person2Group> groupManager = controller.getGroupOnDate(manager, onDate);
        when(personControllerMock.getGroupOnDate(manager, onDate)).thenReturn(groupManager);

        Optional<Person2Group> groupSub1 = controller.getGroupOnDate(subordinate1, onDate);
        when(personControllerMock.getGroupOnDate(subordinate1, onDate)).thenReturn(groupSub1);

        Optional<Person2Group> groupSub2 = controller.getGroupOnDate(subordinate2, onDate);
        when(personControllerMock.getGroupOnDate(subordinate2, onDate)).thenReturn(groupSub2);

        doReturn(Arrays.asList(subordinate1, subordinate2)).when(personControllerMock).getFirstLevelSubordinates(
                ArgumentMatchers.argThat(m ->m.getId().equals(manager.getId())));

        BigDecimal salary = calculator.getSalaryOnDate(manager, onDate);

        assertEquals(BigDecimal.valueOf(231.03).setScale(2, RoundingMode.HALF_UP), salary);
    }

    /**
     * - group = Manager
     * - 9 years of job experience
     * - salary base part = 300
     * - no subordinates
     * <p>
     * Expected salary = 300 * 1.40 = 420
     */
    @Test
    void testCalcSalaryOnDateManager9Years0Subordinates() {
        Person manager = createWithExperience(BigDecimal.valueOf(300), 9, this::createManager);

        LocalDate onDate = LocalDate.now();
        Optional<Person2Group> group = controller.getGroupOnDate(manager, onDate);
        when(personControllerMock.getGroupOnDate(manager, onDate)).thenReturn(group);

        BigDecimal salary = calculator.getSalaryOnDate(manager, onDate);


        assertEquals(BigDecimal.valueOf(420).setScale(2, RoundingMode.HALF_UP), salary);
    }

    /**
     * - group = Salesman
     * - 1 year of job experience
     * - salary base part = 100
     * - no subordinates
     * <p>
     * Expected salary = 100 * 1.01 = 101
     */
    @Test
    void testCalcSalaryOnDateSalesman1Year0Subordinates() {
        Person salesman = createWithExperience(BigDecimal.valueOf(100), 1, this::createSalesman);

        LocalDate onDate = LocalDate.now();
        Optional<Person2Group> group = controller.getGroupOnDate(salesman, onDate);
        when(personControllerMock.getGroupOnDate(salesman, onDate)).thenReturn(group);

        BigDecimal salary = calculator.getSalaryOnDate(salesman, onDate);

        assertEquals(BigDecimal.valueOf(101).setScale(2, RoundingMode.HALF_UP), salary);
    }

    /**
     * - group = Salesman
     * - 3 year of job experience
     * - salary base part = 200
     * - 2 second level subordinates with salary 103 each
     * - 1 first level subordinate with salary 106.03
     * <p>
     * Expected salary = (200 * 1.03) + (106.03 + 206) * 0.003 = 206.94
     */
    @Test
    void testCalcSalaryOnDateSalesman3Years3Subordinates() {
        Person salesman = createWithExperience(BigDecimal.valueOf(200), 3, this::createSalesman);
        LocalDate onDate = LocalDate.now();

        Person manager = createManager();
        controller.updateChief(manager.getId(), salesman.getId());

        Person subordinate1 = createEmployee();
        controller.updateChief(subordinate1.getId(), manager.getId());

        Person subordinate2 = createEmployee();
        controller.updateChief(subordinate2.getId(), manager.getId());

        Optional<Person2Group> group = controller.getGroupOnDate(salesman, onDate);
        when(personControllerMock.getGroupOnDate(salesman, onDate)).thenReturn(group);

        Optional<Person2Group> groupManager = controller.getGroupOnDate(manager, onDate);
        when(personControllerMock.getGroupOnDate(manager, onDate)).thenReturn(groupManager);

        Optional<Person2Group> groupSub1 = controller.getGroupOnDate(subordinate1, onDate);
        when(personControllerMock.getGroupOnDate(subordinate1, onDate)).thenReturn(groupSub1);

        Optional<Person2Group> groupSub2 = controller.getGroupOnDate(subordinate2, onDate);
        when(personControllerMock.getGroupOnDate(subordinate2, onDate)).thenReturn(groupSub2);

        doReturn(Arrays.asList(manager, subordinate1, subordinate2)).when(personControllerMock).getAllSubordinates(
                ArgumentMatchers.argThat(s ->s.getId().equals(salesman.getId())));

        doReturn(Arrays.asList(subordinate1, subordinate2)).when(personControllerMock).getFirstLevelSubordinates(
                ArgumentMatchers.argThat(m ->m.getId().equals(manager.getId())));

        BigDecimal salary = calculator.getSalaryOnDate(salesman, onDate);

        assertEquals(BigDecimal.valueOf(206.94).setScale(2, RoundingMode.HALF_UP), salary);
    }

    /**
     * - group = Salesman
     * - 36 year of job experience
     * - salary base part = 300
     * - no subordinates
     * <p>
     * Expected salary = 300 * 1.35 = 405
     */
    @Test
    void testCalcSalaryOnDateSalesman36Years0Subordinates() {
        Person salesman = createWithExperience(BigDecimal.valueOf(300), 36, this::createSalesman);

        LocalDate onDate = LocalDate.now();
        Optional<Person2Group> group = controller.getGroupOnDate(salesman, onDate);
        when(personControllerMock.getGroupOnDate(salesman, onDate)).thenReturn(group);

        BigDecimal salary = calculator.getSalaryOnDate(salesman, onDate);

        assertEquals(BigDecimal.valueOf(405).setScale(2, RoundingMode.HALF_UP), salary);
    }

    /**
     * - 1 Employee (years = 1, base salary part = 100, salary = 103)
     * - 1 Manager (years = 3, base salary part = 200, salary = 231.03)
     * -- Subordinates:
     * -- 2 Employees (years = 1, base salary part = 100, salary = 103)
     * - 1 Salesman (years = 3, base salary part = 200, salary = 206.94)
     * -- Subordinates:
     * -- 1 Manager (years = 1, base salary part = 100, salary = 106.03)
     * --- Subordinates:
     * --- 2 Employees (years = 1, base salary part = 100, salary = 103)
     * <p>
     * Expected total salary = 103 + 231.03 + (103*2) + 206.94 + 106.03 + (103*2) = 1059
     */
    @Test
    void testCalcTotalSalaryOnDate() {
        // 1 Employee (years = 1, base salary part = 100, salary = 103)
        createEmployee();

        // 1 Manager (years = 3, base salary part = 200, salary = 231.03)
        // -- Subordinates:
        // -- 2 Employees (years = 1, base salary part = 100, salary = 103)
        Person topManager = createWithExperience(BigDecimal.valueOf(200), 3, this::createManager);
        Person subordinate1 = createEmployee();
        controller.updateChief(subordinate1.getId(), topManager.getId());
        Person subordinate2 = createEmployee();
        controller.updateChief(subordinate2.getId(), topManager.getId());

        // 1 Salesman (years = 3, base salary part = 200, salary = 206.94)
        // -- Subordinates:
        // -- 1 Manager (years = 1, base salary part = 100, salary = 106.03)
        // 		--- Subordinates:
        //		--- 2 Employees (years = 1, base salary part = 100, salary = 103)
        Person topSalesman = createWithExperience(BigDecimal.valueOf(200), 3, this::createSalesman);
        Person subManager = createManager();
        controller.updateChief(subManager.getId(), topSalesman.getId());
        Person subordinate3 = createEmployee();
        controller.updateChief(subordinate3.getId(), subManager.getId());
        Person subordinate4 = createEmployee();
        controller.updateChief(subordinate4.getId(), subManager.getId());

        BigDecimal salary = calculator.getTotalSalaryOnDate(LocalDate.now());
        assertEquals(BigDecimal.valueOf(1059).setScale(2, RoundingMode.HALF_UP), salary);
    }

    private Person createWithExperience(BigDecimal baseSalaryPart, int yearsCount, BiFunction<BigDecimal, LocalDate, Person> function) {
        int startYear = LocalDate.now().getYear() - yearsCount + 1;
        return function.apply(baseSalaryPart, LocalDate.of(startYear, 1, 1));
    }

}
