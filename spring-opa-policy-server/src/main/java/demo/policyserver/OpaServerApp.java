package demo.policyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class OpaServerApp {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(OpaServerApp.class, args);

//        try (var filePaths = Files.walk(Paths.get("opa"),10)) {
//            filePaths.forEach(System.out::println);
//        }
    }

}
