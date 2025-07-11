package cl.previred.user.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
	@NotBlank(message = "El nombre de usuario no puede estar vacío")
	@NotNull
    String username,
    
	@NotBlank(message = "La contraseña no puede estar vacía")
	@NotNull
	String password,
	
    @NotBlank(message = "Debe agregar un role")
	@NotNull
	String roles
    
) {}
