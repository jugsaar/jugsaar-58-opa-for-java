package spring.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Component("opa")
@RequiredArgsConstructor
public class OpaPolicyEnforcer {

    private static final String POLICY_PATH =
            // "/access?metrics";
            "/access";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RestTemplate restTemplate = new RestTemplate();

    private final OpaProperties opaProperties;

    public boolean isAllowed(String action, Map<String, Object> resourceAttributes) {

        if (action == null || resourceAttributes == null || resourceAttributes.isEmpty()) {
            return false;
        }

        var auth = currentAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        var subjectAttributes = getSubjectAttributes(auth);
        var input = new AccessRequestInput(subjectAttributes, action, resourceAttributes);

        var accessRequest = toAccessRequest(input);
        log.info("Authorization request:\n{}", accessRequest.toPrettyString());

        var uri = opaProperties.getUri() + POLICY_PATH;

        var accessResponse = restTemplate.postForObject(uri, accessRequest, JsonNode.class);
        log.info("Authorization response:\n{}", accessResponse != null ? accessResponse.toPrettyString() : accessResponse);

        if (accessResponse == null) {
            return false;
        }

        var access = accessResponse.get("result");

        if (access == null) {
            return false;
        }

        return access.get("allowed").asBoolean();
    }

    private static SubjectAttributes getSubjectAttributes(Authentication auth) {

        var username = auth.getName();
        var authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return new SubjectAttributes(username, authorities);
    }

    private static Authentication currentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private ObjectNode toAccessRequest(AccessRequestInput input) {
        var requestNode = objectMapper.createObjectNode();
        requestNode.set("input", objectMapper.valueToTree(input));
        return requestNode;
    }

    record SubjectAttributes(String name, List<String> roles) {
    }

    record AccessRequestInput(SubjectAttributes subject, String action, Map<String, Object> resource) {
    }
}
