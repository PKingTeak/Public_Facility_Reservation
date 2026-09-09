package com.example.reservation.Facility.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

//회원가입 DTO
public class UserRequest {

    @NotBlank 
    private String name;
    @Email
    @NotNull 
    private String email;
    @Min (value = 1, message = "나이는 0보다 작을수 없습니다")
    private int age;
    
    @NotBlank
    private String password;

    
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

 
}
