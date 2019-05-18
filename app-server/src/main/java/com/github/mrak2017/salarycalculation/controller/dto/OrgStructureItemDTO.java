package com.github.mrak2017.salarycalculation.controller.dto;

import com.github.mrak2017.salarycalculation.model.person.Person;

import java.util.List;

public class OrgStructureItemDTO {
	public Long id;
	public String firstName;
	public String lastName;
	public List<OrgStructureItemDTO> children;

	public OrgStructureItemDTO(Person person, List<OrgStructureItemDTO> hierarchy) {
		id = person.getId();
		firstName = person.getFirstName();
		lastName = person.getLastName();
		children = hierarchy;
	}
}
