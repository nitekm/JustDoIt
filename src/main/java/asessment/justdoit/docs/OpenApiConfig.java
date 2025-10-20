package asessment.justdoit.docs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("JustDoIt Task Management API")
						.description("A reactive REST API for managing tasks")
						.version("v1.0.0")
				).servers(List.of(
						new Server().url("http://localhost:8080/")
								.description("Local server for development"))
				);
	}
}
