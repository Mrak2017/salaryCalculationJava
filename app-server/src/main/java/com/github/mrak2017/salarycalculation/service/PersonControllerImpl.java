package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.controller.dto.Person2GroupDTO;
import com.github.mrak2017.salarycalculation.controller.dto.PersonDTO;
import com.github.mrak2017.salarycalculation.controller.dto.PersonJournalDTO;
import com.github.mrak2017.salarycalculation.core.Exception.UserErrorTemplate;
import com.github.mrak2017.salarycalculation.core.Exception.ResourceNotFoundException;
import com.github.mrak2017.salarycalculation.model.person.OrganizationStructure;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import com.github.mrak2017.salarycalculation.repository.PersonRepository;
import com.github.mrak2017.salarycalculation.repository.orgStructure.OrganizationStructureRepository;
import com.github.mrak2017.salarycalculation.repository.person2group.Person2GroupRepository;
import com.github.mrak2017.salarycalculation.utils.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
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
	public Optional<Person2Group> getCurrentGroup(Person person) {
		return groupRepository.getPersonGroupOnDate(person, LocalDate.now());
	}

	@Override
	public BigDecimal getCurrentSalary(Person person) {
		return calculator.getSalaryOnDate(person, LocalDate.now());
	}

	@Override
	public Long create(PersonJournalDTO dto) {
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

		return person.getId();
	}

	@Override
	public Optional<Person> find(Long id) {
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
		return groupRepository.getPossibleChiefs();
	}

	@Override
	public List<Person> getPossibleSubordinates(Person person) {
		//TODO
		return Collections.emptyList();
	}

	@Override
	public Person2Group getGroupById(Long id) {
		return groupRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
	}

	@Override
	public void deleteGroup(Long id) {
		groupRepository.deleteById(id);
	}

	@Override
	public void updatePerson(PersonDTO dto) {
		Person person = repository.findById(dto.id).orElseThrow(ResourceNotFoundException::new);
		person.setFirstName(dto.firstName);
		person.setLastName(dto.lastName);
		person.setFirstDate(dto.startDate);
		person.setLastDate(dto.endDate);
		person.setBaseSalaryPart(dto.baseSalaryPart);
		repository.save(person);
	}

	@Override
	public Long addGroup(Long id, Person2GroupDTO dto) {
		Person person = find(id).orElseThrow(ResourceNotFoundException::new);
		Person2Group group = new Person2Group();
		group.setPerson(person);
		group.setPeriodStart(dto.periodStart);
		group.setPeriodEnd(dto.periodEnd);
		group.setGroupType(dto.groupType);
		checkGroupBeforeSave(person, group);
		groupRepository.save(group);

		return group.getId();
	}

	@Override
	public void updateGroup(Person2GroupDTO dto) {
		Person2Group group = groupRepository.findById(dto.id).orElseThrow(ResourceNotFoundException::new);
		group.setPeriodStart(dto.periodStart);
		group.setPeriodEnd(dto.periodEnd);
		group.setGroupType(dto.groupType);
		checkGroupBeforeSave(group.getPerson(), group);
		groupRepository.save(group);
	}

	private void checkGroupBeforeSave(Person person, Person2Group p2g) {
		List<Person2Group> existingGroups = groupRepository.getExistingGroups(person, p2g);
		if (existingGroups.size() > 0) {
			String identifiers = existingGroups.stream()
										 .map(Person2Group::getId)
										 .map(String::valueOf)
										 .collect(Collectors.joining(","));
			String message = String.format(UserErrorTemplate.MORE_THAN_ONE_GROUP_ON_DATE_RANGE.getTemplate(),
					p2g.getPeriodStart().toString(), p2g.getPeriodEnd().toString(), identifiers);
			throw new ValidationException(message);
		}
	}
}
