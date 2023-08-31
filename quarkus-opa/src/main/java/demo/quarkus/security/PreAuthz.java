package demo.quarkus.security;


import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@InterceptorBinding
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface PreAuthz {

    /**
     * The action of the authorization query. Defaults to method name.
     */
    @Nonbinding String action() default "";

    /**
     * Optional resource for the authorization query. Supports resolving of named parameter values via {@code #paramName}.
     */
    @Nonbinding String resource() default "";

    /**
     * Optional logical permission that should be associated with the authorization query.
     */
    @Nonbinding String permission() default "";
}
