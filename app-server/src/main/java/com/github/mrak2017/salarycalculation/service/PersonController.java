package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.model.Person;
import com.github.mrak2017.salarycalculation.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PersonController {

    private final PersonRepository repository;

    PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    public List<Person> getTestData() {
        repository.save(createPerson("User1", "LastName1"));
        repository.save(createPerson("User2", "LastName2"));
        return repository.findAll();
    }

    public Person createPerson(String firstName, String lastName) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setFirstDate(LocalDate.now());
        return person;
    }
}
