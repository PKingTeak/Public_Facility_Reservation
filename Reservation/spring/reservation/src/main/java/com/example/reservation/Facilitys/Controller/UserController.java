package com.example.reservation.Facilitys.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.reservation.Facilitys.User;
import com.example.reservation.Facilitys.Service.UserService;
import com.example.reservation.Facilitys.DTO.UserRequest;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService _UserService)
    {
        userService = _UserService;
    }

    @PostMapping
    public void registerUser(@Valid @RequestBody UserRequest request)
    {
        userService.registerUser(request);
    }
    
    @GetMapping("/me")
    public String me(Authentication authentication) {
     
           return authentication.getName()
        + " / "
        + authentication.getAuthorities();

    
    }
    

    
    @GetMapping("/{id}")
    public  User getUserDataById(@PathVariable long id)
    {
        return userService.getUserById(id);
    }

    @GetMapping("/by-name")
    public User getUserDataByName(@RequestParam  String username)
    {
        return userService.getUserByName(username);
    }

    @GetMapping("/id-by-name")
    public Long getUserIdByName(@RequestParam String username)
    {
        return userService.getUserIdByName(username);
    }

   
    

}
