package com.github.mrak2017.salarycalculation.controller;

import com.github.mrak2017.salarycalculation.controller.dto.*;
import com.github.mrak2017.salarycalculation.core.Exception.ResourceNotFoundException;
import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import com.github.mrak2017.salarycalculation.service.PersonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Rest-service for app-client requests.
 * Allows to get data for journal of person and filter it,
 * have CRUD-methods for persons,
 * can calculate salary for the whole organization, based on all employees information
 */
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

	@PutMapping("update-main-info")
	void updateMainInfo(@RequestBody PersonDTO dto) {
		controller.updatePerson(dto);
	}

	@GetMapping("get-possible-chiefs")
	List<ComboboxItemDTO> getPossibleChiefs() {
		return controller.getPossibleChiefs()
					   .stream()
					   .map(ComboboxItemDTO::new)
					   .collect(Collectors.toList());
	}

	@PutMapping("{id}/new-chief/{chiefId}")
	void updateChief(@PathVariable long id, @PathVariable long chiefId) {
		controller.updateChief(id, chiefId);
	}

	@PostMapping("{id}/add-group")
	void addGroup(@PathVariable long id, @RequestBody Person2GroupDTO dto) {
		controller.addGroup(id, dto);
	}

	@GetMapping("groups/{id}")
	Person2GroupDTO getGroup(@PathVariable long id) {
		Person2Group group = controller.getGroupById(id);
		return new Person2GroupDTO(group);
	}

	@PutMapping("groups")
	void updateGroup(@RequestBody Person2GroupDTO dto) {
		controller.updateGroup(dto);
	}

	@DeleteMapping("groups/{id}")
	void deleteGroup(@PathVariable long id) {
		controller.deleteGroup(id);
	}

	@GetMapping("{id}/get-possible-subordinates")
	List<ComboboxItemDTO> getPossibleSubordinates(@PathVariable long id) {
		Person person = controller.find(id).orElseThrow(ResourceNotFoundException::new);
		return controller.getPossibleSubordinates(person)
					   .stream()
					   .map(ComboboxItemDTO::new)
					   .collect(Collectors.toList());
	}

	@GetMapping("{id}/salary")
	BigDecimal calcSalaryOnDate(@PathVariable long id, @RequestParam(required = false) String calcDate) {
		//TODO
		return BigDecimal.ZERO;
	}

	@GetMapping("total-salary")
	BigDecimal calcTotalSalaryOnDate(@RequestParam(required = false) String calcDate) {
		//TODO
		return BigDecimal.ZERO;
	}

	private OrgStructureItemDTO getChildrenOrgStructureDTO(Person person) {
		List<OrgStructureItemDTO> children = controller.getFirstLevelSubordinates(person)
													 .stream()
													 .filter(Objects::nonNull)
													 .map(this::getChildrenOrgStructureDTO)
													 .collect(Collectors.toList());
		return new OrgStructureItemDTO(person, children);
	}

	private PersonJournalDTO fillJournalDTO(Person person) {
		GroupType groupType = controller.getCurrentGroup(person)
									  .map(Person2Group::getGroupType)
									  .orElse(null);
		BigDecimal salary = controller.getCurrentSalary(person);
		return new PersonJournalDTO(person, groupType, salary);
	}
}
