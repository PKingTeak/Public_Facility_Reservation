package com.example.reservation.Facility;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.reservation.Facility.Authorization.Role;

public class UserDetail implements UserDetails {
  
    private String email;
    private String password; // BCrypt 해시 문자열
    private Role role;

    public UserDetail(String email, String password , Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getUsername()
    {
        return this.email;
    }

    public String getPassword()
    {
        return this.password;
    }

    public Role getRole()
    {
        return this.role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      
        return List.of(new SimpleGrantedAuthority("ROLE_"+ role.name())); 
    }

}


   


