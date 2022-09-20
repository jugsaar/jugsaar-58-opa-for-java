package demo.quarkus.security;

import lombok.RequiredArgsConstructor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.ForbiddenException;
import java.lang.reflect.Method;

/**
 * {@link OpaInterceptor} weaves the Authorization logic around methods annotated with @{@link PreAuthz}.
 */
@Interceptor
@PreAuthz
@RequiredArgsConstructor
public class OpaInterceptor {

    private final OpaPolicyEnforcer opa;

    @AroundInvoke
    public Object authorize(InvocationContext invocation) throws Exception {

        var method = invocation.getMethod();
        var preAuthz = method.getAnnotation(PreAuthz.class);

        if (preAuthz != null) {
            preAuthorize(preAuthz, method, invocation.getParameters());
        }

        return invocation.proceed();
    }

    private void preAuthorize(PreAuthz preAuthz, Method method, Object[] args) {

        var action = resolveAction(preAuthz, method);
        var permission = resolvePermission(preAuthz);
        var resource = resolveResource(preAuthz, method, args);

        if (!opa.isAllowed(action, permission, resource)) {
            throw new ForbiddenException("Access Denied");
        }
    }

    private static String resolveAction(PreAuthz authz, Method method) {

        var action = authz.action();
        if (action == null || action.isBlank()) {
            return method.getName();
        }

        return action;
    }

    private String resolvePermission(PreAuthz authz) {
        var permission = authz.permission();
        if (permission == null || permission.isBlank()) {
            return null;
        }
        return permission;
    }

    private static Object resolveResource(PreAuthz authz, Method method, Object[] args) {

        var resource = authz.resource();
        if (resource == null || resource.isBlank()) {
            return null;
        }

        // resolve parameter reference
        if (resource.startsWith("#")) {
            var paramName = resource.substring(1);
            var parameters = method.getParameters();
            for (var i = 0; i < parameters.length; i++) {

                if (paramName.equals(parameters[i].getName())) {
                    return args[i];
                }
            }
        }

        return resource;
    }
}
