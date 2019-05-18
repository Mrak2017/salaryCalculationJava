package com.github.mrak2017.salarycalculation.controller;

import com.github.mrak2017.salarycalculation.controller.dto.ConfigurationJournalDTO;
import com.github.mrak2017.salarycalculation.core.Exception.ResourceNotFoundException;
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
