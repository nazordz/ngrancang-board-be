package com.unindra.ngrancang.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.unindra.ngrancang.jwt.AuthTokenFilter;
import com.unindra.ngrancang.jwt.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration{
    @Autowired
    UserDetailsServiceImpl userDetailsService;
  
    // @Autowired
    // private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
    
        return authProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // http.exceptionHandling(auth -> {
        //     auth.authenticationEntryPoint(unauthorizedHandler);
        // });

        // http.sessionManagement(auth -> {
        //     auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // });
        
        http.authorizeHttpRequests(auth -> {
            
            auth.requestMatchers("/api/auth/**").permitAll();
            auth.requestMatchers("/api/user/**").authenticated();
            auth.requestMatchers("/api/role/**").permitAll();
            auth.anyRequest().authenticated();
        })
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(Customizer.withDefaults());
        
        
        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
