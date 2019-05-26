package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.controller.dto.PersonJournalDTO;
import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.OrganizationStructure;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import com.github.mrak2017.salarycalculation.repository.PersonRepository;
import com.github.mrak2017.salarycalculation.repository.orgStructure.OrganizationStructureRepository;
import com.github.mrak2017.salarycalculation.repository.person2group.Person2GroupRepository;
import com.github.mrak2017.salarycalculation.utils.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonControllerImpl implements PersonController {

	@Autowired
	private SalaryCalculator calculator;

	private final PersonRepository repository;

	private final Person2GroupRepository groupRepository;

	private final OrganizationStructureRepository orgStructureRep;

	PersonControllerImpl(PersonRepository repository, Person2GroupRepository groupRepository, OrganizationStructureRepository orgStructureRep) {
		this.repository = repository;
		this.groupRepository = groupRepository;
		this.orgStructureRep = orgStructureRep;
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
		return calculator.getSalaryOnDate(person, LocalDate.now());
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

		OrganizationStructure structure = new OrganizationStructure();
		structure.setPerson(person);
		orgStructureRep.save(structure);
	}

	@Override
	public Optional<Person> find(long id) {
		return repository.findById(id);
	}

	@Override
	public List<Person> getFirstLevelSubordinates(Person person) {
		OrganizationStructure orgStructure = orgStructureRep.findByPerson(person);
		CheckUtil.AssertNotNull(orgStructure, "Unable to find hierarchy unit for Person with id:" + person.getId());
		String childPath = getChildPath(orgStructure);
		List<OrganizationStructure> subOrgStructures = orgStructureRep.findFirstLevelByPath(childPath);
		return subOrgStructures.stream()
					   .map(OrganizationStructure::getPerson)
					   .collect(Collectors.toList());
	}

	private String getChildPath(OrganizationStructure structure) {
		return structure.getMaterializedPath() != null ?
					   String.join(OrganizationStructure.MAT_PATH_DELIMITER,
							   structure.getMaterializedPath(), structure.getId().toString())
					   : structure.getId().toString();
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

	@Override
	public List<Person> getPossibleSubordinates(Person person) {
		//TODO
		return Collections.emptyList();
	}
}
