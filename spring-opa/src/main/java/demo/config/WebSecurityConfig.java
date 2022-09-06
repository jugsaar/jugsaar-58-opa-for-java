package demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {

        var alice = User.withUsername("alice").password(passwordEncoder.encode("pass")).roles("CEO").build();
        var bob = User.withUsername("bob").password(passwordEncoder.encode("pass")).roles("CTO").build();
        var carol = User.withUsername("carol").password(passwordEncoder.encode("pass")).roles("DEV").build();
        var david = User.withUsername("david").password(passwordEncoder.encode("pass")).roles("DEV").build();
        var john = User.withUsername("john").password(passwordEncoder.encode("pass")).roles("HR").build();

        return new InMemoryUserDetailsManager(alice, bob, carol, david, john);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests(arc -> {
            arc.anyRequest().authenticated();
        });

        http.formLogin(AbstractHttpConfigurer::disable);

        http.httpBasic();
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
