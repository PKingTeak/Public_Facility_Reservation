package com.example.reservation.Facility.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reservation.Facility.UserService;
import com.example.reservation.Facility.DTO.UserRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService _UserService)
    {
        userService = _UserService;
    }

    @PostMapping
    public void registerUser(@RequestBody UserRequest request)
    {
        userService.registerUser(request);
    }
    

}
