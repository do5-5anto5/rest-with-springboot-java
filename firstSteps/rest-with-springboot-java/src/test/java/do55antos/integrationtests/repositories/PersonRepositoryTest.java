package do55antos.integrationtests.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import do55antos.model.Person;
import do55antos.repositories.PersonRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest {

	@Autowired
	PersonRepository repository;

	private static Person person;

	@BeforeAll
	public static void setup() {
		person = new Person();
	}

	@Test
	@Order(0)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {

		Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "firstName"));

		person = repository.findPersonsByName("aa", pageable).getContent().get(0);

		assertNotNull(person.getId());
		assertNotNull(person.getFirstName());
		assertNotNull(person.getLastName());
		assertNotNull(person.getGender());
		assertNotNull(person.getAdress());
		assertTrue(person.getEnabled());

		assertEquals(834, person.getId());

		assertEquals("Aaren", person.getFirstName());
		assertEquals("Lukianov", person.getLastName());
		assertEquals("24 Mcguire Circle", person.getAdress());
		assertEquals("Female", person.getGender());
	}
	
	@Test
	@Order(1)
	public void testDisablePerson() throws JsonMappingException, JsonProcessingException {
		
		repository.disablePerson(person.getId());
		
		Pageable pageable = PageRequest.of(0, 6, Sort.by(Direction.ASC, "firstName"));
		
		person = repository.findPersonsByName("aa", pageable).getContent().get(0);
		
		assertNotNull(person.getId());
		assertNotNull(person.getFirstName());
		assertNotNull(person.getLastName());
		assertNotNull(person.getGender());
		assertNotNull(person.getAdress());
		assertFalse(person.getEnabled());
		
		assertEquals(834, person.getId());
		
		assertEquals("Aaren", person.getFirstName());
		assertEquals("Lukianov", person.getLastName());
		assertEquals("24 Mcguire Circle", person.getAdress());
		assertEquals("Female", person.getGender());
	}
}