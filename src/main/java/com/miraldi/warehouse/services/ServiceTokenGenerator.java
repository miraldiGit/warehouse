package com.miraldi.warehouse.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miraldi.warehouse.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.miraldi.warehouse.utils.PathsAndStrings.ACCESS_TOKEN;
import static com.miraldi.warehouse.utils.PathsAndStrings.BEARER_STRING;
import static com.miraldi.warehouse.utils.PathsAndStrings.ERROR_MESSAGE;
import static com.miraldi.warehouse.utils.PathsAndStrings.REFRESH_TOKEN;
import static com.miraldi.warehouse.utils.PathsAndStrings.TOKEN_EMAIL;
import static com.miraldi.warehouse.utils.PathsAndStrings.TOKEN_ID;
import static com.miraldi.warehouse.utils.PathsAndStrings.TOKEN_ROLE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ServiceTokenGenerator {

    public static void tokensGenerator(HttpServletRequest request,
                                       HttpServletResponse response,
                                       CustomUserDetails user) throws IOException {

        String access_token = tokenCreator(user.getUsername(),600000,request,
                user.getRole().name(),String.valueOf(user.getId()),user.getEmail());

        String refresh_token = tokenCreator(user.getUsername(), 604800000, request,
                user.getRole().name(), String.valueOf(user.getId()), user.getEmail());

        Map<String, String> tokens = new HashMap<>();
        tokens.put(ACCESS_TOKEN, access_token);
        tokens.put(REFRESH_TOKEN, refresh_token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    public static void refreshTokenGenerator(HttpServletRequest request,
                                             HttpServletResponse response) throws IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith(BEARER_STRING)){
            try {

                String refresh_token = authorizationHeader.substring(BEARER_STRING.length());
                DecodedJWT decodedJWT = tokenDecoder(refresh_token);
                String username = decodedJWT.getSubject();
                String role = decodedJWT.getClaim(TOKEN_ROLE).asString();
                String id = decodedJWT.getClaim(TOKEN_ID).asString();
                String email = decodedJWT.getClaim(TOKEN_EMAIL).asString();

                String access_token = tokenCreator(username,600000,request,role,id,email);

                Map<String, String> tokens = new HashMap<>();
                tokens.put(ACCESS_TOKEN, access_token);
                tokens.put(REFRESH_TOKEN, refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch(Exception exception){
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put(ERROR_MESSAGE, exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);

            }
        }
        else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    public static DecodedJWT tokenDecoder(String token){
        Algorithm algorithm =  Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public static String tokenCreator(String username,
                                      int durationTime,
                                      HttpServletRequest request,
                                      String role,
                                      String id,
                                      String email){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + durationTime ))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("role", role)
                .withClaim("id", id)
                .withClaim("email", email)
                .sign(Algorithm.HMAC256("secret".getBytes()));
    }
}