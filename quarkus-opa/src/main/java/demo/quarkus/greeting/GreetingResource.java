package demo.quarkus.greeting;

import io.quarkus.security.identity.SecurityIdentity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    private final SecurityIdentity identity;

    private final GreetingFacade greetingFacade;

    public GreetingResource(SecurityIdentity identity, GreetingFacade greetingFacade) {
        this.identity = identity;
        this.greetingFacade = greetingFacade;
    }

    /**
     * Greets the current user
     *
     * @return greeting
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String greetMe() {
        return greet(identity.getPrincipal().getName());
    }

    /**
     * Greets the given user
     *
     * @return greeting
     */
    @GET
    @Path(("{name}"))
    @Produces(MediaType.TEXT_PLAIN)
    public String greet(@PathParam("name") String name) {
        return greetingFacade.greet(name);
    }
}