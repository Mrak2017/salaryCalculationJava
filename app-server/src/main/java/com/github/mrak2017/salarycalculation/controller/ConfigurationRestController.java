package com.github.mrak2017.salarycalculation.controller;

import com.github.mrak2017.salarycalculation.controller.dto.ConfigurationJournalDTO;
import com.github.mrak2017.salarycalculation.core.Exception.ResourceNotFoundException;
import com.github.mrak2017.salarycalculation.repository.ConfigurationRepository;
import com.github.mrak2017.salarycalculation.service.ConfigurationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/configuration/")
public class ConfigurationRestController {

	@Autowired
	private ConfigurationController controller;

	private final ConfigurationRepository repository;

	ConfigurationRestController(ConfigurationRepository repository) {
		this.repository = repository;
	}

	@GetMapping("journal")
	List<ConfigurationJournalDTO> getForJournal() {
		return repository.findAll()
					   .stream()
					   .map(ConfigurationJournalDTO::new)
					   .collect(Collectors.toList());
	}

	@GetMapping("{id}")
	ConfigurationJournalDTO getConfiguration(@PathVariable long id) {
		return new ConfigurationJournalDTO(repository.findById(id)
												   .orElseThrow(ResourceNotFoundException::new));
	}

	@PostMapping()
	void addConfiguration(@RequestBody ConfigurationJournalDTO dto) {
		controller.create(dto.code, dto.value, dto.description);
	}

	@PutMapping("{id}")
	void updateConfiguration(@RequestBody ConfigurationJournalDTO dto) {
		//controller.create(dto.code, dto.value, dto.description);
	}

	@DeleteMapping("{id}")
	void deleteConfiguration(@PathVariable long id) {
		//controller.create(dto.code, dto.value, dto.description);
	}
}
