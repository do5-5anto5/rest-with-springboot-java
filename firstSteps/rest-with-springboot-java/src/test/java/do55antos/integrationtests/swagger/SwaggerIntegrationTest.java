package do55antos.integrationtests.swagger;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import do55antos.configs.TestConfigs;
import do55antos.integrationtests.testcontainers.AbstractIntegrationTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

	@Test
	public void shoudDisplaySwaggerUiPage() {
		var content =
			given()
					.basePath("/swagger-ui/index.html")
					.port(TestConfigs.SERVER_PORT)
					.when()
							.get()
					.then()
							.statusCode(200)
					.extract()
							.body()
									.asString();
		assertTrue(content.contains("Swagger UI"));
	}

}
