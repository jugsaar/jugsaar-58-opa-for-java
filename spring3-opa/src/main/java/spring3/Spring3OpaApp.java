package spring3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import spring3.security.OpaProperties;

@SpringBootApplication
@EnableConfigurationProperties(OpaProperties.class)
public class Spring3OpaApp {

    public static void main(String[] args) {
        SpringApplication.run(Spring3OpaApp.class, args);
    }

}
