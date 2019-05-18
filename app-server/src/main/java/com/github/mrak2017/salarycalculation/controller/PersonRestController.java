package com.github.mrak2017.salarycalculation.controller;

import com.github.mrak2017.salarycalculation.controller.dto.PersonJournalDTO;
import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.service.PersonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/persons/")
public class PersonRestController {

	@Autowired
	private PersonController controller;

	@GetMapping("journal")
	List<PersonJournalDTO> getForJournal(@RequestParam(required=false) String q) {
		String search = q != null ? q : "";
		return controller.findAll(search)
					   .stream()
					   .map(this::fillJournalDTO)
					   .collect(Collectors.toList());
	}

	private PersonJournalDTO fillJournalDTO(Person person) {
		GroupType group = controller.getCurrentGroup(person);
		BigDecimal salary = controller.getCurrentSalary(person);
		return new PersonJournalDTO(person, group, salary);
	}
}
