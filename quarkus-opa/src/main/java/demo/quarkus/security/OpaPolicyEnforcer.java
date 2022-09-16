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

    private static final ObjectMapper OM = new ObjectMapper();

    private final OpaClient opaClient;

    private final SecurityIdentity identity;

    OpaPolicyEnforcer(@RestClient OpaClient opaClient, SecurityIdentity identity) {
        this.opaClient = opaClient;
        this.identity = identity;
    }

    public boolean isAllowed(String action, String permission, Object resource) {

        if (action == null) {
            return false;
        }

        if (identity.isAnonymous()) {
            return false;
        }

        var subjectAttributes = getSubjectAttributes(identity);
        var input = new AccessRequestInput(subjectAttributes, action, permission, resource);

        var accessRequest = new AuthzRequest(input);
        log.infov("Authz request:\n{0}", OM.convertValue(accessRequest, ObjectNode.class).toPrettyString());

        var accessResponse = opaClient.check("allow", accessRequest);
        log.infov("Authz response:\n{0}", OM.convertValue(accessResponse, ObjectNode.class).toPrettyString());

        if (accessResponse == null) {
            return false;
        }

        var access = accessResponse.result();
        return Boolean.TRUE.equals(access);
    }

    private static SubjectAttributes getSubjectAttributes(SecurityIdentity identity) {

        var username = identity.getPrincipal().getName();
        var authorities = identity.getRoles();

        return new SubjectAttributes(username, authorities);
    }

    public record SubjectAttributes(String name, Set<String> roles) {
    }

    public record AccessRequestInput(SubjectAttributes subject, String action, String permission, Object resource) {
    }

    public record AuthzRequest(AccessRequestInput input) {
    }

    public record AuthzResponse(Boolean result) {
    }

}
