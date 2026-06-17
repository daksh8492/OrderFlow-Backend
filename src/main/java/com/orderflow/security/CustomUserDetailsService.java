package com.orderflow.security;

import com.orderflow.entity.user.User;
import com.orderflow.repository.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String code) throws UsernameNotFoundException {
        return new CustomUserPrincipal(userRepo.findByCode(code).orElseThrow(() -> new UsernameNotFoundException("User does not exist")));
    }
}
