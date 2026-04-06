package com.orderflow.util;

import com.orderflow.entity.user.User;
import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.repository.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private UserRepo userRepo;

    private static final String TEMP_USER = "USR-0002";

    public User getLoggedInUser(){
        return userRepo.findByCode(TEMP_USER).orElse(null);
    }

    public Warehouse getLoggedInUserWarehouse(){
        Warehouse warehouse = getLoggedInUser().getUserWarehouse();
        if (warehouse == null) {
            throw new IllegalStateException("User is not assigned to any warehouse");
        }
        return warehouse;
    }


}
