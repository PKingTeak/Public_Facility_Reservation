package com.example.reservation.Facility.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.reservation.Facility.User;
import com.example.reservation.Facility.UserService;
import com.example.reservation.Facility.DTO.UserRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

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
    

    
    @GetMapping("/{id}")
    public  User getUserDataById(@PathVariable long id)
    {
        return userService.getUserById(id);
    }

    @GetMapping("/by-name")
    public long getUserDataByName(@RequestParam  String username)
    {
        return userService.getUserByName(username);
    }

    @GetMapping
    public Collection<User> getAllUserData()
    {
        return userService.getAllUserData();
    }
    

}
