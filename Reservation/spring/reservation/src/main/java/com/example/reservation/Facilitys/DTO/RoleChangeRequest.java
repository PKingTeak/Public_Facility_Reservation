package com.example.reservation.Facilitys.DTO;

import com.example.reservation.Facilitys.Authorization.Role;

import jakarta.validation.constraints.NotNull;

public class RoleChangeRequest {
    
    @NotNull 
    private Role role;
    
    public void setRole(Role newRole)
    {
        role = newRole;
    }

    public Role getRole()
    {
        return role;
    }
}
