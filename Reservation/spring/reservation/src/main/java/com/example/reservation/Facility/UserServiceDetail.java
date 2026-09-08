package com.example.reservation.Facility;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service 
public class UserServiceDetail implements UserDetailsService {
    private final UserService userService;

    public UserServiceDetail(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetail loadUserByUsername(String email) {
        User user = userService.getUserByEmail(email);
        return new UserDetail(user.getEmail(), user.getPassword(), user.getRole());
    }
    
}



/*
CustomUserDetailsService
→ 로그인 식별자(email) 받음 ㅇ
→ UserRepository.findByEmail(email) ㅇ
→ DB에서 User Entity 조회 ㅇ
→ UserDetail로 변환 ㅇ
→ Spring Security에 반환

 */