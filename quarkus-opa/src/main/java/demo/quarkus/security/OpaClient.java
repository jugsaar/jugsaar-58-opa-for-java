package demo.quarkus.security;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import static demo.quarkus.security.OpaPolicyEnforcer.AuthzRequest;
import static demo.quarkus.security.OpaPolicyEnforcer.AuthzResponse;

@ApplicationScoped
@Path("/authz/quarkus")
@RegisterRestClient(configKey = "opa-client")
public interface OpaClient {

    @POST
    @Path("{scope}")
    AuthzResponse check(@PathParam("scope") String scope, AuthzRequest authzRequest);
}
