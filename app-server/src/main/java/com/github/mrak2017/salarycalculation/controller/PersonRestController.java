package com.github.mrak2017.salarycalculation.controller;

import com.github.mrak2017.salarycalculation.controller.dto.ComboboxItemDTO;
import com.github.mrak2017.salarycalculation.controller.dto.OrgStructureItemDTO;
import com.github.mrak2017.salarycalculation.controller.dto.PersonDTO;
import com.github.mrak2017.salarycalculation.controller.dto.PersonJournalDTO;
import com.github.mrak2017.salarycalculation.core.Exception.ResourceNotFoundException;
import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import com.github.mrak2017.salarycalculation.service.PersonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/persons/")
public class PersonRestController {

	@Autowired
	private PersonController controller;

	@GetMapping("journal")
	List<PersonJournalDTO> getForJournal(@RequestParam(required = false) String q) {
		String search = q != null ? q : "";
		return controller.findAll(search)
					   .stream()
					   .map(this::fillJournalDTO)
					   .collect(Collectors.toList());
	}

	@PostMapping()
	void addPerson(@RequestBody PersonJournalDTO dto) {
		controller.create(dto);
	}

	@GetMapping("{id}")
	PersonDTO getPerson(@PathVariable long id) {
		Person person = controller.find(id).orElseThrow(ResourceNotFoundException::new);
		List<Person2Group> groups = controller.getAllGroups(person);
		Person chief = controller.getCurrentChief(person).orElse(null);
		BigDecimal salary = controller.getCurrentSalary(person);
		OrgStructureItemDTO hierarchy = getChildrenOrgStructureDTO(person);
		return new PersonDTO(person, groups, chief, salary, hierarchy);
	}

	@GetMapping("get-possible-chiefs")
	List<ComboboxItemDTO> getPossibleChiefs() {
		return controller.getPossibleChiefs()
					   .stream()
					   .map(ComboboxItemDTO::new)
					   .collect(Collectors.toList());
	}

	@GetMapping("{id}/get-possible-subordinates")
	List<ComboboxItemDTO> getPossibleSubordinates(@PathVariable long id) {
		Person person = controller.find(id).orElseThrow(ResourceNotFoundException::new);
		return controller.getPossibleSubordinates(person)
					   .stream()
					   .map(ComboboxItemDTO::new)
					   .collect(Collectors.toList());
	}

	private OrgStructureItemDTO getChildrenOrgStructureDTO(Person person) {
		List<OrgStructureItemDTO> children = controller.getFirstLevelSubordinates(person)
													 .stream()
													 .map(this::getChildrenOrgStructureDTO)
													 .collect(Collectors.toList());
		return new OrgStructureItemDTO(person, children);
	}

	private PersonJournalDTO fillJournalDTO(Person person) {
		GroupType groupType = controller.getCurrentGroupType(person).orElse(null);
		BigDecimal salary = controller.getCurrentSalary(person);
		return new PersonJournalDTO(person, groupType, salary);
	}
}
