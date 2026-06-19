package com.orderflow.service;

import com.orderflow.dto.LoginResponse;
import com.orderflow.dto.UserDto;
import com.orderflow.entity.user.User;
import com.orderflow.mapper.UserMapper;
import com.orderflow.security.CustomUserPrincipal;
import com.orderflow.security.JwtService;
import com.orderflow.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private UserMapper userMapper;

    public LoginResponse login(String code, String password){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(code, password));
        String accessToken = jwtService.generateAccessToken((CustomUserPrincipal) authentication.getPrincipal());
        String refreshToken = jwtService.generateRefreshToken((CustomUserPrincipal) authentication.getPrincipal());
        return new LoginResponse(accessToken, refreshToken);
    }

    public String refresh(String refreshToken){
        if (refreshToken == null || refreshToken.isEmpty()) throw new BadCredentialsException("Refresh token cannot be empty");
        System.out.println("REFRESH TOKEN:");
        System.out.println(refreshToken);
        String code = jwtService.extractCode(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(code);
        if (jwtService.isTokenValid(refreshToken, userDetails)){
            String accessToken = jwtService.generateAccessToken((CustomUserPrincipal) userDetails);
            return accessToken;
        }
        throw new IllegalArgumentException("Refresh token expired");
    }

    public UserDto me() {
        User user = authUtil.getLoggedInUser();
        return userMapper.userToUserDto(user);
    }
}
