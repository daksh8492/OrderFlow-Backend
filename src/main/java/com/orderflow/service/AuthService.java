package com.orderflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    public void login(String code, String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(code, password));
    }
}
