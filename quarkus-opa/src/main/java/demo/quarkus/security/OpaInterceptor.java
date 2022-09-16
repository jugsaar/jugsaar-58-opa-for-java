package demo.quarkus.security;

import lombok.RequiredArgsConstructor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.ForbiddenException;
import java.lang.reflect.Method;

@Interceptor
@PreAuthorize
@RequiredArgsConstructor
public class OpaInterceptor {

    private final OpaPolicyEnforcer opa;

    @AroundInvoke
    public Object authorize(InvocationContext invocationContext) throws Exception {

        var method = invocationContext.getMethod();
        var preAuthz = method.getAnnotation(PreAuthorize.class);

        if (preAuthz != null) {
            preAuthorize(preAuthz, method, invocationContext.getParameters());
        }

        return invocationContext.proceed();
    }

    private void preAuthorize(PreAuthorize preAuthz, Method method, Object[] args) {

        var action = resolveAction(preAuthz, method);
        var permission = resolvePermission(preAuthz);
        var resource = resolveResource(preAuthz, method, args);

        if (!opa.isAllowed(action, permission, resource)) {
            throw new ForbiddenException("Access Denied");
        }
    }

    private String resolvePermission(PreAuthorize authz) {
        return authz.permission();
    }

    private static String resolveAction(PreAuthorize authz, Method method) {

        var action = authz.action();
        if (action == null || action.isBlank()) {
            return method.getName();
        }

        return action;
    }

    private static Object resolveResource(PreAuthorize authz, Method method, Object[] args) {

        var resource = authz.resource();
        if (resource == null || resource.isBlank()) {
            return "";
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
