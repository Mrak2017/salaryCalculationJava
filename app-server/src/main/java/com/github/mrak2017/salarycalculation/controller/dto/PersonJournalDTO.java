package com.github.mrak2017.salarycalculation.controller.dto;

import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for single journal row
 */
public class PersonJournalDTO {

	public Long id;
	public String firstName;
	public String lastName;
	public LocalDate startDate;
	public GroupType currentGroup;
	public BigDecimal baseSalaryPart;
	public BigDecimal currentSalary;

	public PersonJournalDTO() {

	}

	public PersonJournalDTO(Person person, GroupType group, BigDecimal salary) {
		id = person.getId();
		firstName = person.getFirstName();
		lastName = person.getLastName();
		startDate = person.getFirstDate();
		currentGroup = group;
		baseSalaryPart = person.getBaseSalaryPart();
		currentSalary = salary;
	}
}
