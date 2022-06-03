package com.team7.matadorenbackend.jwtrefreshtoken;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
@RestController
@RequestMapping(path = "token")
public class JwtController {

    private final JwtRefreshToken jwtRefreshToken;

    @SneakyThrows
    @GetMapping(path = "refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){

        jwtRefreshToken.refreshToken(request,response);
    }
}
