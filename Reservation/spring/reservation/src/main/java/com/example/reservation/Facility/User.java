package com.example.reservation.Facility;


import com.example.reservation.Facility.Authorization.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int age;
    private String email;
    @Enumerated(EnumType.STRING) //테이블에 저장될 때 Enum의 이름을 문자열로 저장하도록 지정
    private Role role = Role.USER;

   private String password;

    //성별
    

    protected User() {
        // JPA requires a default constructor
    
    
    }

    public User(String _name, int _age , String _email ,String _password)
    {
        this.name = _name;
        this.age = _age;
        this.email = _email;
        this.password = _password;
        
    }
    
    public Long getUserId()
    {
        return this.id;
    }
    public String getName()
    {
        return this.name;
    }

    public int getAge()
    {
        return this.age;

    }

    public long getId()
    {
        return this.id;
    }

    public String getEmail()
    {
        return this.email;
    }
    public Role getRole()
    {
        return this.role;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void changeRole(Role _Role)
    {
        role = _Role;
    }

}