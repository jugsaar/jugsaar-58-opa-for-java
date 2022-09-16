package spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import spring.security.OpaProperties;

@SpringBootApplication
@EnableConfigurationProperties(OpaProperties.class)
public class SpringOpaApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringOpaApp.class, args);
    }

}
