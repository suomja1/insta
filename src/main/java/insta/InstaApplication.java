package insta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InstaApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(InstaApplication.class);
        app.run(args);
    }
}   