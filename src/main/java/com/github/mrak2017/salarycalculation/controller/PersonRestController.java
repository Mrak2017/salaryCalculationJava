package com.github.mrak2017.salarycalculation.controller;

import com.github.mrak2017.salarycalculation.model.Person;
import com.github.mrak2017.salarycalculation.repository.PersonRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonRestController {

    private final PersonRepository repository;

    PersonRestController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/persons")
    List<Person> getAll() {
        return repository.findAll();
    }
}
