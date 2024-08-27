package com.miraldi.warehouse.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miraldi.warehouse.entities.User;
import com.miraldi.warehouse.security.CustomUserDetails;
import com.miraldi.warehouse.services.ServiceTokenGenerator;
import com.miraldi.warehouse.utils.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.miraldi.warehouse.utils.PathsAndStrings.BEARER_STRING;
import static com.miraldi.warehouse.utils.PathsAndStrings.ERROR_MESSAGE;
import static com.miraldi.warehouse.utils.PathsAndStrings.LOGIN_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.REFRESH_TOKEN_FULL_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.SYSTEM_ADMIN;
import static com.miraldi.warehouse.utils.PathsAndStrings.TOKEN_EMAIL;
import static com.miraldi.warehouse.utils.PathsAndStrings.TOKEN_ID;
import static com.miraldi.warehouse.utils.PathsAndStrings.TOKEN_ROLE;
import static com.miraldi.warehouse.utils.PathsAndStrings.WAREHOUSE_MANAGER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals(LOGIN_PATH) ||
                request.getServletPath().equals(REFRESH_TOKEN_FULL_PATH)){
            filterChain.doFilter(request, response);
        }
        else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader != null && authorizationHeader.startsWith(BEARER_STRING)){
                try {
                    DecodedJWT decodedJWT = ServiceTokenGenerator
                            .tokenDecoder(authorizationHeader
                                    .substring(BEARER_STRING.length()));
                    String username = decodedJWT.getSubject();
                    String role = decodedJWT.getClaim(TOKEN_ROLE).asString();
                    Long id = decodedJWT.getClaim(TOKEN_ID).asLong();
                    String email = decodedJWT.getClaim(TOKEN_EMAIL).asString();


                    User user = new User();
                    user.setUsername(username);

                    if (role.equals(SYSTEM_ADMIN)) {
                        user.setRole(Role.SYSTEM_ADMIN);
                    }
                    else if(role.equals(WAREHOUSE_MANAGER)) {
                        user.setRole(Role.WAREHOUSE_MANAGER);
                    }
                    else {
                        user.setRole(Role.CLIENT);
                    }
                    user.setId(id);
                    user.setDeleted(false);
                    user.setPassword("test");
                    user.setCountry("test");
                    user.setFirstName("test");
                    user.setLastName("test");
                    user.setCity("test");
                    user.setPostalCode(1111);
                    user.setEmail(email);

                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(role));

                    CustomUserDetails customUserDetails = new CustomUserDetails(user);
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(customUserDetails, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                }catch(Exception exception){
                    log.error("Error logging in: {}", exception.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put(ERROR_MESSAGE, exception.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            }
            else {
                filterChain.doFilter(request, response);
            }
        }
    }
}