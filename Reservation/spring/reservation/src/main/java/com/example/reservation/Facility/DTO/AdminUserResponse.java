package com.example.reservation.Facility.DTO;

import com.example.reservation.Facility.Authorization.Role;

public class AdminUserResponse {

    long userid;
    
    String username;
    
    String email;
    
    int age;

    Role role;

   
    public AdminUserResponse(long _userid,String name,String _email , int _age, Role _role)
    {
        userid = _userid;
        username = name;
        email = _email;
        age   = _age;
        role = _role;
    }


    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public long getId() {
        return userid;
    }

    public String getName() {
        return username;
    }

}