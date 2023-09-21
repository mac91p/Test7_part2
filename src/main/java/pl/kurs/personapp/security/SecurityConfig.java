package pl.kurs.personapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails employee = User.withUsername("employee")
                .password(passwordEncoder.encode("123"))
                .roles("EMPLOYEE")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();
        UserDetails importer = User.withUsername("importer")
                .password(passwordEncoder.encode("123"))
                .roles("IMPORTER")
                .build();
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user,employee,importer,admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/api/person").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/person").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/person/import").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/person/import").hasRole("IMPORTER")
                .requestMatchers(HttpMethod.PUT, "/position/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/position/{id}").hasRole("EMPLOYEE")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf()
                .disable();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }


}
