package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class SalaryCalculatorImpl implements SalaryCalculator {

    private class SingleSalaryCalcForTotalCallable implements Callable<BigDecimal>{

        private Person person;
        private LocalDate onDate;

        SingleSalaryCalcForTotalCallable(Person person, LocalDate onDate) {
            this.person = person;
            this.onDate = onDate;
        }

        @Override
        public BigDecimal call() {
            return getSalaryOnDate(person, onDate);
        }
    }

    private final PersonService personService;
    private final ConfigurationService configurationService;

    public SalaryCalculatorImpl(PersonService personService, ConfigurationService configurationService) {
        this.personService = personService;
        this.configurationService = configurationService;
    }

    @Override
    public BigDecimal getTotalSalaryOnDate(LocalDate onDate) {
        List<Person> all = personService.findAll("");

        final int NUMBER_OF_THREADS = 3;
        ExecutorService pool = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        Set<Future<BigDecimal>> set = new HashSet<>();
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
        Optional<Person2Group> group = personService.getGroupOnDate(person, onDate);
        if (group.isEmpty() || group.get().getPerson().getBaseSalaryPart() == null) {
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
        return configurationService.getOrDefault(groupType.workExperienceRatioSetting, BigDecimal.ZERO);
    }

    private BigDecimal getMaxWorkExperienceRatio(GroupType groupType) {
        return configurationService.getOrDefault(groupType.maxWorkExperienceRatioSetting, BigDecimal.ZERO);
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
                return personService.getFirstLevelSubordinates(person);

            case Salesman:
                return personService.getAllSubordinates(person);

            default:
                throw new ValidationException("Unknown GroupType " + groupType);
        }
    }

    private BigDecimal getSubordinatesRatio(GroupType groupType) {
        if (groupType.subordinatesRatioSetting != null) {
            return configurationService.getOrDefault(groupType.subordinatesRatioSetting, BigDecimal.ZERO);
        } else {
            return BigDecimal.ZERO;
        }
    }

    private void checkResult(BigDecimal result, Person person) {
		if (result.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException(String.format(
					"Calculated salary less than 0 for person with id %s. Check system settings.", person.getId()));
		}
    }

}
