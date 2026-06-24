package com.orderflow.controller;

import com.orderflow.dto.UserDto;
import com.orderflow.entity.user.FieldOfWork;
import com.orderflow.mapper.UserMapper;
import com.orderflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;


    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto){
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(@PageableDefault(size = 20) Pageable pageable){
        return new ResponseEntity<>(userService.getAllUsers(pageable), HttpStatus.OK);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<UserDto> getUserByCode(@PathVariable String code){
        return new ResponseEntity<>(userService.getUserByCode(code), HttpStatus.OK);
    }

    @GetMapping("/fieldOfWork/{fieldOfWork}")
    public ResponseEntity<Page<UserDto>> getUserByFieldOfWork(@PathVariable FieldOfWork fieldOfWork, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(userService.getUserByFieldOfWork(fieldOfWork, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @RequestBody UserDto userDto){
        return new ResponseEntity<>(userService.updateUser(id, userDto), HttpStatus.OK);
    }

    @PatchMapping("/join/{id}")
    public ResponseEntity<UserDto> joinUser(@PathVariable UUID id){
        return new ResponseEntity<>(userService.joinUser(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable UUID id){
        userService.disableUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<?> activateUser(@PathVariable UUID id){
        userService.enableUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
