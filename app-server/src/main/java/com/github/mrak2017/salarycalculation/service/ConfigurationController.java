package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.controller.dto.ConfigurationJournalDTO;
import com.github.mrak2017.salarycalculation.model.configuration.Configuration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ConfigurationController {

	List<Configuration> getAll();

	Optional<Configuration> find(long id);

	void create(ConfigurationJournalDTO dto);

	void update(ConfigurationJournalDTO dto);

	void delete(long id);

	BigDecimal getOrDefault(String code, BigDecimal defaultValue);
}
