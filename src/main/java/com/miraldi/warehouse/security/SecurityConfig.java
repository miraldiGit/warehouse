package com.miraldi.warehouse.security;

import com.miraldi.warehouse.filter.CustomAuthenticationFilter;
import com.miraldi.warehouse.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.miraldi.warehouse.utils.PathsAndStrings.CHANGE_PASSWORD_INCLUSIVE_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.INVENTORY_ITEM_INCLUSIVE_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.LOGIN_INCLUSIVE_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.LOGIN_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.REFRESH_TOKEN_INCLUSIVE_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.SYSTEM_ADMIN;
import static com.miraldi.warehouse.utils.PathsAndStrings.TRUCK_INCLUSIVE_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.USERS_INCLUSIVE_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.WAREHOUSE_MANAGER;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter =
                new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl(LOGIN_PATH);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(LOGIN_INCLUSIVE_PATH).permitAll()
                        .requestMatchers(GET,REFRESH_TOKEN_INCLUSIVE_PATH).permitAll()
                        .requestMatchers(PUT, CHANGE_PASSWORD_INCLUSIVE_PATH).authenticated()
                        .requestMatchers(INVENTORY_ITEM_INCLUSIVE_PATH, TRUCK_INCLUSIVE_PATH).hasAuthority(WAREHOUSE_MANAGER)
                        .requestMatchers(USERS_INCLUSIVE_PATH).hasAuthority(SYSTEM_ADMIN)
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(new CustomAuthorizationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(authProvider);
    }
}