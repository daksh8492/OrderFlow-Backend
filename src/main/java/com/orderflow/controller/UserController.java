package com.orderflow.controller;

import com.orderflow.dto.UserDto;
import com.orderflow.entity.user.User;
import com.orderflow.mapper.UserMapper;
import com.orderflow.service.UserService;
import org.mapstruct.factory.Mappers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);


    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto){
        User user = userMapper.dtoToUser(userDto);
        return new ResponseEntity<>(userMapper.userToUserDto(userService.addUser(user)), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> userDtos = userMapper.usersToUserDtos(userService.getAllUsers());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<UserDto> getUserByCode(@PathVariable String code){
        return new ResponseEntity<>(userMapper.userToUserDto(userService.getUserByCode(code)), HttpStatus.OK);
    }

    @GetMapping("fieldOfWork/{fieldOfWork}")
    public ResponseEntity<List<UserDto>> getUserByFieldOfWork(@PathVariable User.FieldOfWork fieldOfWork){
        List<UserDto> users = userMapper.usersToUserDtos(userService.getUserByFieldOfWork(fieldOfWork));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id){
        return new ResponseEntity<>(userMapper.userToUserDto(userService.getUserById(id)), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @RequestBody UserDto userDto){
        return new ResponseEntity<>(userMapper.userToUserDto(userService.updateUser(id, userMapper.dtoToUser(userDto))), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable UUID id){
        userService.disableUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<?> activateUser(@PathVariable UUID id){
        userService.enableUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
