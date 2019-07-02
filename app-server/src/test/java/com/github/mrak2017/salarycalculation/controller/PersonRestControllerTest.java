package com.github.mrak2017.salarycalculation.controller;


import com.github.mrak2017.salarycalculation.BaseTest;
import com.github.mrak2017.salarycalculation.controller.dto.ComboboxItemDTO;
import com.github.mrak2017.salarycalculation.controller.dto.Person2GroupDTO;
import com.github.mrak2017.salarycalculation.controller.dto.PersonDTO;
import com.github.mrak2017.salarycalculation.controller.dto.PersonJournalDTO;
import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.OrganizationStructure;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import com.github.mrak2017.salarycalculation.repository.PersonRepository;
import com.github.mrak2017.salarycalculation.repository.orgStructure.OrganizationStructureRepository;
import com.github.mrak2017.salarycalculation.repository.person2group.Person2GroupRepository;
import com.github.mrak2017.salarycalculation.service.PersonController;
import com.github.mrak2017.salarycalculation.utils.CheckUtil;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PersonRestControllerTest extends BaseTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private static final String REST_PREFIX = "/api/persons/";

	@Autowired
	private PersonController controller;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private Person2GroupRepository person2GroupRepository;

	@Autowired
	private OrganizationStructureRepository orgStructureRepository;

	private Person createEmployee() {
		PersonJournalDTO dtoEmployee = new PersonJournalDTO();
		dtoEmployee.firstName = getStringUUID();
		dtoEmployee.lastName = getStringUUID();
		dtoEmployee.baseSalaryPart = new BigDecimal(100);
		dtoEmployee.startDate = LocalDate.now();
		dtoEmployee.currentGroup = GroupType.Employee;

		Long id = controller.create(dtoEmployee);
		Person result = controller.find(id).orElse(null);
		assertNotNull(result);
		return result;
	}

	private Person createManager() {
		PersonJournalDTO dtoManager = new PersonJournalDTO();
		dtoManager.firstName = getStringUUID();
		dtoManager.lastName = getStringUUID();
		dtoManager.baseSalaryPart = new BigDecimal(100);
		dtoManager.startDate = LocalDate.now();
		dtoManager.currentGroup = GroupType.Manager;

		Long id = controller.create(dtoManager);
		Person result = controller.find(id).orElse(null);
		assertNotNull(result);
		return result;
	}

	private Person createSalesman() {
		PersonJournalDTO dtoSalesman = new PersonJournalDTO();
		dtoSalesman.firstName = getStringUUID();
		dtoSalesman.lastName = getStringUUID();
		dtoSalesman.baseSalaryPart = new BigDecimal(100);
		dtoSalesman.startDate = LocalDate.now();
		dtoSalesman.currentGroup = GroupType.Salesman;

		Long id = controller.create(dtoSalesman);
		Person result = controller.find(id).orElse(null);
		assertNotNull(result);
		return result;
	}

	@Test
	void testGetForJournal() throws Exception {
		Person person1 = createEmployee();
		Person person2 = createEmployee();
		Person person3 = createEmployee();
		Person person4 = createManager();
		Person person5 = createSalesman();

		List<PersonJournalDTO> result = getResultList(
				get(REST_PREFIX + "journal").contentType("application/json"),
				PersonJournalDTO.class
		);

		assertEquals(5, result.size());
		assertTrue(CheckUtil.listContainsByFunction(result, dto -> dto.firstName.equals(person1.getFirstName())));
		assertTrue(CheckUtil.listContainsByFunction(result, dto -> dto.firstName.equals(person2.getFirstName())));
		assertTrue(CheckUtil.listContainsByFunction(result, dto -> dto.firstName.equals(person3.getFirstName())));
		assertTrue(CheckUtil.listContainsByFunction(result, dto -> dto.firstName.equals(person4.getFirstName())));
		assertTrue(CheckUtil.listContainsByFunction(result, dto -> dto.firstName.equals(person5.getFirstName())));
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
	void testGetPerson() throws Exception {
		Person person = createEmployee();

		PersonDTO result = getResult(
				get(REST_PREFIX + person.getId()).contentType("application/json"),
				PersonDTO.class
		);

		assertNotNull(result);
		assertEquals(person.getFirstName(), result.firstName);
		assertEquals(person.getLastName(), result.lastName);
		assertEquals(person.getFirstDate(), result.startDate);
		assertEquals(person.getLastDate(), result.endDate);
		assertEquals(person.getBaseSalaryPart(), result.baseSalaryPart);
	}

	@Test
	void testUpdateMainInfo() throws Exception {
		Person person = createEmployee();
		PersonDTO dto = new PersonDTO(person);
		dto.firstName = getStringUUID();
		dto.lastName = getStringUUID();
		dto.startDate = LocalDate.of(2019, 1, 1);
		dto.endDate = LocalDate.of(2019, 12, 31);
		dto.baseSalaryPart = new BigDecimal(999);

		String content = objectMapper.writeValueAsString(dto);

		mockMvc.perform(put(REST_PREFIX + "update-main-info")
								.contentType("application/json")
								.content(content))
				.andExpect(status().isOk());

		Person result = personRepository.findById(person.getId()).orElse(null);
		assertNotNull(result);

		assertEquals(dto.firstName, result.getFirstName());
		assertEquals(dto.lastName, result.getLastName());
		assertEquals(dto.startDate, result.getFirstDate());
		assertEquals(dto.endDate, result.getLastDate());
		assertEquals(0, result.getBaseSalaryPart().compareTo(dto.baseSalaryPart));
	}

	@Test
	void testGetPossibleChiefs() throws Exception {
		createEmployee();
		Person manager = createManager();
		Person salesman = createSalesman();

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

		List<ComboboxItemDTO> result = getResultList(
				get(REST_PREFIX + "get-possible-chiefs").contentType("application/json"),
				ComboboxItemDTO.class
		);

		assertEquals(2, result.size());
		assertTrue(CheckUtil.listContainsByFunction(result,
				dto -> dto.name.equals(manager.getFirstName() + " " + manager.getLastName())));

		assertTrue(CheckUtil.listContainsByFunction(result,
				dto -> dto.name.equals(salesman.getFirstName() + " " + salesman.getLastName())));
	}

	@Test
	void testUpdateChief() throws Exception {
		Person employee = createEmployee();
		Person manager = createManager();

		String url = String.format("%d/new-chief/%d", employee.getId(), manager.getId());
		mockMvc.perform(put(REST_PREFIX + url)
								.contentType("application/json"))
				.andExpect(status().isOk());

		Person resultChief = controller.getCurrentChief(employee).orElse(null);
		assertNotNull(resultChief);
		assertEquals(manager.getFirstName(), resultChief.getFirstName());
		assertEquals(manager.getLastName(), resultChief.getLastName());
	}

	@Test
	void testAddCorrectGroup() throws Exception {
		Person employee = createEmployee();

		Person2GroupDTO dto = new Person2GroupDTO();
		dto.periodStart = LocalDate.of(2019, 1, 1);
		dto.periodEnd = LocalDate.of(2019, 1, 31);
		dto.groupType = GroupType.Salesman;

		String content = objectMapper.writeValueAsString(dto);

		String url = employee.getId() + "/add-group";
		mockMvc.perform(post(REST_PREFIX + url)
								.contentType("application/json")
								.content(content))
				.andExpect(status().isOk());

		List<Person2Group> list = controller.getAllGroups(employee);

		assertNotNull(list);
		assertEquals(2, list.size());

		list.sort(Comparator.comparing(Person2Group::getPeriodStart));

		assertEquals(dto.groupType, list.get(0).getGroupType());
		assertEquals(dto.periodStart, list.get(0).getPeriodStart());

		assertEquals(GroupType.Employee, list.get(1).getGroupType());
		assertEquals(LocalDate.now(), list.get(1).getPeriodStart());
	}

	@Test
	void testGetGroup() throws Exception {
		Person employee = createEmployee();

		Person2GroupDTO dto = new Person2GroupDTO();
		dto.periodStart = LocalDate.of(2019, 1, 1);
		dto.periodEnd = LocalDate.of(2019, 1, 31);
		dto.groupType = GroupType.Salesman;

		Long groupId = controller.addGroup(employee.getId(), dto);

		assertNotNull(groupId);

		Person2GroupDTO result = getResult(
				get(REST_PREFIX + "groups/" + groupId).contentType("application/json"),
				Person2GroupDTO.class
		);

		assertNotNull(result);
		assertEquals(dto.periodStart, result.periodStart);
		assertEquals(dto.periodEnd, result.periodEnd);
		assertEquals(dto.groupType, result.groupType);
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
