package com.example.reservation.Facilitys.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.reservation.Facilitys.User;
import com.example.reservation.Facilitys.Authorization.Role;
import com.example.reservation.Facilitys.DTO.AdminUserResponse;
import com.example.reservation.Facilitys.DTO.UserRequest;
import com.example.reservation.Facilitys.Exception.DataNotFoundException;
import com.example.reservation.Facilitys.Exception.DuplicateDataException;
import com.example.reservation.Facilitys.Exception.InvalidRequestException;
import com.example.reservation.Facilitys.Repository.UserRepository;

import jakarta.transaction.Transactional;

import java.util.Collection;
import java.util.ArrayList;

//비즈니스 로직 담당 클래스 
@Service
public class UserService {
 

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; //비밀번호 암호화 객체


    UserService(EmailService _EmailService,UserRepository _UserRepository , PasswordEncoder _passwordEncoder)
    {
        emailService = _EmailService;
        userRepository = _UserRepository;
        passwordEncoder = _passwordEncoder;
    }


    @Transactional
    public void registerUser(UserRequest userRequest)
    {
        if(userRequest.getAge() <= 0)
        {
            throw new InvalidRequestException("잘못된 나이를 지정하였습니다");
        }
        if(userRepository.findByEmail(userRequest.getEmail()).isPresent())
        {
            // 같은 이메일로 계정이 두 개 생기면 로그인(이메일 기준) 시 어떤 계정인지 모호해진다
            throw new DuplicateDataException("이미 가입된 이메일입니다");
        }
        if(!emailService.checkVerify(userRequest.getEmail()))
        {
            throw new InvalidRequestException("인증이 안된 유저입니다");
        }
        String inputpassword = passwordEncoder.encode(userRequest.getPassword()); //비밀번호 암호화 객체를 가져옴;
        User user = new User(userRequest.getName(),userRequest.getAge(),userRequest.getEmail(),inputpassword); //User 비밀번호는 해시 문자열을 가지게 함.
        userRepository.save(user);
        emailService.consumeVerification(userRequest.getEmail()); // 한 번 쓴 인증은 폐기
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

    @Transactional 
    public void changeRole(Long id, Role _changeRole)
    {
      User user = userRepository.findById(id).orElseThrow(()-> new DataNotFoundException("해당하는 유저가 없습니다."));
      user.changeRole(_changeRole);

    }


    public AdminUserResponse getUserDataFromAdmin(Long _userID)
    {
        User user = userRepository.findById(_userID).orElseThrow(()-> new DataNotFoundException("해당 유저가 없습니다. "));
        AdminUserResponse output =  new AdminUserResponse(user.getId(),user.getName(),user.getEmail(),user.getAge(),user.getRole());

        return output;
    }

    public Collection<AdminUserResponse> getAllUserDataFromAdmin()
    {
        Collection<User> users = userRepository.findAll();
        Collection<AdminUserResponse> outputs = new ArrayList<>();
        for (User user : users) {
            outputs.add(getUserDataFromAdmin(user.getUserId()));
        }
    
        return outputs;
    }

}