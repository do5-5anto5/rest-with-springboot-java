package do55antos.integrationtests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import do55antos.configs.TestConfigs;
import do55antos.data_vo_v1.security.TokenVO;
import do55antos.integrationtests.testcontainers.AbstractIntegrationTest;
import do55antos.integrationtests.vo.AccountCredentialsVO;
import do55antos.integrationtests.vo.BookVO;
import do55antos.integrationtests.vo.pagedmodels.PagedModelBook;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerXmlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static XmlMapper objectMapper;
	private static BookVO book;

	@BeforeAll
	public static void setup() {
		objectMapper = new XmlMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		book = new BookVO();
	}

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("jonathan", "admin123");

		var accessToken = given()
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_XML)
					.accept(TestConfigs.CONTENT_TYPE_XML)
				.body(user)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
								.as(TokenVO.class)
							.getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockBook();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.body(book)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
							.body()
								.asString();

		try {
			book = objectMapper.readValue(content, BookVO.class);
		} catch (JsonProcessingException e){
			e.printStackTrace();
		}

		assertNotNull(book.getId());
		assertTrue(book.getId() > 0);
		assertNotNull(book.getTitle());
		assertNotNull(book.getAuthor());
		assertNotNull(book.getPrice());
		assertEquals("Docker Deep Dive", book.getTitle());
		assertEquals("Nigel Poulton", book.getAuthor());
		assertEquals(BigDecimal.valueOf(55.99), book.getPrice());
	}

	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		book.setTitle("Docker Deep Dive - Updated");

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.body(book)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
							.body()
								.asString();

		BookVO updatedBook = objectMapper.readValue(content, BookVO.class);

		assertNotNull(updatedBook.getId());
		assertNotNull(updatedBook.getTitle());
		assertNotNull(updatedBook.getAuthor());
		assertNotNull(updatedBook.getPrice());
		assertEquals(updatedBook.getId(), book.getId());
		assertEquals("Docker Deep Dive - Updated", updatedBook.getTitle());
		assertEquals("Nigel Poulton", updatedBook.getAuthor());
		assertEquals(BigDecimal.valueOf(55.99), updatedBook.getPrice());
	}

	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockBook();

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.pathParam("id", book.getId())
					.when()
					.get("{id}")
				.then()
					.statusCode(200)
						.extract()
							.body()
								.asString();

		BookVO foundBook = objectMapper.readValue(content, new TypeReference<BookVO>() {});

		assertNotNull(foundBook.getId());
		assertEquals(foundBook.getId(), foundBook.getId());
		assertNotNull(foundBook.getTitle());
		assertNotNull(foundBook.getAuthor());
		assertNotNull(foundBook.getPrice());
		assertEquals(foundBook.getId(), book.getId());
		assertEquals("Docker Deep Dive - Updated", foundBook.getTitle());
		assertEquals("Nigel Poulton", foundBook.getAuthor());
		assertEquals(BigDecimal.valueOf(55.99), foundBook.getPrice());
	}

	@Test
	@Order(4)
	public void testDelete() throws JsonMappingException, JsonProcessingException {

		given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.pathParam("id", book.getId())
					.when()
					.delete("{id}")
				.then()
					.statusCode(204);
	}

	@Test
	@Order(5)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.queryParams("page", 3, "size", 10, "description", "asc")
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
							.body()
								.asString();

		PagedModelBook wrapper = objectMapper.readValue(content, PagedModelBook.class);
		var books = wrapper.getContent();

		BookVO foundBookOne = books.get(0);

		assertNotNull(foundBookOne.getId());
		assertNotNull(foundBookOne.getTitle());
		assertNotNull(foundBookOne.getAuthor());
		assertNotNull(foundBookOne.getPrice());
		assertTrue(foundBookOne.getId() > 0);
		
		assertEquals(180, foundBookOne.getId());
		assertEquals("Chicken Run", foundBookOne.getTitle());
		assertEquals("Ambur Exer", foundBookOne.getAuthor());
		assertEquals(Double.valueOf(79.53), foundBookOne.getPrice().doubleValue());
		
		BookVO foundBookFive = books.get(4);
		
		assertNotNull(foundBookFive.getId());
		assertNotNull(foundBookFive.getTitle());
		assertNotNull(foundBookFive.getAuthor());
		assertNotNull(foundBookFive.getPrice());
		assertTrue(foundBookFive.getId() > 0);
		
		assertEquals(624, foundBookFive.getId());
		assertEquals("True Heart Susie", foundBookFive.getTitle());
		assertEquals("Anett Moores", foundBookFive.getAuthor());
		assertEquals(Double.valueOf(82.72), foundBookFive.getPrice().doubleValue());
	}
	
	@Test
	@Order(5)
	public void testFindByAuthor() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.pathParam("author", "C.")
					.queryParams("page", 0, "size", 6, "description", "asc")
					.when()
				.get("/findBookByAuthor/{author}")
				.then()
					.statusCode(200)
					.extract()
						.body()
							.asString();
		
		PagedModelBook wrapper = objectMapper.readValue(content, PagedModelBook.class);
		var books = wrapper.getContent();
		
		BookVO foundBookOne = books.get(0);
		
		assertNotNull(foundBookOne.getId());
		assertNotNull(foundBookOne.getTitle());
		assertNotNull(foundBookOne.getAuthor());
		assertNotNull(foundBookOne.getPrice());
		assertTrue(foundBookOne.getId() > 0);
		
		assertEquals(1, foundBookOne.getId());
		assertEquals("Working effectively with legacy code", foundBookOne.getTitle());
		assertEquals("Michael C. Feathers", foundBookOne.getAuthor());
		assertEquals(Double.valueOf(49.00), foundBookOne.getPrice().doubleValue());
	}

	@Test
	@Order(7)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		given().spec(specificationWithoutToken)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
					.when()
					.get()
				.then()
					.statusCode(403);
	}

	@Test
	@Order(8)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.queryParams("page", 3, "size", 12, "dicrection", "asc")
					.when()
					.get()
				.then()
				.statusCode(200)
					.extract()
						.body()
							.asString();

		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost/api/book/v1/"));
		
		assertTrue(content.contains("<links><rel>first</rel><href>http://localhost/api/book/v1?limit=12&amp;direction=asc&amp;page=0&amp;size=12&amp;sort=author,asc</href></links>"));
		assertTrue(content.contains("<links><rel>prev</rel><href>http://localhost/api/book/v1?limit=12&amp;direction=asc&amp;page=2&amp;size=12&amp;sort=author,asc</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost/api/book/v1?page=3&amp;limit=12&amp;direction=asc</href></links>"));
		assertTrue(content.contains("<links><rel>next</rel><href>http://localhost/api/book/v1?limit=12&amp;direction=asc&amp;page=4&amp;size=12&amp;sort=author,asc</href></links>"));
		}
	
	private void mockBook() {
		book.setTitle("Docker Deep Dive");
		book.setAuthor("Nigel Poulton");
		book.setPrice(BigDecimal.valueOf(55.99));
		book.setLaunchDate(new Date());
	}

}
