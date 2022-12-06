package spring3.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix="opa")
public class OpaProperties {
    private String uri;
}
