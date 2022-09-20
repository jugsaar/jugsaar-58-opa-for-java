package demo.quarkus.security;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import static demo.quarkus.security.OpaPolicyEnforcer.AuthzRequest;
import static demo.quarkus.security.OpaPolicyEnforcer.AuthzResponse;

/**
 * Provides a typed REST client for OPA policy API usable as Resteasy Client Proxy.
 */
@ApplicationScoped
@Path("/authz/quarkus")
@RegisterRestClient(configKey = "opa-client")
public interface OpaClient {

    /**
     * Evaluates the given {@link AuthzRequest} in the context of the given {@code rule}.
     *
     * @param rule
     * @param authzRequest
     * @return authzResponse
     */
    @POST
    @Path("{rule}")
    AuthzResponse check(@PathParam("rule") String rule, AuthzRequest authzRequest);
}
