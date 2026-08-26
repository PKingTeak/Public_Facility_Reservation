package com.example.reservation.Facility;

import org.springframework.stereotype.Service;

import com.example.reservation.Facility.DTO.UserRequest;

import java.util.Collection;
import java.util.HashMap;


@Service
public class UserService {
 
    HashMap<Long,User> userHmap = new HashMap<Long,User>();


    public void registerUser(UserRequest userRequest)
    {
        User user = new User(userRequest.getName(),userRequest.getAge());
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

    public Long getUserByName(String name)
    {
        for(User user : userHmap.values())
        {
            if(user.getName().equals(name))
            {
                return user.getUserId();
            }
        }

       throw new IllegalStateException("해당 이름의 사용자가 없습니다.");
    }
    public Collection<User> getAllUserData()
    {

        if(userHmap.isEmpty())
        {
            throw new IllegalStateException("userHmap이 비어있습니다 유저 데이터가 없습니다.");
        }
      
        return userHmap.values();
    }



}