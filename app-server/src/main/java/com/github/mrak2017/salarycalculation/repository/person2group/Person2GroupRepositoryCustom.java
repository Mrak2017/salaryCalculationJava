package com.github.mrak2017.salarycalculation.repository.person2group;

import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface Person2GroupRepositoryCustom {
	Optional<Person2Group> getPersonGroupOnDate(Person person, LocalDate onDate);

	List<Person2Group> getExistingGroups(Person person, Person2Group p2g);

	List<Person> getPersonListWithCurrentGroupExists(List<Person> excludedPersons, List<GroupType> possibleGroups);
}
