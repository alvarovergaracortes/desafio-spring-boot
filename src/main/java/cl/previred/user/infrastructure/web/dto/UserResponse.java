package cl.previred.user.infrastructure.web.dto;

public record UserResponse(
    Long id,
	String username,
	String roles
){}
