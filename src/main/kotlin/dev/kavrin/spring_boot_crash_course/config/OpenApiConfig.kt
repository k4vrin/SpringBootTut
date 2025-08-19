package dev.kavrin.spring_boot_crash_course.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Central OpenAPI / Swagger configuration.
 * JSON spec: /v3/api-docs
 * Swagger UI: /swagger-ui/index.html
 */
@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        val bearerScheme = SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .description("Provide the JWT access token, e.g. Bearer eyJhbGciOiJI...")

        return OpenAPI()
            .info(
                Info()
                    .title("Spring Boot Crash Course API")
                    .version("1.0.0")
                    .description("API for managing notes and authentication (JWT).")
                    .contact(Contact().name("API Support"))
            )
            .components(
                Components().addSecuritySchemes("bearerAuth", bearerScheme)
            )
            .addSecurityItem(SecurityRequirement().addList("bearerAuth"))
    }
}
