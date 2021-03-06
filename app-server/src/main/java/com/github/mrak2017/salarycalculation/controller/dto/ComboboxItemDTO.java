package com.github.mrak2017.salarycalculation.controller.dto;

import com.github.mrak2017.salarycalculation.model.person.Person;

/**
 * DTO for client combobox choosers
 */
public class ComboboxItemDTO {

	public long id;
	public String name;

	public ComboboxItemDTO() {
	}

	public ComboboxItemDTO(Person person) {
		id = person.getId();
		name = person.getFullName();
	}
}
