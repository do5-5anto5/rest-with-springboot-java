package do55antos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import do55antos.data_vo_v1.PersonVO;
import do55antos.services.PersonService;
import do55antos.util.MediaType;

@RestController
@RequestMapping("/api/person/v1")
public class PersonController {
	
	@Autowired
	private PersonService service;
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public List<PersonVO> findAll(){
		return service.findAll();
	}
	
	@GetMapping(value = "/{id}", produces = { 
			MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public PersonVO findById(@PathVariable(value = "id") Long id) {		
		return service.findById(id);
	}
	
	@PostMapping(
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,	MediaType.APPLICATION_YML },
			consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,	MediaType.APPLICATION_YML })	
	public PersonVO create(@RequestBody PersonVO person) {		
		return service.create(person);
	}
	
	@PutMapping(
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,	MediaType.APPLICATION_YML },
			consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,	MediaType.APPLICATION_YML })	
	public PersonVO update(@RequestBody PersonVO person) {		
		return service.update(person);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {		
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
}
