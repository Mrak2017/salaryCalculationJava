package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class SalaryCalculatorImpl implements SalaryCalculator {

    private class SingleSalaryCalcForTotalCallable implements Callable<BigDecimal>{

        private Person person;
        private LocalDate onDate;

        public SingleSalaryCalcForTotalCallable(Person person, LocalDate onDate) {
            this.person = person;
            this.onDate = onDate;
        }

        @Override
        public BigDecimal call() throws Exception {
            return getSalaryOnDate(person, onDate);
        }
    }

    private final PersonController personController;

    public SalaryCalculatorImpl(PersonController personController) {
        this.personController = personController;
    }

    @Override
    public BigDecimal getTotalSalaryOnDate(LocalDate onDate) {
        //HashMap<Person, BigDecimal> storage = new HashMap<>();

        List<Person> all = personController.findAll("");

        ExecutorService pool = Executors.newFixedThreadPool(3);
        Set<Future<BigDecimal>> set = new HashSet<Future<BigDecimal>>();
        for (Person person: all) {
            Callable<BigDecimal> callable = new SingleSalaryCalcForTotalCallable(person, onDate);
            Future<BigDecimal> future = pool.submit(callable);
            set.add(future);
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (Future<BigDecimal> future : set) {
            try {
                sum = sum.add(future.get());
            } catch (Exception e) {
                throw new ValidationException(e.getMessage());
            }
        }
        return sum;
    }

    @Override
    public BigDecimal getSalaryOnDate(Person person, LocalDate onDate) {
        Optional<Person2Group> group = personController.getGroupOnDate(person, onDate);
        if (!group.isPresent() || group.get().getPerson().getBaseSalaryPart() == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal baseSalaryPart = group.get().getPerson().getBaseSalaryPart();
        BigDecimal workExpAddition = getWorkExperienceAddition(group.get(), onDate);
        BigDecimal subordinatesAddition = getSubordinatesAddition(group.get(), onDate);

        BigDecimal result = baseSalaryPart.add(workExpAddition)
                .add(subordinatesAddition)
                .setScale(2, RoundingMode.HALF_UP);

        checkResult(result, person);
        return result;
    }

    private BigDecimal getWorkExperienceAddition(Person2Group group, LocalDate onDate) {
        long yearsWorked = ChronoUnit.YEARS.between(group.getPeriodStart(), onDate) + 1;
        BigDecimal workExpRatio = getWorkExperienceRatio(group.getGroupType())
                .multiply(BigDecimal.valueOf(yearsWorked))
                .min(getMaxWorkExperienceRatio(group.getGroupType()))
                .setScale(2, RoundingMode.HALF_UP);

        return group.getPerson()
                .getBaseSalaryPart()
                .multiply(workExpRatio)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getWorkExperienceRatio(GroupType groupType) {
        switch (groupType) {
            case Employee:
                return BigDecimal.valueOf(0.03);

            case Manager:
                return BigDecimal.valueOf(0.05);

            case Salesman:
                return BigDecimal.valueOf(0.01);

            default:
                throw new ValidationException("Unknown GroupType " + groupType);
        }
    }

    private BigDecimal getMaxWorkExperienceRatio(GroupType groupType) {
        switch (groupType) {
            case Employee:
                return BigDecimal.valueOf(0.3);

            case Manager:
                return BigDecimal.valueOf(0.4);

            case Salesman:
                return BigDecimal.valueOf(0.35);

            default:
                throw new ValidationException("Unknown GroupType " + groupType);
        }
    }

    /**
     * Attention. We take subordinates for current moment, not for "onDate" from input parameters,
     * because we don't have such information
     */
    private BigDecimal getSubordinatesAddition(Person2Group person2Group, LocalDate onDate) {
        List<Person> subordinates = getSubordinates(person2Group.getPerson(), person2Group.getGroupType());
        return subordinates.stream()
                .map(person -> getSalaryOnDate(person, onDate))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(getSubordinatesRatio(person2Group.getGroupType()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private List<Person> getSubordinates(Person person, GroupType groupType) {
        switch (groupType) {
            case Employee:
                return Collections.emptyList();

            case Manager:
                return personController.getFirstLevelSubordinates(person);

            case Salesman:
                return personController.getAllSubordinates(person);

            default:
                throw new ValidationException("Unknown GroupType " + groupType);
        }
    }

    private BigDecimal getSubordinatesRatio(GroupType groupType) {
        switch (groupType) {
            case Employee:
                return BigDecimal.ZERO;

            case Manager:
                return BigDecimal.valueOf(0.005);

            case Salesman:
                return BigDecimal.valueOf(0.003);

            default:
                throw new ValidationException("Unknown GroupType " + groupType);
        }
    }

    private void checkResult(BigDecimal result, Person person) {
		if (result.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException(String.format(
					"Calculated salary less than 0 for person with id %s. Check system settings.", person.getId()));
		}
    }

}
