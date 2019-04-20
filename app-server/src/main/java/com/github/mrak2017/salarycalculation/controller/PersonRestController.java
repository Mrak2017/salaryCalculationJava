package com.github.mrak2017.salarycalculation.controller;

import com.github.mrak2017.salarycalculation.model.Person;
import com.github.mrak2017.salarycalculation.repository.PersonRepository;
import com.github.mrak2017.salarycalculation.service.PersonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/persons/")
public class PersonRestController {

    @Autowired
    PersonController controller;

    private final PersonRepository repository;

    PersonRestController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping("get")
    List<Person> getAll() {
        controller.getTestData();
        return repository.findAll();
    }
}
