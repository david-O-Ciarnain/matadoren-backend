package com.team7.matadorenbackend.jwtrefreshtoken;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team7.matadorenbackend.appuser.AppUser;
import com.team7.matadorenbackend.appuser.AppUserService;
import com.team7.matadorenbackend.appuser.roles.Roles;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class JwtRefreshToken {

    private final AppUserService appUserService;

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {


        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("team7 ")) ;
        try {
            String refresh_token = authorizationHeader.substring("team7 ".length());

            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refresh_token);
            String username = decodedJWT.getSubject();
            AppUser appUser = appUserService.getUser(username);

            String access_token = JWT.create()
                    .withSubject(appUser.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 20 * 60 * 1000))
                    .withIssuer(request.getRequestURI().toString())
                    .withClaim("roles", appUser.getRoles().stream().map(Roles::getName).collect(Collectors.toList()))
                    .sign(algorithm);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("refresh_token", refresh_token);
            tokens.put("access_token", access_token);
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);

        } catch (Exception e) {
            e.getMessage();
            response.setHeader("error", e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            Map<String, String> error = new HashMap<>();
            error.put("error_message", e.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }

    }
}
