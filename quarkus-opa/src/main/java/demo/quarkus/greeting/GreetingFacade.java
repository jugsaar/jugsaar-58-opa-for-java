package demo.quarkus.greeting;

import demo.quarkus.security.PreAuthz;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;

/**
 * Provides a coarse grained interface for the greeting service.
 */
@ApplicationScoped
@RequiredArgsConstructor
class GreetingFacade {

    private final GreetingService greetingService;

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
        return greetingService.greet(name);
    }
}
