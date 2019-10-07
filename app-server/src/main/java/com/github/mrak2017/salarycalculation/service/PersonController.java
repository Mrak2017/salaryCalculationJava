package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.controller.dto.Person2GroupDTO;
import com.github.mrak2017.salarycalculation.controller.dto.PersonDTO;
import com.github.mrak2017.salarycalculation.controller.dto.PersonJournalDTO;
import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PersonController {

	List<Person> findAll(String search);

	Optional<Person2Group> getCurrentGroup(Person person);

	BigDecimal getCurrentSalary(Person person);

	Long create(PersonJournalDTO dto);

	Optional<Person> find(Long id);

	List<Person> getFirstLevelSubordinates(Person person);

	List<Person2Group> getAllGroups(Person person);

	Optional<Person> getCurrentChief(Person person);

	List<Person> getPossibleChiefs(Person person);

	List<Person> getPossibleSubordinates(Person person);

	Person2Group getGroupById(Long id);

	void deleteGroup(Long id);

	void updatePerson(PersonDTO dto);

	Long addGroup(Long id, Person2GroupDTO dto);

	void updateGroup(Person2GroupDTO dto);

    void updateChief(Long id, Long chiefId);
}
