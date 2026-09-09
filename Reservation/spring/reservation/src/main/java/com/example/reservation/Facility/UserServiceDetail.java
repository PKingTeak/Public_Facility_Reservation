package com.example.reservation.Facility;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.reservation.Facility.Exception.DataNotFoundException;

@Service 
public class UserServiceDetail implements UserDetailsService {
    private final UserService userService;

    public UserServiceDetail(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetail loadUserByUsername(String email) {
        User user = userService.getUserByEmail(email);
        if(user == null)
        {
            throw new DataNotFoundException("[UserServiceDetail]해당하는 유저가 없습니다.");
        }
        return new UserDetail(user.getEmail(), user.getPassword(), user.getRole());
    }
    
}

