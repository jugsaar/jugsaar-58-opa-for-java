package demo.quarkus.greeting;

import demo.quarkus.security.PreAuthz;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class GreetingService {

    /**
     * <pre>{@code
     * // action derived from method name
     * // #name -> reference to value of "name" parameter
     *
     * @PreAuthz(resource = "#name") --> {"action": "greet", "resource": "#name"}
     * }
     * </pre>
     */
    @PreAuthz(resource = "#name")
    public String greet(String name) {
        return "Hello " + name;
    }
}
