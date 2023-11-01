/**
 * Improving skills in API RESTful
 * last commit 31, October, 2023 _ 2:15 pm (UTC -3) _ refresh Token
 * 
 * 30/10/2023 - testing authentication in postman
 * 	problems: Error 500,
 * 	fix items:
 * 	fix override methods in User class
 * 			-solved
 *
 *	After this, could not access token by tests in Postman, that always returns invalid username/password error.
 *	To fix it, implemented basic logic to create passwords in Startup class (this)
 *		and insert it in user on database
 *			-solved
 *
 *	Edit environment variable in Postman, setting variables {{username}} and {{password}}
 *	to dynamic authenticate 
 *			-solved
 */

package do55antos;

//import java.util.HashMap;
//import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
//import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;

@SpringBootApplication
public class Startup {

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);

//		Map<String, PasswordEncoder> encoders = new HashMap<>();
//
//		Pbkdf2PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000,
//				SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
//
//		encoders.put("pbkdf2", pbkdf2Encoder);
//		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
//		passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
//
//		String result1 = passwordEncoder.encode("admin123");
//		String result2 = passwordEncoder.encode("admin234");
//		System.out.println("My hash result1 " + result1);
//		System.out.println("My hash result2 " + result2);
	}

}
