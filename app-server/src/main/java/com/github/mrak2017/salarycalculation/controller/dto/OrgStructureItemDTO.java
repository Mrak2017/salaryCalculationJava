package com.github.mrak2017.salarycalculation.controller.dto;

import com.github.mrak2017.salarycalculation.model.person.Person;

import java.util.List;

/**
 * Hierarchical DTO for tree-representation
 */
public class OrgStructureItemDTO {
	public Long personId;
	public String firstName;
	public String lastName;
	public List<OrgStructureItemDTO> children;

	public OrgStructureItemDTO() {
	}

	public OrgStructureItemDTO(Person person, List<OrgStructureItemDTO> hierarchy) {
		personId = person.getId();
		firstName = person.getFirstName();
		lastName = person.getLastName();
		children = hierarchy;
	}
}
