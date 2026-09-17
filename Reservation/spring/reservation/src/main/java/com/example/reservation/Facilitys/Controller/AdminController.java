package com.example.reservation.Facilitys.Controller;

import com.example.reservation.Facilitys.Service.UserService;
import com.example.reservation.Facilitys.DTO.AdminUserResponse;
import com.example.reservation.Facilitys.DTO.RoleChangeRequest;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.Collection;




@RequestMapping ("/admin")
@RestController
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
    
     
    @GetMapping("/admin/users")
    public Collection<AdminUserResponse> getAllUserData()
    {
        return service.getAllUserDataFromAdmin();
        
    }
    

    
}
