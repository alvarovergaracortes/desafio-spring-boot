package cl.previred.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Clase para el manejo de errores con el formato JSON")
public record ErrorResponse(
		@Schema(description = "Mensaje de error explicativo", example = "Contraseña incorrecta")
		String mensaje
) {}
