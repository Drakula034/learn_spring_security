package com.eazybytes.config;

import com.eazybytes.filter.CsrfCookieFilter;
import com.eazybytes.filter.RequestValidationBeforeFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.sql.DataSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        csrfTokenRequestAttributeHandler.setCsrfRequestAttributeName("_csrf");


        http.securityContext(context -> context.requireExplicitSave(false))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))

                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                        corsConfiguration.setAllowCredentials(true);
                        corsConfiguration.setMaxAge(3600L);
                        return corsConfiguration;
                    }
                }))
                .csrf(csrfCustomizer -> csrfCustomizer
                        .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("/register", "/contact")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                ).addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)

                .authorizeHttpRequests((requests) ->
                                requests
//                                .requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
//                                .requestMatchers("/myBalance").hasAnyAuthority("VIEWACCOUNT", "VIEWBALANCE")
//                                .requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
//                                .requestMatchers("myCards").hasAuthority("VIEWCARDS")
                                        .requestMatchers("/myAccount").hasRole("USER")
                                        .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers("/myLoans").hasRole("USER")
                                        .requestMatchers("myCards").hasRole("USER")
                                        .requestMatchers("/user").authenticated()
                                        .requestMatchers("/notices", "/register", "/contact").permitAll()
                )
                .formLogin(withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}