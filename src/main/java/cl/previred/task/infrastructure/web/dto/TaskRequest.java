package cl.previred.task.infrastructure.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskRequest(
		Long id,
		
		@NotBlank(message = "El título no puede estar vacío")
		@Size(min = 3, max = 100, message = "El titulo debe tener entre 3 y 100 caracteres")
		String title,
		
		String description,
		
		@NotNull(message = "El ID del estado no puede ser nulo")
		@Min(value = 1, message = "El ID del estado debe ser un numero positivo")
		Integer stateId,
		
		@NotNull(message = "El ID del usuario no puede ser nulo")
		@Min(value = 1, message = "El ID del usuario debe ser un numero positivo")
		Long userId
){}
