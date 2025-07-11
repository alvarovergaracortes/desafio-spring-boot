package cl.previred.common.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;

	public AuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String token = getTokenFromRequest(request);
		
		if (token==null) {
			filterChain.doFilter(request, response);
			return;
		}
		
		if (jwtUtil.validateToken(token)) {
			Claims claims = jwtUtil.extractAllClaims(token);
			String username = claims.getSubject();
			List<String> listRole = jwtUtil.extractRoles(token);
			
			List<GrantedAuthority> authorities = listRole.stream()
				    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
				    .collect(Collectors.toList());

			var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
			auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		
		filterChain.doFilter(request, response);

	}
	
	private String getTokenFromRequest(HttpServletRequest request) {
		final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
		if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
			return authHeader.substring(7);
		}
		return null;
	}
}
