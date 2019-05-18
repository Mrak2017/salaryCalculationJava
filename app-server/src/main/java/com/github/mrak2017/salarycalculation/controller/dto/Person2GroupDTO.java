package com.github.mrak2017.salarycalculation.controller.dto;

import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;

import java.time.LocalDate;

public class Person2GroupDTO {

	public Long id;
	public GroupType groupType;
	public LocalDate periodStart;
	public LocalDate periodEnd;

	public Person2GroupDTO(Person2Group group) {
		id = group.getId();
		groupType = group.getGroupType();
		periodStart = group.getPeriodStart();
		periodEnd = group.getPeriodEnd();
	}
}
