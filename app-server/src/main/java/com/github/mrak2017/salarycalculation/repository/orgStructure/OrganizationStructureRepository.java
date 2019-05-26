package com.github.mrak2017.salarycalculation.repository.orgStructure;

import com.github.mrak2017.salarycalculation.model.person.OrganizationStructure;
import com.github.mrak2017.salarycalculation.model.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationStructureRepository extends JpaRepository<OrganizationStructure, Long> {
	OrganizationStructure findByPerson(Person person);

	@Query(value = "SELECT * FROM sc_organization_structure WHERE materialized_path <@ CAST(:pathToSearch AS ltree)", nativeQuery = true)
	List<OrganizationStructure> findAllByPath(@Param("pathToSearch") String pathToSearch);

	@Query(value = "SELECT * FROM sc_organization_structure WHERE materialized_path = CAST(:pathToSearch AS ltree)", nativeQuery = true)
	List<OrganizationStructure> findFirstLevelByPath(@Param("pathToSearch") String pathToSearch);
}
