package com.example.reservation.Facility;


import jakarta.persistence.Entity;
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
    //성별
    

    protected User() {
        // JPA requires a default constructor
    
    
    }

    public User(String _name, int _age)
    {
        this.name = _name;
        this.age = _age;
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

}