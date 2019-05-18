package com.github.mrak2017.salarycalculation.repository;

import com.github.mrak2017.salarycalculation.model.configuration.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
	Optional<Configuration> findConfigurationByCode(String code);
}
