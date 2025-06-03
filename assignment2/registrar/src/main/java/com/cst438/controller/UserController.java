package com.cst438.controller;

import com.cst438.domain.User;
import com.cst438.domain.UserRepository;
import com.cst438.dto.UserDTO;
import com.cst438.service.GradebookServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;


@RestController
public class UserController {

    private final UserRepository userRepository;

    private final GradebookServiceProxy gradebook;

    private final BCryptPasswordEncoder encoder;

    public UserController(
            UserRepository userRepository,
            GradebookServiceProxy gradebook,
            BCryptPasswordEncoder encoder
    ) {
        this.userRepository = userRepository;
        this.gradebook = gradebook;
        this.encoder = encoder;
    }

    /**
     list all users
     */
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public List<UserDTO> findAllUsers() {

        //TODO return list of all users
        return null;
    }


    @PostMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public UserDTO createUser(@RequestBody UserDTO userDTO) {

        //TODO create and save a user Entity
        // generate password as user name + "2025"
        // verify that user type is STUDENT, INSTRUCTOR or ADMIN
        // return updated userDTO
        // String password = userDTO.name()+"2025";
        // String enc_password = encoder.encode(password);
        // send message to gradebook proxy

        return null;
    }


    @PutMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        //TODO admin can only update user name, email, type
        //  send message to gradebook proxy
        //  return updated userDTO
        return null;
    }

    /**
     Admin delete a user
     */
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public void  updateUser(@PathVariable("id") int id) {
        //TODO  delete user entity
        // send message to gradebook proxy
    }

}
