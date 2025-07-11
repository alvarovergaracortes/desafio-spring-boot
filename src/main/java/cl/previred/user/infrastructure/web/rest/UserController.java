package cl.previred.user.infrastructure.web.rest;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.previred.user.application.service.UserService;
import cl.previred.user.domain.modelo.User;
import cl.previred.user.infrastructure.web.dto.LoginRequest;
import cl.previred.user.infrastructure.web.dto.TokenResponse;
import cl.previred.user.infrastructure.web.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "UserController", description = "Permite realizar login y si esta OK devuelve token de acceso")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    
    public UserController(UserService userService, UserMapper userMapper) {
		this.userService = userService;
		this.userMapper = userMapper;
    }

    @Operation(summary = "Login de la api", description = "Permite realizar la autenticacion de usuarios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Creacion del token exitosa"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "401", description = "Password incorrecta"),
        @ApiResponse(responseCode = "401", description = "Rol no autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
	@PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request){
    	User user = userMapper.toUser(request);
    	Optional<String> tknOptional = userService.login(user);

    	String token = tknOptional.orElseThrow(
                () -> new IllegalStateException("Error interno: Token no pudo ser generado.")
            );
    	
    	TokenResponse tokenResponse = new TokenResponse(token);
        return ResponseEntity.ok(tokenResponse);
    }


}

