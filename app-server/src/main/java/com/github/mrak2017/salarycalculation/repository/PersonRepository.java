package com.github.mrak2017.salarycalculation.repository;

import com.github.mrak2017.salarycalculation.model.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
	List<Person> findByLastNameContainingIgnoreCase(String lastName);
}
