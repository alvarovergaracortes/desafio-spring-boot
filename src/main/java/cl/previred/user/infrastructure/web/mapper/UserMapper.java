package cl.previred.user.infrastructure.web.mapper;

import org.springframework.stereotype.Component;

import cl.previred.common.security.UserDetailsImpl;
import cl.previred.user.domain.modelo.User;
import cl.previred.user.infrastructure.persistence.entity.UserEntity;
import cl.previred.user.infrastructure.web.dto.LoginRequest;
import cl.previred.user.infrastructure.web.dto.UserRequest;
import cl.previred.user.infrastructure.web.dto.UserResponse;
import jakarta.validation.Valid;


@Component
public class UserMapper {
    public User toDomain(UserEntity entity) {
        User domain = new User();
        domain.setId(entity.getId());
        domain.setUsername(entity.getUsername());
        domain.setRoles(entity.getRoles());
        
        return domain;
    }

    public UserEntity toEntity(User user){
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRoles(null);
        return entity;
    }

    public User toDomain(UserRequest req) {
        User domain = new User();
        //agregar seteo de la clase entidad a la dominio
        return domain;
    }
    
    
    public UserResponse toUserResponse(UserEntity entity) {
        return new UserResponse(entity.getId(), entity.getUsername(), entity.getRoles());
    }

	public UserDetailsImpl toUserDetail(@Valid LoginRequest request) {
		
		return null;
	}

	public User toUser(@Valid LoginRequest request) {
		User user = new User();
		user.setUsername(request.username());
		user.setPassword(request.password());
		user.setRoles(request.roles());
		
		return user;
	}
}
