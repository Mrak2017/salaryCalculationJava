package com.github.mrak2017.salarycalculation.controller;

import com.github.mrak2017.salarycalculation.controller.dto.ConfigurationJournalDTO;
import com.github.mrak2017.salarycalculation.core.Exception.ResourceNotFoundException;
import com.github.mrak2017.salarycalculation.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rest-service for app-client requests.
 * Allows to get data for journal of configurations, also have other CRUD-methods
 */
@RestController
@RequestMapping("api/configuration/")
public class ConfigurationRestController {

	@Autowired
	private ConfigurationService controller;

	@GetMapping("journal")
	List<ConfigurationJournalDTO> getForJournal() {
		return controller.getAll()
					   .stream()
					   .map(ConfigurationJournalDTO::new)
					   .collect(Collectors.toList());
	}

	@GetMapping("{id}")
	ConfigurationJournalDTO getConfiguration(@PathVariable long id) {
		return new ConfigurationJournalDTO(controller.find(id)
												   .orElseThrow(ResourceNotFoundException::new));
	}

	@PostMapping()
	void addConfiguration(@RequestBody ConfigurationJournalDTO dto) {
		controller.create(dto);
	}

	@PutMapping()
	void updateConfiguration(@RequestBody ConfigurationJournalDTO dto) {
		controller.update(dto);
	}

	@DeleteMapping("{id}")
	void deleteConfiguration(@PathVariable long id) {
		controller.delete(id);
	}
}
