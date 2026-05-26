package cl.duoc.archivos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the Spring Boot application.
 *
 * To run the application locally:
 * - Configure AWS credentials through environment variables or AWS CLI configuration.
 * - Specify your AWS region and S3 bucket name in application.properties.
 * - Execute: mvn spring-boot:run
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}