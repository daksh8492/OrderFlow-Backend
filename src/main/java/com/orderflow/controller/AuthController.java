package com.orderflow.controller;

import com.orderflow.dto.LoginRequest;
import com.orderflow.dto.LoginResponse;
import com.orderflow.dto.UserDto;
import com.orderflow.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        LoginResponse loginResponse = authService.login(loginRequest.code(), loginRequest.password());
        ResponseCookie cookie = ResponseCookie.from("refreshToken", loginResponse.refreshToken())
                .httpOnly(true)
                .secure(true)
                        .path("/")
                                .sameSite("None")
                                        .build();
//        Cookie cookie = new Cookie("refreshToken", loginResponse.refreshToken());
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
        response.addHeader("Set-Cookie", cookie.toString());
        return new ResponseEntity<>(loginResponse.accessToken(), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@CookieValue("refreshToken") String token) {
        String accessToken = authService.refresh(token);
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        return new ResponseEntity<>(authService.me(), HttpStatus.OK);
    }
}
