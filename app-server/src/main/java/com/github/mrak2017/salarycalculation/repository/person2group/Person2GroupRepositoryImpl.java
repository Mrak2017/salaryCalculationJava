package com.github.mrak2017.salarycalculation.repository.person2group;

import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public class Person2GroupRepositoryImpl implements Person2GroupRepositoryCustom {

	@Override
	public Optional<Person2Group> getPersonGroupOnDate(Person person, LocalDate onDate) {
		return Optional.empty();
	}
}
