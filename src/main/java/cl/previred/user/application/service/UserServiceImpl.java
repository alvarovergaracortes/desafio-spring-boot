package cl.previred.user.application.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cl.previred.common.exception.CredencialesInvalidasException;
import cl.previred.common.exception.ErrorCode;
import cl.previred.common.exception.ResourceNotFoundException;
import cl.previred.common.security.JwtUtil;
import cl.previred.common.security.PasswordUtil;
import cl.previred.user.domain.modelo.User;
import cl.previred.user.infrastructure.persistence.entity.UserEntity;
import cl.previred.user.infrastructure.persistence.repository.UserJpaRepository;
import cl.previred.user.infrastructure.web.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService{
	private final PasswordUtil passwordUtil;
	private final JwtUtil jwtUtil;
	private final UserJpaRepository userJpaRepository;
	private final UserMapper userMapper;
	

	public UserServiceImpl(PasswordUtil passwordUtil, JwtUtil jwtUtil, UserJpaRepository userJpaRepository,
			UserMapper userMapper) {
		this.passwordUtil = passwordUtil;
		this.jwtUtil = jwtUtil;
		this.userJpaRepository = userJpaRepository;
		this.userMapper = userMapper;
	}


	@Override
	public Optional<String> login(User user) {
		UserEntity userEntity = userJpaRepository.findByUsername(user.getUsername())
				.orElseThrow(()-> new CredencialesInvalidasException("Usuario no encontrado", ErrorCode.RESOURCE_NOT_FOUND));
		
		System.out.println("user: " + user.getPassword() + "  entity: " + userEntity.getPassword());
		
		if (!passwordUtil.matches(user.getPassword(), userEntity.getPassword())) {
			throw new CredencialesInvalidasException("Usuario o contraseña incorrecta", ErrorCode.INCORRECT_CREDENTIALS);
		}
		
		List<String> roles = Arrays.stream(userEntity.getRoles().split(","))
				.map(String::trim)
				.map(String::toLowerCase)
				.toList();
		
		if (!roles.contains(user.getRoles().toLowerCase())) {
			throw new CredencialesInvalidasException("Rol no autorizado", ErrorCode.UNAUTHORIZED_ROLE);
		}
		
		return Optional.of(jwtUtil.generateToken(userEntity.getUsername(), roles));
	}


	@Override
	public User findById(Long userId) {
		UserEntity entity = userJpaRepository.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("Usuario con ID: " + userId + " no encontrado"));
		
		User user = userMapper.toDomain(entity);
		return user;
	}


	@Override
	public User findByUsername(String userName) {
		UserEntity entity = userJpaRepository.findByUsername(userName)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario con ID: " + userName + " no encontrado"));
		
		User user = userMapper.toDomain(entity);
		return user;
	}


	@Override
	public UserEntity findEntityById(Long userId) {
		return userJpaRepository.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("Usuario con ID: " + userId + " no encontrado"));
	}
	
	
	
}
