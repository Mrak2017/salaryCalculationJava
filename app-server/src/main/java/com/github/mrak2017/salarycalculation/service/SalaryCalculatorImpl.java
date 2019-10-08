package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.model.person.Person;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public BigDecimal getSalaryOnDate(Person person, LocalDate onDate) {
        //TODO
        return BigDecimal.ZERO;
    }
}
