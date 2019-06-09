package com.github.mrak2017.salarycalculation.controller;


import com.github.mrak2017.salarycalculation.BaseTest;
import com.github.mrak2017.salarycalculation.controller.dto.PersonJournalDTO;
import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.service.PersonController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PersonRestControllerTest extends BaseTest {

	@Autowired
	private PersonController controller;

	@Test
	void testPost() throws Exception {
		PersonJournalDTO dto = new PersonJournalDTO();
		dto.firstName = "1";
		dto.lastName = "2";
		dto.baseSalaryPart = BigDecimal.valueOf(100);
		dto.startDate = LocalDate.now();
		dto.currentGroup = GroupType.Employee;

		String content = objectMapper.writeValueAsString(dto);

		mockMvc.perform(post("/api/persons/")
								.contentType("application/json")
								.content(content))
				.andExpect(status().isOk());

		List<Person> list = controller.findAll("");
		list.size();
	}

	@Test
	void testGet() throws Exception {

		/*controller.create(dto);

		Person person = new Person();
		person.setId(1L);

		when(controller.find(1L)).thenReturn(Optional.of(person));*/

		mockMvc.perform(get("/api/persons/{id}", 1)
								.contentType("application/json"))
				.andExpect(status().isOk());
	}
}
