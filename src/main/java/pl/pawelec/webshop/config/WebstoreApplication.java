package pl.pawelec.webshop.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ComponentScan("pl.pawelec.webshop")
@EntityScan("pl.pawelec.webshop.model")
@ImportResource("file:src/main/webapp/WEB-INF/spring/web-context.xml")
public class WebstoreApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(WebstoreApplication.class, args);
    }
}
