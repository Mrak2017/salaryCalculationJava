package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.BaseUnitTest;
import com.github.mrak2017.salarycalculation.model.person.GroupType;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SalaryCalculatorImplTest extends BaseUnitTest {

    @InjectMocks
    private SalaryCalculatorImpl calculator;

    @Mock
    private PersonController personControllerMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private String getStringUUID() {
        return UUID.randomUUID().toString();
    }

    private Person createPerson(BigDecimal baseSalaryPart, LocalDate startDate) {
        Person person = new Person();
        person.setFirstName(getStringUUID());
        person.setLastName(getStringUUID());
        person.setBaseSalaryPart(baseSalaryPart);
        person.setFirstDate(startDate);
        return person;
    }

    private Person2Group createGroup(Person person, GroupType groupType) {
        Person2Group group = new Person2Group();
        group.setPerson(person);
        group.setGroupType(groupType);
        group.setPeriodStart(person.getFirstDate());
        return group;
    }

    private Person createPersonWithMock(BigDecimal baseSalaryPart, int yearsCount, LocalDate onDate, GroupType type) {
        int startYear = LocalDate.now().getYear() - yearsCount + 1;
        Person person = createPerson(baseSalaryPart, LocalDate.of(startYear, 1, 1));
        Person2Group group = createGroup(person, type);

        mockGetGroupOnDate(person, onDate, group);
        return person;
    }

    private void mockGetGroupOnDate(Person person, LocalDate onDate, Person2Group group) {
        when(personControllerMock.getGroupOnDate(person, onDate)).thenReturn(Optional.of(group));
    }

    private void mockGetFirstLevelSubordinates(List<Person> result, Person manager) {
        doReturn(result).when(personControllerMock).getFirstLevelSubordinates(
                ArgumentMatchers.argThat(m -> m.getFirstName().equals(manager.getFirstName())));
    }

    private void mockGetAllSubordinates(List<Person> result, Person salesman) {
        doReturn(result).when(personControllerMock).getAllSubordinates(
                ArgumentMatchers.argThat(s -> s.getFirstName().equals(salesman.getFirstName())));
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
        BigDecimal expectedSalary = BigDecimal.valueOf(103.00);

        checkEmployeeSalary(baseSalaryPart, yearsCount, expectedSalary);
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
        BigDecimal expectedSalary = BigDecimal.valueOf(218.00);

        checkEmployeeSalary(baseSalaryPart, yearsCount, expectedSalary);
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
        BigDecimal expectedSalary = BigDecimal.valueOf(390.00);

        checkEmployeeSalary(baseSalaryPart, yearsCount, expectedSalary);
    }

    private void checkEmployeeSalary(BigDecimal baseSalaryPart, int yearsCount, BigDecimal expectedSalary) {
        LocalDate onDate = LocalDate.now();
        Person employee = createPersonWithMock(baseSalaryPart, yearsCount, onDate, GroupType.Employee);

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
        BigDecimal baseSalaryPart = BigDecimal.valueOf(100);
        int yearsCount = 1;
        LocalDate onDate = LocalDate.now();
        Person manager = createPersonWithMock(baseSalaryPart, yearsCount, onDate, GroupType.Manager);

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
        BigDecimal baseSalaryPart = BigDecimal.valueOf(200);
        int yearsCount = 3;
        LocalDate onDate = LocalDate.now();
        Person manager = createPersonWithMock(baseSalaryPart, yearsCount, onDate, GroupType.Manager);

        Person subordinate1 = createPersonWithMock(BigDecimal.valueOf(100), 1, LocalDate.now(), GroupType.Employee);
        Person subordinate2 = createPersonWithMock(BigDecimal.valueOf(100), 1, LocalDate.now(), GroupType.Employee);

        mockGetFirstLevelSubordinates(Arrays.asList(subordinate1, subordinate2), manager);

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
        BigDecimal baseSalaryPart = BigDecimal.valueOf(300);
        int yearsCount = 9;
        LocalDate onDate = LocalDate.now();
        Person manager = createPersonWithMock(baseSalaryPart, yearsCount, onDate, GroupType.Manager);

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
        BigDecimal baseSalaryPart = BigDecimal.valueOf(100);
        int yearsCount = 1;
        LocalDate onDate = LocalDate.now();
        Person salesman = createPersonWithMock(baseSalaryPart, yearsCount, onDate, GroupType.Salesman);

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
        BigDecimal baseSalaryPart = BigDecimal.valueOf(200);
        int yearsCount = 3;
        LocalDate onDate = LocalDate.now();
        Person salesman = createPersonWithMock(baseSalaryPart, yearsCount, onDate, GroupType.Salesman);

        Person manager = createPersonWithMock(BigDecimal.valueOf(100), 1, LocalDate.now(), GroupType.Manager);
        Person subordinate1 = createPersonWithMock(BigDecimal.valueOf(100), 1, LocalDate.now(), GroupType.Employee);
        Person subordinate2 = createPersonWithMock(BigDecimal.valueOf(100), 1, LocalDate.now(), GroupType.Employee);

        mockGetFirstLevelSubordinates(Arrays.asList(subordinate1, subordinate2), manager);
        mockGetAllSubordinates(Arrays.asList(manager, subordinate1, subordinate2), salesman);

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
        BigDecimal baseSalaryPart = BigDecimal.valueOf(300);
        int yearsCount = 36;
        LocalDate onDate = LocalDate.now();
        Person salesman = createPersonWithMock(baseSalaryPart, yearsCount, onDate, GroupType.Salesman);

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
        Person simpleEmployee = createPersonWithMock(BigDecimal.valueOf(100), 1, LocalDate.now(), GroupType.Employee);

        // 1 Manager (years = 3, base salary part = 200, salary = 231.03)
        // -- Subordinates:
        // -- 2 Employees (years = 1, base salary part = 100, salary = 103)
        Person topManager = createPersonWithMock(BigDecimal.valueOf(200), 3, LocalDate.now(), GroupType.Manager);
        Person subordinate1 = createPersonWithMock(BigDecimal.valueOf(100), 1, LocalDate.now(), GroupType.Employee);
        Person subordinate2 = createPersonWithMock(BigDecimal.valueOf(100), 1, LocalDate.now(), GroupType.Employee);

        // 1 Salesman (years = 3, base salary part = 200, salary = 206.94)
        // -- Subordinates:
        // -- 1 Manager (years = 1, base salary part = 100, salary = 106.03)
        // 		--- Subordinates:
        //		--- 2 Employees (years = 1, base salary part = 100, salary = 103)
        Person topSalesman = createPersonWithMock(BigDecimal.valueOf(200), 3, LocalDate.now(), GroupType.Salesman);
        Person subManager = createPersonWithMock(BigDecimal.valueOf(100), 1, LocalDate.now(), GroupType.Manager);
        Person subordinate3 = createPersonWithMock(BigDecimal.valueOf(100), 1, LocalDate.now(), GroupType.Employee);
        Person subordinate4 = createPersonWithMock(BigDecimal.valueOf(100), 1, LocalDate.now(), GroupType.Employee);

        LocalDate onDate = LocalDate.now();
        mockGetFirstLevelSubordinates(Arrays.asList(subordinate1, subordinate2), topManager);
        mockGetFirstLevelSubordinates(Arrays.asList(subordinate3, subordinate4), subManager);

        mockGetAllSubordinates(Arrays.asList(subManager, subordinate3, subordinate4), topSalesman);

        List<Person> all = Arrays.asList(
                simpleEmployee,
                topManager,
                subordinate1,
                subordinate2,
                topSalesman,
                subManager,
                subordinate3,
                subordinate4
        );
        doReturn(all).when(personControllerMock).findAll(ArgumentMatchers.anyString());

        BigDecimal salary = calculator.getTotalSalaryOnDate(onDate);
        assertEquals(BigDecimal.valueOf(1059).setScale(2, RoundingMode.HALF_UP), salary);
    }


}
