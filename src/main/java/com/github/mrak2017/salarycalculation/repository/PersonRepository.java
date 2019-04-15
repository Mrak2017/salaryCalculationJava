package com.github.mrak2017.salarycalculation.repository;

import com.github.mrak2017.salarycalculation.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
