package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.controller.dto.PersonJournalDTO;
import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PersonController {

	List<Person> findAll(String search);

	Optional<GroupType> getCurrentGroupType(Person person);

	BigDecimal getCurrentSalary(Person person);

	void create(PersonJournalDTO dto);

	Optional<Person> find(long id);

	List<Person> getFirstLevelSubordinates(Person person);

	List<Person2Group> getAllGroups(Person person);

	Optional<Person> getCurrentChief(Person person);

	List<Person> getPossibleChiefs();

	List<Person> getPossibleSubordinates(Person person);
}
