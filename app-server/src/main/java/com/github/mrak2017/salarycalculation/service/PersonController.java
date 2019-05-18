package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;

import java.math.BigDecimal;
import java.util.List;

public interface PersonController {

	List<Person> findAll(String search);

	GroupType getCurrentGroup(Person person);

	BigDecimal getCurrentSalary(Person person);
}
