package com.example.reservation.Facility.Controller;

import com.example.reservation.Facility.UserService;
import com.example.reservation.Facility.DTO.AdminUserResponse;
import com.example.reservation.Facility.DTO.RoleChangeRequest;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.Collection;




@RestController
@RequestMapping ("/admin")
public class AdminController {

    private final UserService service;

    public AdminController(UserService _Service)
    {
        service = _Service;
    }

    
    @GetMapping("users/{id}")
    public AdminUserResponse getuserbyID(@PathVariable Long id)
    {
        return service.getUserDataFromAdmin(id);       
    }   

    @PatchMapping("/users/{id}/role")
    public void changeuserRole(@PathVariable Long id,@Valid @RequestBody RoleChangeRequest changeRequest)
    {
        service.changeRole(id,changeRequest.getRole());
    }    
    
     
    @GetMapping("/users")
    public Collection<AdminUserResponse> getAllUserData()
    {
        return service.getAllUserDataFromAdmin();
        
    }
    

    
}
