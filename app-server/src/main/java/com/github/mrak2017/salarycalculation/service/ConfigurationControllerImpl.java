package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.controller.dto.ConfigurationJournalDTO;
import com.github.mrak2017.salarycalculation.core.Exception.ResourceNotFoundException;
import com.github.mrak2017.salarycalculation.model.configuration.Configuration;
import com.github.mrak2017.salarycalculation.repository.ConfigurationRepository;
import com.github.mrak2017.salarycalculation.utils.StringUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public class ConfigurationControllerImpl implements ConfigurationController {

	private final ConfigurationRepository repository;

	ConfigurationControllerImpl(ConfigurationRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Configuration> getAll() {
		return repository.findAll();
	}

	@Override
	public Optional<Configuration> find(long id) {
		return repository.findById(id);
	}

	@Override
	public void create(ConfigurationJournalDTO dto) {
		if (repository.findConfigurationByCode(dto.code).isPresent()) {
			throw new IllegalArgumentException(String.format("Setting with code '%s' already exists.", dto.code));
		}

		Configuration conf = new Configuration();
		conf.setCode(dto.code);
		conf.setValue(dto.value);
		conf.setDescription(dto.description);
		repository.save(conf);
	}

	@Override
	public void update(ConfigurationJournalDTO dto) {
		Configuration conf = find(dto.id).orElseThrow(ResourceNotFoundException::new);

		Optional<Configuration> existed = repository.findConfigurationByCode(dto.code);
		if (existed.isPresent() && !conf.getId().equals(existed.get().getId())) {
			throw new IllegalArgumentException(String.format("Setting with code '%s' already exists.", dto.code));
		}

		conf.setCode(dto.code);
		conf.setValue(dto.value);
		conf.setDescription(dto.description);
		repository.save(conf);
	}

	@Override
	public void delete(long id) {
		repository.delete(find(id).orElseThrow(ResourceNotFoundException::new));
	}

	@Override
	public BigDecimal getOrDefault(String code, BigDecimal defaultValue) {
		Optional<Configuration> existed = repository.findConfigurationByCode(code);

		if (existed.isPresent()) {
			try {
				return StringUtil.parseBigDecimal(existed.get().getValue());
			} catch (ParseException e) {
				e.printStackTrace();
				throw new IllegalArgumentException(String.format("Unable to parse BigDecimal from '%s'",
						existed.get().getValue()));
			}
		} else {
			return defaultValue;
		}
	}

}
