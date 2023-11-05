package do55antos.integrationtests.controller.withyaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import do55antos.configs.TestConfigs;
import do55antos.data_vo_v1.security.TokenVO;
import do55antos.integrationtests.controller.withyaml.mapper.YMLMapper;
import do55antos.integrationtests.testcontainers.AbstractIntegrationTest;
import do55antos.integrationtests.vo.AccountCredentialsVO;
import do55antos.integrationtests.vo.BookVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerYamlTest extends AbstractIntegrationTest {
	
	private static RequestSpecification specification;
	private static YMLMapper objectMapper;
	private static BookVO book;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new YMLMapper();
		book = new BookVO();
	}

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("jonathan", "admin123");
		
		var accessToken = given()
				.config(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(
												TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_YML)
					.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(user, objectMapper)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
								.as(TokenVO.class, objectMapper)
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

		var persistedBook = given().spec(specification)
				.config(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(
												TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.body(book, objectMapper)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
							.body()
								.as(BookVO.class, objectMapper);

		book = persistedBook;

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

		var persistedBook = given().spec(specification)
				.config(
					RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
							.encodeContentTypeAs(
								TestConfigs.CONTENT_TYPE_YML,
								ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.body(book, objectMapper)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
							.body()
								.as(BookVO.class, objectMapper);

		book = persistedBook;

		assertNotNull(book.getId());
		assertTrue(book.getId() > 0);
		assertNotNull(book.getTitle());
		assertNotNull(book.getAuthor());
		assertNotNull(book.getPrice());
		assertEquals("Docker Deep Dive - Updated", book.getTitle());
		assertEquals("Nigel Poulton", book.getAuthor());
		assertEquals(BigDecimal.valueOf(55.99), book.getPrice());
	}

	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockBook();

		var foundBook = given().spec(specification)
				.config(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(
												TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.pathParam("id", book.getId())
					.when()
					.get("{id}")
				.then()
					.statusCode(200)
						.extract()
							.body()
								.as(BookVO.class, objectMapper);


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
				.config(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(
												TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
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
				.config(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(
												TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.when()
					.get()
				.then()
				.statusCode(200)
					.extract()
						.body()
							.as(BookVO[].class, objectMapper);

		List<BookVO> books = Arrays.asList(content);

		BookVO foundBookOne = books.get(0);

		assertNotNull(foundBookOne.getId());
		assertNotNull(foundBookOne.getTitle());
		assertNotNull(foundBookOne.getAuthor());
		assertNotNull(foundBookOne.getPrice());
		assertTrue(foundBookOne.getId() > 0);
		assertEquals("Working effectively with legacy code", foundBookOne.getTitle());
		assertEquals("Michael C. Feathers", foundBookOne.getAuthor());
		assertEquals(Double.valueOf(49.00), foundBookOne.getPrice().doubleValue());

		BookVO foundBookFive = books.get(4);

		assertNotNull(foundBookFive.getId());
		assertNotNull(foundBookFive.getTitle());
		assertNotNull(foundBookFive.getAuthor());
		assertNotNull(foundBookFive.getPrice());
		assertTrue(foundBookFive.getId() > 0);
		assertEquals("Code complete", foundBookFive.getTitle());
		assertEquals("Steve McConnell", foundBookFive.getAuthor());
		assertEquals(Double.valueOf(58.0), foundBookFive.getPrice().doubleValue());
	}

	@Test
	@Order(6)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		given().spec(specificationWithoutToken)
				.config(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(
												TestConfigs.CONTENT_TYPE_YML,
												ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.when()
					.get()
				.then()
					.statusCode(403);
	}

	private void mockBook() {
		book.setTitle("Docker Deep Dive");
		book.setAuthor("Nigel Poulton");
		book.setPrice(BigDecimal.valueOf(55.99));
		book.setLaunchDate(new Date());
	}

}
