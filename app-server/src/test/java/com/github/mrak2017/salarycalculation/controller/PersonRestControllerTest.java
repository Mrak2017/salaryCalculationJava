package com.github.mrak2017.salarycalculation.controller;


import com.github.mrak2017.salarycalculation.BaseTest;
import com.github.mrak2017.salarycalculation.controller.dto.ComboboxItemDTO;
import com.github.mrak2017.salarycalculation.controller.dto.Person2GroupDTO;
import com.github.mrak2017.salarycalculation.controller.dto.PersonDTO;
import com.github.mrak2017.salarycalculation.controller.dto.PersonJournalDTO;
import com.github.mrak2017.salarycalculation.core.Exception.UserErrorTemplate;
import com.github.mrak2017.salarycalculation.core.Exception.ResourceNotFoundException;
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

import javax.validation.ValidationException;
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
	private PersonRepository personRepository;

	@Autowired
	private Person2GroupRepository person2GroupRepository;

	@Autowired
	private OrganizationStructureRepository orgStructureRepository;

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

	/** Possible chiefs - all persons except:
	 * 1) Person itself
	 * 2) Person without group at this moment
	 * 3) Person group is different from Manager, Salesman
	 * 4) Person's subordinates of any level
	 * 5) Person's current chief
	 *
	 * This test checks 1st, 2nd and 3rd conditions */
	@Test
	void testGetPossibleChiefsWithGroupCheck() throws Exception {
		Person employee = createEmployee();
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
				get(REST_PREFIX + "get-possible-chiefs/" + employee.getId()).contentType("application/json"),
				ComboboxItemDTO.class
		);

		assertEquals(2, result.size());
		assertTrue(CheckUtil.listContainsByFunction(result,
				dto -> dto.name.equals(manager.getFirstName() + " " + manager.getLastName())));

		assertTrue(CheckUtil.listContainsByFunction(result,
				dto -> dto.name.equals(salesman.getFirstName() + " " + salesman.getLastName())));
	}

	/** Possible chiefs - all persons except:
	 * 1) Person itself
	 * 2) Person without group at this moment
	 * 3) Person group is different from Manager, Salesman
	 * 4) Person's subordinates of any level
	 * 5) Person's current chief
	 *
	 * This test checks 4th and 5th conditions */
	@Test
	void testGetPossibleChiefsWithoutSubordinates() throws Exception {
		Person chief = createManager();
		Person person = createManager();
		Person subordinate1Level = createManager();
		Person subordinate2Level = createManager();
		Person possibleChief = createManager();

		controller.updateChief(person.getId(), chief.getId());
		controller.updateChief(subordinate1Level.getId(), person.getId());
		controller.updateChief(subordinate2Level.getId(), subordinate1Level.getId());

		List<ComboboxItemDTO> result = getResultList(
				get(REST_PREFIX + "get-possible-chiefs/" + person.getId()).contentType("application/json"),
				ComboboxItemDTO.class
		);

		assertEquals(1, result.size());

		assertTrue(CheckUtil.listContainsByFunction(result,
				dto -> dto.name.equals(possibleChief.getFirstName() + " " + possibleChief.getLastName())));
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
	void testAddIncorrectGroup() {
		Person employee = createEmployee();

		Person2GroupDTO dto = new Person2GroupDTO();
		dto.periodStart = LocalDate.of(2018, 1, 1);
		dto.periodEnd = LocalDate.of(2030, 12, 31);
		dto.groupType = GroupType.Salesman;

		try {
			controller.addGroup(employee.getId(), dto);
			fail("There was no ValidationException thrown");
		} catch (ValidationException ex) {
			// Expected exception => test is ok
			assertTrue(
					ex.getMessage().contains(
							UserErrorTemplate.MORE_THAN_ONE_GROUP_ON_DATE_RANGE.getTemplate().substring(0, 55))
			);
		}
	}

	@Test
	void testGetGroup() throws Exception {
		Person employee = createEmployee();

		Person2GroupDTO dto = new Person2GroupDTO();
		dto.periodStart = LocalDate.of(2019, 1, 1);
		dto.periodEnd = LocalDate.of(2019, 1, 31);
		dto.groupType = GroupType.Employee;

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
	void testUpdateGroupCorrect() throws Exception {
		Person person = createEmployee();
		Person2Group p2g = controller.getCurrentGroup(person).orElseThrow(ResourceNotFoundException::new);

		Person2GroupDTO dto = new Person2GroupDTO();
		dto.id = p2g.getId();
		dto.periodStart = LocalDate.of(2019, 1, 1);
		dto.periodEnd = LocalDate.of(2019, 12, 31);
		dto.groupType = GroupType.Manager;

		String content = objectMapper.writeValueAsString(dto);

		mockMvc.perform(put(REST_PREFIX + "groups")
								.contentType("application/json")
								.content(content))
				.andExpect(status().isOk());

		Person2Group updated = controller.getGroupById(dto.id);

		assertNotNull(updated);
		assertEquals(dto.periodStart, updated.getPeriodStart());
		assertEquals(dto.periodEnd, updated.getPeriodEnd());
		assertEquals(dto.groupType, updated.getGroupType());

		assertNotEquals(p2g.getPeriodStart(), updated.getPeriodStart());
		assertNotEquals(p2g.getPeriodEnd(), updated.getPeriodEnd());
		assertNotEquals(p2g.getGroupType(), updated.getGroupType());
	}

	@Test
	void testUpdateGroupIncorrect() {
		// create person with default group (from now and without end date)
		Person person = createEmployee();
		Person2Group p2g = controller.getCurrentGroup(person).orElseThrow(ResourceNotFoundException::new);

		// add new group before default group period
		Person2GroupDTO correctGroupDTO = new Person2GroupDTO();
		correctGroupDTO.periodStart = LocalDate.of(2019, 1, 1);
		correctGroupDTO.periodEnd = LocalDate.of(2019, 1, 31);
		correctGroupDTO.groupType = GroupType.Employee;

		Long groupId = controller.addGroup(person.getId(), correctGroupDTO);
		assertNotNull(groupId);

		// add another group with date range intersect
		Person2GroupDTO incorrectGroupDTO = new Person2GroupDTO();
		incorrectGroupDTO.id = p2g.getId();
		incorrectGroupDTO.periodStart = LocalDate.of(2018, 1, 1);
		incorrectGroupDTO.periodEnd = LocalDate.of(2030, 12, 31);
		incorrectGroupDTO.groupType = GroupType.Manager;

		try {
			controller.updateGroup(incorrectGroupDTO);
			fail("There was no ValidationException thrown");
		} catch (ValidationException ex) {
			// Expected exception => test is ok
			assertTrue(
					ex.getMessage().contains(
							UserErrorTemplate.MORE_THAN_ONE_GROUP_ON_DATE_RANGE.getTemplate().substring(0, 55))
			);
		}
	}

	@Test
	void testDeleteGroup() throws Exception {
		Person person = createEmployee();
		Person2Group p2g = controller.getCurrentGroup(person).orElseThrow(ResourceNotFoundException::new);

		mockMvc.perform(delete(REST_PREFIX + "groups/" + p2g.getId())
								.contentType("application/json"))
				.andExpect(status().isOk());

		try {
			Person2Group deleted = controller.getGroupById(p2g.getId());
			fail("There was no ResourceNotFoundException thrown");
		} catch (ResourceNotFoundException ex) {
			// Expected exception => test is ok
		}
	}

	/** Possible subordinates - all persons except:
	 * 1) Person itself
	 * 2) Person without group at this moment
	 * 3) First level subordinates
	 * 4) Person's chiefs of any level of hierarchy*/
	@Test
	void testGetPossibleSubordinates() throws Exception {
		Person person = createManager();
		Person chief = createManager();
		Person subordinate = createEmployee();
		Person possibleSubordinate = createEmployee();

		Person personWithoutGroup = createEmployee();
		Person2Group p2g = controller.getCurrentGroup(personWithoutGroup).orElseThrow(ResourceNotFoundException::new);
		controller.deleteGroup(p2g.getId());

		controller.updateChief(person.getId(), chief.getId());
		controller.updateChief(subordinate.getId(), person.getId());

		List<ComboboxItemDTO> result = getResultList(
				get(REST_PREFIX + person.getId() + "/get-possible-subordinates")
						.contentType("application/json"),
				ComboboxItemDTO.class
		);

		assertEquals(1, result.size());
		assertTrue(CheckUtil.listContainsByFunction(result,
				dto -> dto.name.equals(possibleSubordinate.getFirstName() + " " + possibleSubordinate.getLastName())));
	}

}
