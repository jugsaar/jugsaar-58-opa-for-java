package demo.quarkus.greeting;


import demo.quarkus.security.PreAuthorize;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RequiredArgsConstructor
public class GreetingFacade {

    private final GreetingService greetingService;

    @PreAuthorize(resource = "#name")
    public String greet(String name) {
        return greetingService.greet(name);
    }
}
