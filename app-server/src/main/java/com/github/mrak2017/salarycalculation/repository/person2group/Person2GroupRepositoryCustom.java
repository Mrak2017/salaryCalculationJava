package com.github.mrak2017.salarycalculation.repository.person2group;

import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;

import java.time.LocalDate;
import java.util.Optional;

public interface Person2GroupRepositoryCustom {
	Optional<Person2Group> getPersonGroupOnDate(Person person, LocalDate onDate);
}
