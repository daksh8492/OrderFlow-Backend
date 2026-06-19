package com.orderflow.util;

import com.orderflow.entity.user.User;
import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.repository.user.UserRepo;
import com.orderflow.security.CustomUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserPrincipal principal)) throw new IllegalStateException("No authenticated user found");
        User user = principal.getUser();
        user.setPassword(null);
        return user;
    }

    public Warehouse getLoggedInUserWarehouse() {
        Warehouse warehouse = getLoggedInUser().getUserWarehouse();
        if (warehouse == null) throw new IllegalStateException("User is not assigned to any warehouse");
        return warehouse;
    }


}
