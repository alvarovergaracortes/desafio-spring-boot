package cl.previred.common.helper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 
 * Clase para encriptar password de usuario y asi dejarla en BD.
 * 
 * Con esto se podran crear mas usuario y fuera necesario.
 */
public class CreateEncryptedPassword {

	public static void main(String[] args) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println("clavedepepito: " + encoder.encode("clavedepepito"));
		System.out.println("admin123: " + encoder.encode("admin123"));
		System.out.println("user123: " + encoder.encode("user123"));
	}
}
