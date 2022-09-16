package demo.quarkus.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.security.identity.SecurityIdentity;
import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import java.util.Set;

@JBossLog
@ApplicationScoped
public class OpaPolicyEnforcer {

    private final OpaClient opaClient;

    private final SecurityIdentity identity;

    private final ObjectMapper objectMapper;

    OpaPolicyEnforcer(@RestClient OpaClient opaClient, SecurityIdentity identity, ObjectMapper objectMapper) {
        this.opaClient = opaClient;
        this.identity = identity;
        this.objectMapper = objectMapper;
    }

    public boolean isAllowed(String action, String permission, Object resource) {

        if (action == null) {
            return false;
        }

        if (identity.isAnonymous()) {
            return false;
        }

        var subject = getSubjectAttributes(identity);
        var input = new AuthzRequestInput(subject, action, permission, resource);

        var authzRequest = new AuthzRequest(input);
        log.infov("Authz request:\n{0}", objectMapper.convertValue(authzRequest, ObjectNode.class).toPrettyString());

        var authzResponse = opaClient.check("allow", authzRequest);
        log.infov("Authz response:\n{0}", objectMapper.convertValue(authzResponse, ObjectNode.class).toPrettyString());

        if (authzResponse == null) {
            return false;
        }

        return Boolean.TRUE.equals(authzResponse.result());
    }

    private SubjectAttributes getSubjectAttributes(SecurityIdentity identity) {

        var username = identity.getPrincipal().getName();
        var authorities = identity.getRoles();

        return new SubjectAttributes(username, authorities);
    }

    public record SubjectAttributes(String name, Set<String> roles) {
    }

    public record AuthzRequestInput(SubjectAttributes subject, String action, String permission, Object resource) {
    }

    public record AuthzRequest(AuthzRequestInput input) {
    }

    public record AuthzResponse(Boolean result) {
    }

}
