package rs.ac.raf.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import rs.ac.raf.userservice.security.GatewayHeaderAuthFilter;
import rs.ac.raf.userservice.security.InternalSecretFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.internal-secret}")
    private String internalSecret;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/users/register", "/api/users/login", "/api/users/activate",
                        "/api/users/forgot-password", "/api/users/reset-password").permitAll()
                .antMatchers("/internal/**").permitAll()
                .antMatchers("/api/users/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new InternalSecretFilter(internalSecret), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new GatewayHeaderAuthFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
