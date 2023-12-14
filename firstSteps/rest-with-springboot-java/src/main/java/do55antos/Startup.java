/**
 * Improving skills in API RESTful
 * last commit 14, December, 2023 _ 09:00 pm (UTC -3) 
 * 
 * application.yml from tests updated (Dockerizing application)
 * 
 * (13 - December - 2023)
 * Dockerfile; docker-compose-yml; fix HATEOAS tests; 
 * 
 *  PS.:
 *  (Unless the container is rebuilt before testing)
 *  Whenever you run the join tests on the container,
 *  new elements are added to the database. So the total number of 
 *  elements and the last page assertions of the HATEOAS tests always change.
 *  The lines of these assertions were removed from the tests 
 *  to upload the image without confusing and unnecessary problems.
 *  MockBook testCreate was with an error stubbing - fixed too  
 *
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
