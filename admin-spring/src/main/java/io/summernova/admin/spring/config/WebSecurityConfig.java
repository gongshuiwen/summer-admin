package io.summernova.admin.spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.summernova.admin.spring.security.web.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

/**
 * @author gongshuiwen
 */
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    private final ObjectMapper objectMapper;

    public WebSecurityConfig(
            @Qualifier("mappingJackson2HttpMessageConverterObjectMapper") ObjectMapper ObjectMapper) {
        // use the same objectMapper instance with the MappingJackson2HttpMessageConverter
        this.objectMapper = ObjectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            UsernamePasswordAuthenticationFilter filter
    ) throws Exception {

        http
                // Disable CSRF protection
                .csrf(AbstractHttpConfigurer::disable)

                // Config login filter
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)

                // Config logout filter
                .logout(configurer -> configurer
                        .logoutSuccessHandler(new CustomLogoutSuccessHandler(objectMapper)))

                // Config exception handle filter
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper))
                        .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper)))

                // Static configurations for requests authorization
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/doc.html").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/file/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    UsernamePasswordAuthenticationFilter requestBodyUsernamePasswordAuthenticationFilter(
            AuthenticationManager authenticationManager
    ) {
        UsernamePasswordAuthenticationFilter filter =
                new RequestBodyUsernamePasswordAuthenticationFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler(objectMapper));
        filter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler(objectMapper));
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        return filter;
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserDetailsService userDetailsService() {
        return username -> null;
    }
}
