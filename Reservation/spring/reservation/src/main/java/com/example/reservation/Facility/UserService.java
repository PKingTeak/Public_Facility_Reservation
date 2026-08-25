package com.example.reservation.Facility;

import org.springframework.stereotype.Service;

import com.example.reservation.Facility.DTO.UserRequest;

import java.util.HashMap;
@Service
public class UserService {
 
    HashMap<Long,User> userHmap = new HashMap<Long,User>();


    public UserService()
    {
        
    }

    public void registerUser(UserRequest userRequest)
    {
        User user = new User(userRequest.getUserName(),userRequest.getAge());
        userHmap.put(user.getUserId(),user);
    }

    public User getUserById(long id)
    {
        if(userHmap.containsKey(id))
        {
            return userHmap.get(id);
        }

        
        throw new IllegalStateException("해당 아이디는 존재하지 않습니다.");
    }

}