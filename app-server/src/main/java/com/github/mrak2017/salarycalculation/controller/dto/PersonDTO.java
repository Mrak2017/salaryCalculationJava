package com.github.mrak2017.salarycalculation.controller.dto;

import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import com.github.mrak2017.salarycalculation.utils.DateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PersonDTO {

	public Long id;
	public String firstName;
	public String lastName;
	public LocalDate startDate;
	public LocalDate endDate;
	public GroupType currentGroup;
	public List<Person2GroupDTO> groups;
	public BigDecimal baseSalaryPart;
	public BigDecimal currentSalary;
	public PersonDTO currentChief;
	public OrgStructureItemDTO children;

	public PersonDTO(Person person) {
		fillMainFields(person);
	}

	public PersonDTO(Person person, List<Person2Group> personGroups, Person chief, BigDecimal salary,
					 OrgStructureItemDTO hierarchy) {
		fillMainFields(person);
		currentGroup = personGroups.stream()
							   .filter(this::isCurrentGroup)
							   .findFirst()
							   .map(Person2Group::getGroupType)
							   .orElse(null);
		groups = personGroups.stream().map(Person2GroupDTO::new).collect(Collectors.toList());
		currentSalary = salary;
		if (chief != null) {
			currentChief = new PersonDTO(chief);
		}
		children = hierarchy;
	}

	private void fillMainFields(Person person) {
		id = person.getId();
		firstName = person.getFirstName();
		lastName = person.getLastName();
		startDate = person.getFirstDate();
		endDate = person.getLastDate();
		baseSalaryPart = person.getBaseSalaryPart();
	}

	private boolean isCurrentGroup(Person2Group group) {
		return DateUtil.IsBeforeOrEqual(group.getPeriodStart(), LocalDate.now())
					   && (group.getPeriodEnd() == null
								   || DateUtil.IsAfterOrEqual(group.getPeriodEnd(), LocalDate.now()));
	}
}
