package cl.previred.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cl.previred.common.exception.CustomAccessDeniedHandler;

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUtil jwtUtil,
			CustomAccessDeniedHandler accessDeniedHandler, CustomAuthenticationEntryPoint authenticationEntryPoint) throws Exception {
		
		return http
				.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**").disable())
				.headers(headers -> headers.frameOptions(frame -> frame.disable()))
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// BD H2
						.requestMatchers("/h2-console/**").permitAll()
						
						// Permitir login y Swagger
						.requestMatchers(HttpMethod.POST, "/user/login").permitAll()
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
						
						// Rutas GET a /tasks accesibles por USER o ADMIN
						.requestMatchers(HttpMethod.GET, "/tasks/**").hasAnyRole("USER", "ADMIN")
						
						// Admin puede ver todas las tareas y user solo las propias
						.requestMatchers(HttpMethod.GET, "/tasks/users/**").hasAnyRole("USER", "ADMIN")
						
						// Todo lo demas requiere autenticacion
						.anyRequest().authenticated()
				)
				.exceptionHandling(ex -> ex
					.accessDeniedHandler(accessDeniedHandler)
					.authenticationEntryPoint(authenticationEntryPoint)
				)
				.addFilterBefore(new AuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
}
