package com.example.reservation.Facility.DTO;

import com.example.reservation.Facility.Authorization.Role;

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
