package com.orderflow.service;

import com.orderflow.dto.UserDto;
import com.orderflow.entity.user.User;
import com.orderflow.entity.warehouse.Warehouse;
import com.orderflow.exceptions.UserNotFoundException;
import com.orderflow.exceptions.WarehouseNotFoundException;
import com.orderflow.mapper.UserMapper;
import com.orderflow.repository.user.UserRepo;
import com.orderflow.repository.warehouse.WarehouseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WarehouseRepo warehouseRepo;

    public UserDto addUser(UserDto userDto){
        User user = userMapper.dtoToUser(userDto);
        Warehouse warehouse = warehouseRepo.findById(user.getUserWarehouse().getWarehouseId()).orElseThrow( () -> new WarehouseNotFoundException("Warehouse does not exist"));
        user.setUserWarehouse(warehouse);
        long count = userRepo.count();
        String code;
        do {
            code = "USR-"+ String.format("%04d", count+1);
            count++;
        }while (userRepo.existsByCode(code));
        user.setCode(code);
        return userMapper.userToUserDto(userRepo.save(user));
    }

    public List<UserDto> getAllUsers(){
        return userMapper.usersToUserDtos(userRepo.findAll());
    }

    public UserDto getUserByCode(String code){
        Optional<User> user = userRepo.findByCode(code);
        return userMapper.userToUserDto(user.orElseThrow( () -> new UserNotFoundException("User not found")));
    }

    public List<UserDto> getUserByFieldOfWork(User.FieldOfWork fieldOfWork){
        List<User> users = userRepo.findByFieldOfWork(fieldOfWork);
        return userMapper.usersToUserDtos(users);
    }

    public UserDto getUserById(UUID id){
        return userMapper.userToUserDto(userRepo.findById(id).orElseThrow( () -> new UserNotFoundException("User not found")));
    }

    public UserDto updateUser(UUID id, UserDto userDto){
        User user = userMapper.dtoToUser(userDto);
        User existingUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (user.getName()!=null) existingUser.setName(user.getName());
        if (user.getFieldOfWork() != null) existingUser.setFieldOfWork(user.getFieldOfWork());
        if (user.getUserWarehouse() != null) existingUser.setUserWarehouse(user.getUserWarehouse());
        if (user.getAddress() != null) existingUser.setAddress(user.getAddress());
        if (user.getCity() != null) existingUser.setCity(user.getCity());
        if (user.getContactEmail() != null) existingUser.setContactEmail(user.getContactEmail());
        if (user.getContactNumber() !=null) existingUser.setContactNumber(user.getContactNumber());
        if (user.getContactTelephone() !=null) existingUser.setContactTelephone(user.getContactTelephone());
        if (user.getSalary() != null) existingUser.setSalary(user.getSalary());
        return userMapper.userToUserDto(userRepo.save(existingUser));
    }

    public UserDto joinUser(UUID id){
        User user = userRepo.findById(id).orElseThrow( () -> new UserNotFoundException("User not found"));
        user.setJoinedAt(Instant.now());
        user.setActive(true);
        return userMapper.userToUserDto(userRepo.save(user));
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
