package co.onclass.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Microservicio de capacidades",
        description = "Microservicio para gestionar las capacidades de los bootcamps"
))
public class SwaggerConfig {
}
