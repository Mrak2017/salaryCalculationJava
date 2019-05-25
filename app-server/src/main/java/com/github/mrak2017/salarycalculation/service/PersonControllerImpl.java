package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.controller.dto.PersonJournalDTO;
import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import com.github.mrak2017.salarycalculation.repository.PersonRepository;
import com.github.mrak2017.salarycalculation.repository.person2group.Person2GroupRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PersonControllerImpl implements PersonController {

	private final PersonRepository repository;

	private final Person2GroupRepository groupRepository;

	PersonControllerImpl(PersonRepository repository, Person2GroupRepository groupRepository) {
		this.repository = repository;
		this.groupRepository = groupRepository;
	}

	@Override
	public List<Person> findAll(String search) {
		return repository.findByLastNameContainingIgnoreCase(search);
	}

	@Override
	public Optional<GroupType> getCurrentGroupType(Person person) {
		Person2Group group = groupRepository.getPersonGroupOnDate(person, LocalDate.now()).orElse(null);
		if (group != null) {
			return Optional.of(group.getGroupType());
		}
		return Optional.empty();
	}

	@Override
	public BigDecimal getCurrentSalary(Person person) {
		//TODO
		return BigDecimal.ZERO;
	}

	@Override
	public void create(PersonJournalDTO dto) {
		Person person = new Person();
		person.setFirstName(dto.firstName);
		person.setLastName(dto.lastName);
		person.setFirstDate(dto.startDate);
		person.setBaseSalaryPart(dto.baseSalaryPart);
		repository.save(person);

		Person2Group group = new Person2Group();
		group.setPerson(person);
		group.setPeriodStart(dto.startDate);
		group.setGroupType(dto.currentGroup);
		groupRepository.save(group);
	}

	@Override
	public Optional<Person> find(long id) {
		return repository.findById(id);
	}

	@Override
	public List<Person> getFirstLevelSubordinates(Person person) {
		//TODO
		return null;
	}

	@Override
	public List<Person2Group> getAllGroups(Person person) {
		return groupRepository.findByPersonOrderByPeriodStartAsc(person);
	}

	@Override
	public Optional<Person> getCurrentChief(Person person) {
		//TODO
		return Optional.empty();
	}

	@Override
	public List<Person> getPossibleChiefs() {
		//TODO
		return Collections.emptyList();
	}
}
