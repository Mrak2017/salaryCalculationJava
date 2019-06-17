package com.github.mrak2017.salarycalculation.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.github.mrak2017.salarycalculation.BaseTest;
import com.github.mrak2017.salarycalculation.controller.dto.ComboboxItemDTO;
import com.github.mrak2017.salarycalculation.controller.dto.Person2GroupDTO;
import com.github.mrak2017.salarycalculation.controller.dto.PersonJournalDTO;
import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.OrganizationStructure;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import com.github.mrak2017.salarycalculation.repository.PersonRepository;
import com.github.mrak2017.salarycalculation.repository.orgStructure.OrganizationStructureRepository;
import com.github.mrak2017.salarycalculation.repository.person2group.Person2GroupRepository;
import com.github.mrak2017.salarycalculation.service.PersonController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PersonRestControllerTest extends BaseTest {

	private static final String REST_PREFIX = "/api/persons/";

	@Autowired
	private PersonController controller;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private Person2GroupRepository person2GroupRepository;

	@Autowired
	private OrganizationStructureRepository orgStructureRepository;

	@Test
	void testGetForJournal() {
		// TODO
	}

	@Test
	void testAddPerson() throws Exception {
		PersonJournalDTO dto = new PersonJournalDTO();
		dto.firstName = getStringUUID();
		dto.lastName = getStringUUID();
		dto.baseSalaryPart = new BigDecimal(100);
		dto.startDate = LocalDate.now();
		dto.currentGroup = GroupType.Employee;

		String content = objectMapper.writeValueAsString(dto);

		mockMvc.perform(post(REST_PREFIX)
								.contentType("application/json")
								.content(content))
				.andExpect(status().isOk());

		List<Person> list = personRepository.findByLastNameContainingIgnoreCase(dto.lastName);
		assertEquals(1, list.size());

		Person result = list.get(0);
		assertEquals(result.getLastName(), dto.lastName);
		assertEquals(result.getFirstName(), dto.firstName);
		assertEquals(result.getFirstDate(), dto.startDate);
		assertEquals(0, result.getBaseSalaryPart().compareTo(dto.baseSalaryPart));

		List<Person2Group> groups = person2GroupRepository.findByPersonOrderByPeriodStartAsc(result);
		assertEquals(1, groups.size());
		Person2Group group = groups.get(0);
		assertEquals(group.getGroupType(), dto.currentGroup);

		OrganizationStructure org = orgStructureRepository.findByPerson(result);
		assertNotNull(org);
		assertNull(org.getMaterializedPath());
	}

	@Test
	void testGetPerson() {
		// TODO
	}

	@Test
	void testUpdateMainInfo() {
		// TODO
	}



	@Test
	void testGetPossibleChiefs() throws Exception {
		PersonJournalDTO dtoEmployee = new PersonJournalDTO();
		dtoEmployee.firstName = getStringUUID();
		dtoEmployee.lastName = getStringUUID();
		dtoEmployee.baseSalaryPart = new BigDecimal(100);
		dtoEmployee.startDate = LocalDate.now();
		dtoEmployee.currentGroup = GroupType.Employee;

		controller.create(dtoEmployee);

		PersonJournalDTO dtoManager = new PersonJournalDTO();
		dtoManager.firstName = getStringUUID();
		dtoManager.lastName = getStringUUID();
		dtoManager.baseSalaryPart = new BigDecimal(100);
		dtoManager.startDate = LocalDate.now();
		dtoManager.currentGroup = GroupType.Manager;

		controller.create(dtoManager);

		PersonJournalDTO dtoSalesman = new PersonJournalDTO();
		dtoSalesman.firstName = getStringUUID();
		dtoSalesman.lastName = getStringUUID();
		dtoSalesman.baseSalaryPart = new BigDecimal(100);
		dtoSalesman.startDate = LocalDate.now();
		dtoSalesman.currentGroup = GroupType.Salesman;

		controller.create(dtoSalesman);

		PersonJournalDTO dtoManagerPast = new PersonJournalDTO();
		dtoManagerPast.firstName = getStringUUID();
		dtoManagerPast.lastName = getStringUUID();
		dtoManagerPast.baseSalaryPart = new BigDecimal(100);
		dtoManagerPast.startDate = LocalDate.now();
		dtoManagerPast.currentGroup = GroupType.Employee;

		Long managerPastId = controller.create(dtoManagerPast);

		Person2GroupDTO managerPastGroup = new Person2GroupDTO();
		managerPastGroup.groupType = GroupType.Manager;
		managerPastGroup.periodStart = LocalDate.of(2019, 1, 1);
		managerPastGroup.periodEnd = dtoManagerPast.startDate.minusDays(1);
		controller.addGroup(managerPastId, managerPastGroup);

		List<ComboboxItemDTO> result = getResult(
				get(REST_PREFIX + "get-possible-chiefs").contentType("application/json"),
				new TypeReference<List<ComboboxItemDTO>>() {
				});
		assertEquals(2, result.size());
		List<ComboboxItemDTO> managers = result.stream()
												 .filter(dto -> dto.name.equals(dtoManager.firstName + " " + dtoManager.lastName))
												 .collect(Collectors.toList());
		assertEquals(1, managers.size());

		List<ComboboxItemDTO> salesmen = result.stream()
												 .filter(dto -> dto.name.equals(dtoSalesman.firstName + " " + dtoSalesman.lastName))
												 .collect(Collectors.toList());
		assertEquals(1, salesmen.size());
	}

	@Test
	void testUpdateChief() {
		// TODO
	}

	@Test
	void testAddGroup() {
		// TODO
	}

	@Test
	void testUpdateGroup() {
		// TODO
	}

	@Test
	void testDeleteGroup() {
		// TODO
	}

	@Test
	void testGetPossibleSubordinates() {
		// TODO
	}

	@Test
	void testCalcSalaryOnDate() {
		// TODO
	}

	@Test
	void testCalcTotalSalaryOnDate() {
		// TODO
	}

}
