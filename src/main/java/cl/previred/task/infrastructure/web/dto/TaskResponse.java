package cl.previred.task.infrastructure.web.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Respuesta del servicio con datos de una tarea")
public record TaskResponse(
    Long id,
    
    @Schema(description = "Título de la tarea", example = "Actualizar documentación")
    @NotBlank(message = "El título no puede estar en blanco")
    @Size(max = 100, message = "El título no puede tener más de 100 caracteres")
    String title,
    
    String description,
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime creationDate,
    
    Integer stateId,
    Long userId
){}
