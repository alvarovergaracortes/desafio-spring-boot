package cl.previred.user.application.service;

import java.util.Optional;

import cl.previred.user.domain.modelo.User;
import cl.previred.user.infrastructure.persistence.entity.UserEntity;

public interface UserService {
	
	Optional<String> login(User user);
	User findById(Long userId);
	User findByUsername(String userName);
	
	// para evitar el Not-null property references a transient value
	UserEntity findEntityById(Long userId);
	
}
