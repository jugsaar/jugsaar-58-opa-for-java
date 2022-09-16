package demo.quarkus.greeting;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class GreetingService {

    public String greet(String name) {
        return "Hello " + name;
    }
}
