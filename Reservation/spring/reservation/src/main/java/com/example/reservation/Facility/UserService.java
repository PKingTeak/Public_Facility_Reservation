package com.example.reservation.Facility;

import org.springframework.stereotype.Service;

import com.example.reservation.Facility.DTO.UserRequest;
import com.example.reservation.Facility.Exception.DataNotFoundException;
import com.example.reservation.Facility.Exception.InvalidRequestException;

import java.util.Collection;
import java.util.HashMap;


@Service
public class UserService {
 
    HashMap<Long,User> userHmap = new HashMap<Long,User>();


    public void registerUser(UserRequest userRequest)
    {
        if(userRequest.getAge() <= 0)
        {
            throw new InvalidRequestException("잘못된 나이를 지정하였습니다");
        }
        
        User user = new User(userRequest.getName(),userRequest.getAge());
        userHmap.put(user.getUserId(),user);
    }

    public User getUserById(long id)
    {
        if(userHmap.containsKey(id))
        {
            return userHmap.get(id);
        }

        
        throw new DataNotFoundException("해당 아이디는 존재하지 않습니다.");
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

      throw new DataNotFoundException("해당하는 유저 " + name +"이 없습니다");
    }
    public Collection<User> getAllUserData()
    {

        if(userHmap.isEmpty())
        {
            throw new DataNotFoundException("userHmap이 비어있습니다 유저 데이터가 없습니다.");
        }
      
        return userHmap.values();
    }



}