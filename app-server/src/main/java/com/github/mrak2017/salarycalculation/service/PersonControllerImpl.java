package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PersonControllerImpl implements PersonController {

	private final PersonRepository repository;

	PersonControllerImpl(PersonRepository repository) {
		this.repository = repository;
	}

	public List<Person> getTestData() {
		repository.save(createPerson("Иван", "Иванов"));
		repository.save(createPerson("Петр", "Петров"));
		return repository.findAll();
	}

	private Person createPerson(String firstName, String lastName) {
		Person person = new Person();
		person.setFirstName(firstName);
		person.setLastName(lastName);
		person.setFirstDate(LocalDate.now());
		return person;
	}

	@Override
	public GroupType getCurrentGroup(Person person) {
		return GroupType.Employee;
	}

	@Override
	public BigDecimal getCurrentSalary(Person person) {
		return BigDecimal.ZERO;
	}
}
