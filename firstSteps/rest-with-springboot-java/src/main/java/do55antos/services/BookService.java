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

import do55antos.controllers.BookController;
import do55antos.data_vo_v1.BookVO;
import do55antos.exceptions.RequiredObjectIsNullException;
import do55antos.exceptions.ResourceNotFoundException;
import do55antos.mapper.DozerMapper;
import do55antos.model.Book;
import do55antos.repositories.BookRepository;

@Service
public class BookService {
	
	private Logger logger = Logger.getLogger(BookService.class.getName());
	
	@Autowired
	BookRepository repository;
	
	@Autowired
	PagedResourcesAssembler<BookVO> assembler;
	
	public PagedModel<EntityModel<BookVO>> findAll (Pageable pageable){
		
		logger.info("Finding All book!");
		
		var booksPage = repository.findAll(pageable);
		
		var booksVOsPage = booksPage.map(b -> DozerMapper.parseObject(b, BookVO.class));		
		booksVOsPage.map(b ->
				b.add(linkTo(
						methodOn(BookController.class)
						.findById(b.getKey())).withSelfRel()));
		
		Link link = linkTo(methodOn(BookController.class).findAll(
				pageable.getPageNumber(),
				pageable.getPageSize(),
				"asc")).withSelfRel();
		return assembler.toModel(booksVOsPage, link);
	}

	public BookVO findById(Long id) {
		
		logger.info("Finding one book!");
		
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		var vo = DozerMapper.parseObject(entity, BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public BookVO create(BookVO book) {
		
		if(book == null) throw new RequiredObjectIsNullException();
		
		logger.info("Creating one book!");
		
		var entity = DozerMapper.parseObject(book, Book.class);
		var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public BookVO update(BookVO book) {
		
		if(book == null) throw new RequiredObjectIsNullException();

		logger.info("Updating one book!");
		
		var entity = repository.findById(book.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		entity.setTitle(book.getTitle());
		entity.setAuthor(book.getAuthor());
		entity.setLaunchDate(book.getLaunchDate());
		entity.setPrice(book.getPrice());
		
		var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one book!");
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		repository.delete(entity);
	}
	
	
}
