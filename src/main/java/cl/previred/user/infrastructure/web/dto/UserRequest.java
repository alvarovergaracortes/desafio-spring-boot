package cl.previred.user.infrastructure.web.dto;

public record UserRequest(
		Long id,
		String username,
		String password,
		String roles
){}
