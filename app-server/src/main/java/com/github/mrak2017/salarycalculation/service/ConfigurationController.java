package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.model.configuration.Configuration;
import com.github.mrak2017.salarycalculation.repository.ConfigurationRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationController {

	private final ConfigurationRepository repository;

	ConfigurationController(ConfigurationRepository repository) {
		this.repository = repository;
	}

	public void create(String code, String value, String description) {
		Configuration conf = new Configuration();
		conf.setCode(code);
		conf.setValue(value);
		conf.setDescription(description);
		repository.save(conf);
	}
}
