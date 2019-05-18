package com.github.mrak2017.salarycalculation.repository;

import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Person2GroupRepository extends JpaRepository<Person2Group, Long>, Person2GroupRepositoryCustom {
}
