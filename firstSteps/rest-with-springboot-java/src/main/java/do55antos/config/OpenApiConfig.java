package do55antos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenApi() {
		return new OpenAPI()
			.info(new Info()
					.title("RESTful API with Java 19 and Spring Boot 3")
					.version("v1")
					.description("Walking on the road to Glory of REST")
					.termsOfService("https://github.com/do5-5anto5/rest-with-springboot-java#readme")
					.license(
							new License()
							.name("Apache 2.0")
							.url("https://https://github.com/do5-5anto5")
							)
					);
	}
}
