package com.example.reservation.Facility.DTO;

import com.example.reservation.Facility.Email;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UserRequest {

    @NotNull 
    private String name;
    @Email 
    @NotNull 
    private String email;
    @Min (value = 0, message = "나이는 0보다 작을수 없습니다")
    private int age;
    @Positive 
    private long id;

    public long getId() {
        return id;
    }

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
}
