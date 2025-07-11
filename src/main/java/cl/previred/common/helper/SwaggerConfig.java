package cl.previred.common.helper;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API Desafio Spring Boot",
        version = "1.0",
        description = "Gestión de tareas de  usuarios con autenticacion y token JWT",
        contact = @Contact(name = "Alvaro Vergara C", email = "alvaro.vergara.cl@gmail.com")
    )
)
public class SwaggerConfig {

}
