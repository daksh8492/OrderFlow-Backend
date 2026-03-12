package com.orderflow.service;

import com.orderflow.entity.user.User;
import com.orderflow.exceptions.UserNotFoundException;
import com.orderflow.repository.user.UserRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User addUser(User user){
        long count = userRepo.count();
        String code;
        do {
            code = "USR-"+ String.format("%04d", count+1);
            count++;
        }while (userRepo.existsByCode(code));
        user.setCode(code);
        return userRepo.save(user);
    }

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    public User getUserByCode(String code){
        Optional<User> user = userRepo.findByCode(code);
        return user.orElseThrow( () -> new UserNotFoundException("User not found"));
    }

    public List<User> getUserByFieldOfWork(User.FieldOfWork fieldOfWork){
        List<User> users = userRepo.findByFieldOfWork(fieldOfWork);
        return users;
    }

    public User getUserById(UUID id){
        return userRepo.findById(id).orElseThrow( () -> new UserNotFoundException("User not found"));
    }

    public User updateUser(UUID id, User user){
        User existingUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        BeanUtils.copyProperties(user, existingUser, "id", "code");
        return userRepo.save(existingUser);
    }

    public void deleteUser(UUID id){
        User user = userRepo.findById(id).orElseThrow(()-> new UserNotFoundException("User not found"));
        userRepo.delete(user);
    }

    public void disableUser(UUID id){
        User user = userRepo.findById(id).orElseThrow( () -> new UserNotFoundException("User not found"));
        user.setActive(false);
        userRepo.save(user);
    }

    public void enableUser(UUID id){
        User user = userRepo.findById(id).orElseThrow( () -> new UserNotFoundException("User not found"));
        user.setActive(true);
        userRepo.save(user);
    }

}
