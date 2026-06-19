package com.orderflow.controller;

import com.orderflow.dto.LoginRequest;
import com.orderflow.dto.LoginResponse;
import com.orderflow.dto.UserDto;
import com.orderflow.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        LoginResponse loginResponse = authService.login(loginRequest.code(), loginRequest.password());
        Cookie cookie = new Cookie("refreshToken", loginResponse.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new ResponseEntity<>(loginResponse.accessToken(), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@CookieValue("refreshToken") String token) {
        String accessToken = authService.refresh(token);
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        return new ResponseEntity<>(authService.me(), HttpStatus.OK);
    }
}
