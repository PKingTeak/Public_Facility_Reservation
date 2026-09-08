package com.example.reservation.Facility;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.reservation.Facility.DTO.UserRequest;
import com.example.reservation.Facility.Exception.DataNotFoundException;
import com.example.reservation.Facility.Exception.InvalidRequestException;
import com.example.reservation.Facility.Repository.UserRepository;

import java.util.Collection;

//비즈니스 로직 담당 클래스 
@Service
public class UserService {
 

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; //비밀번호 암호화 객체


    UserService(UserRepository _UserRepository , PasswordEncoder _passwordEncoder)
    {

        userRepository = _UserRepository;
        passwordEncoder = _passwordEncoder;
    }


    public void registerUser(UserRequest userRequest)
    {
        if(userRequest.getAge() <= 0)
        {
            throw new InvalidRequestException("잘못된 나이를 지정하였습니다");
        }
        String inputpassword = passwordEncoder.encode(userRequest.getPassword()); //비밀번호 암호화 객체를 가져옴;
        User user = new User(userRequest.getName(),userRequest.getAge(),userRequest.getEmail(),inputpassword); //User 비밀번호는 해시 문자열을 가지게 함.
      
        userRepository.save(user);
    }

    public User getUserById(long id)
    {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("해당하는 유저 아이디가 존재하지 않습니다."));
       //수정 기존은 맵에서 채크하고 throw를 해줬음
        
       return user;
    }

    public Long getUserIdByName(String name)
    {
        return userRepository.findByName(name).orElseThrow(()-> new DataNotFoundException("해당하는 유저" + name + "이 없습니다")).getId();
    }
    public User getUserByName(String name)
    {
        User user = userRepository.findByName(name).orElseThrow(()-> new DataNotFoundException("해당하는 유저 " + name +"이 없습니다"));
        
        return user;
    }
    public Collection<User> getAllUserData()
    {
        return userRepository.findAll();
        //return userHmap.values();
    }


    public User getUserByEmail(String email)
    {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new DataNotFoundException("해당 " + email +"에 일치하는 유저가 없습니다."));
        return user;
    }

}