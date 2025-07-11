package cl.previred.common.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
	@Value("${jwt.secret}")
	private String secret;

	private Key key;
	
	@PostConstruct
	public void init() {
		byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
		
		if (keyBytes.length < 32) {
			throw new IllegalArgumentException("La clave secreta debe tener minimo 32 caracteres para el algoritmo HS256.");
		}
		
		this.key = Keys.hmacShaKeyFor(keyBytes);
    }
	
	/**
	 * Genera el token con una duracion de 10 minutos (expira en 10 minutos)
	 * 
	 * @param userName
	 * @param roles
	 * @return
	 */
	public String generateToken(String userName, List<String> roles) {
		return Jwts.builder()
				.setSubject(userName)
				.claim("roles", roles)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	
	/**
     * Extrae todos los claims del token JWT.
     *
     * @param token El token JWT.
     * @return Los claims del token.
     */
	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(this.key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public List<String> extractRoles(String token) {
		Claims claims = extractAllClaims(token);
		Object rolesObject = claims.get("roles");
		
		if (rolesObject instanceof List<?>) {
			return ((List<?>) rolesObject).stream()
					.map(role -> role.toString().toUpperCase())
					.collect(Collectors.toList());
		}

		return List.of();
	}
	
	
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

}
