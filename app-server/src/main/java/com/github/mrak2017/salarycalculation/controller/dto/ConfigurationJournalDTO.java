package com.github.mrak2017.salarycalculation.controller.dto;

import com.github.mrak2017.salarycalculation.model.configuration.Configuration;

import java.time.LocalDateTime;

/**
 * DTO for single journal row
 */
public class ConfigurationJournalDTO {

	public Long id;
	public LocalDateTime insertDate;
	public LocalDateTime updateDate;
	public String code;
	public String value;
	public String description;

	public ConfigurationJournalDTO() {
	}

	public ConfigurationJournalDTO(Configuration configuration) {
		id = configuration.getId();
		insertDate = configuration.getInsertDate();
		updateDate = configuration.getUpdateDate();
		code = configuration.getCode();
		value = configuration.getValue();
		description = configuration.getDescription();
	}
}
