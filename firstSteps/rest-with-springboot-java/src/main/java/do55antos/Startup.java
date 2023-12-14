/**
 * Improving skills in API RESTful
 * last commit 13, December, 2023 _ 10:25 pm (UTC -3) 
 * 
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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Startup {

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
	}
}
