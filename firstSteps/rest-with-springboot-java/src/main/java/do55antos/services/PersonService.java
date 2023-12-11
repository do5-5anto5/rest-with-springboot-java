package do55antos.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import do55antos.controllers.PersonController;
import do55antos.data_vo_v1.PersonVO;
import do55antos.exceptions.RequiredObjectIsNullException;
import do55antos.exceptions.ResourceNotFoundException;
import do55antos.mapper.DozerMapper;
import do55antos.model.Person;
import do55antos.repositories.PersonRepository;
import jakarta.transaction.Transactional;

@Service
public class PersonService {
	
	private Logger logger = Logger.getLogger(PersonService.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	@Autowired
	PagedResourcesAssembler<PersonVO> assembler;
	
	public PagedModel<EntityModel<PersonVO>> findAll (Pageable pageable){
		
		logger.info("Finding All people!");
		
		var personPage = repository.findAll(pageable);
		
		var personVOsPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));
		personVOsPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
		
		Link link = linkTo(
				methodOn(PersonController.class)
					.findAll(pageable.getPageNumber(),
							pageable.getPageSize(),
							"asc")).withSelfRel();
		return assembler.toModel(personVOsPage, link);
	}
	
	public PagedModel<EntityModel<PersonVO>> findPersonByName (String firstName, Pageable pageable){
		
		logger.info("Finding people by name!");
		
		var personPage = repository.findPersonsByName(firstName, pageable);
		
		var personVOsPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));
		personVOsPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
		
		Link link = linkTo(
				methodOn(PersonController.class)
				.findAll(pageable.getPageNumber(),
						pageable.getPageSize(),
						"asc")).withSelfRel();
		return assembler.toModel(personVOsPage, link);
	}

	public PersonVO findById(Long id) {
		
		logger.info("Finding one person!");
		
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public PersonVO create(PersonVO person) {
		
		if(person == null) throw new RequiredObjectIsNullException();
		
		logger.info("Creating one person!");
		
		var entity = DozerMapper.parseObject(person, Person.class);
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public PersonVO update(PersonVO person) {
		
		if(person == null) throw new RequiredObjectIsNullException();

		logger.info("Updating one person!");
		
		var entity = repository.findById(person.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAdress(person.getAdress());
		entity.setGender(person.getGender());
		
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	@Transactional
	public PersonVO disablePerson(Long id) {
		
		logger.info("Disabling one person!");
		
		repository.disablePerson(id);
		
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one person!");
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		repository.delete(entity);
	}
	
	
}
