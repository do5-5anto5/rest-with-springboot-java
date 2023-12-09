package do55antos.integrationtests.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import do55antos.integrationtests.testcontainers.AbstractIntegrationTest;
import do55antos.model.Book;
import do55antos.repositories.BookRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest extends AbstractIntegrationTest {

	@Autowired
	BookRepository repository;

	private static Book book;

	@BeforeAll
	public static void setup() {
		book = new Book();
	}

	@Test
	public void testFindByName() throws JsonMappingException, JsonProcessingException {

		Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "author"));

		book = repository.findBooksByAuthor("C.", pageable).getContent().get(0);

		assertNotNull(book.getId());
		assertNotNull(book.getTitle());
		assertNotNull(book.getAuthor());
		assertNotNull(book.getPrice());
		assertTrue(book.getId() > 0);
		
		assertEquals(1, book.getId());
		assertEquals("Working effectively with legacy code", book.getTitle());
		assertEquals("Michael C. Feathers", book.getAuthor());
		assertEquals(Double.valueOf(49.00), book.getPrice().doubleValue());
	}
	
}