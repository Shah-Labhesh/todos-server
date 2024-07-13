package com.labhesh.Todos.Todos;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@SecurityScheme(
		name = "auth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT"
)

@SpringBootApplication
public class TodosApplication {
	@Value("${swagger.api.title}")
	private String title;
	@Value("${swagger.api.description}")
	private String description;
	@Value("${swagger.api.version}")
	private String version;
	@Value("${swagger.api.termsOfServiceUrl}")
	private String termsOfServiceUrl;
	@Value("${swagger.api.contact.name}")
	private String contactName;
	@Value("${swagger.api.contact.url}")
	private String contactUrl;

	@Value("${application.timezone}")
	private String applicationTimeZone;

	public static void main(String[] args) {
		SpringApplication.run(TodosApplication.class, args);
	}

	@PostConstruct
	public void executeAfterMain() {
		TimeZone.setDefault(TimeZone.getTimeZone(applicationTimeZone));
		System.out.println("Application Timezone set to : " + applicationTimeZone);
		System.out.println("Application started at : " + java.time.LocalDateTime.now());
	}


	// swagger

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
//				.addSecurityItem(new SecurityRequirement().
//						addList("Bearer Authentication"))
//				.components(new Components().addSecuritySchemes
//						("Bearer Authentication", createAPIKeyScheme()))
//				.servers(Arrays.asList(
//								new Server().url("http://localhost:8080")
//						)
//				)
				.info(new Info()
						.title(title)
						.version(version)
						.description(description)
						.termsOfService(termsOfServiceUrl)
						.contact(new Contact().name(contactName).url(contactUrl))
						.license(new License().name("Apache 2.0").url("https://springdoc.org")));
	}


}
