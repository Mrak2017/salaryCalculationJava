package com.github.mrak2017.salarycalculation.repository.person2group;

import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Person2GroupRepository extends JpaRepository<Person2Group, Long>, Person2GroupRepositoryCustom {
	List<Person2Group> findByPersonByOrderByPeriodStartAsc(Person person);
}
