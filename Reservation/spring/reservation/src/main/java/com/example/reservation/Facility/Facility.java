package com.example.reservation.Facility;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String location;
    private int capacity; //수용가능 인원

    protected Facility() {
        // JPA requires a default constructor
    }

    Facility(String _name, String _location, int _capacity ) {
        this.name = _name;
        this.location = _location;
        this.capacity = _capacity;

    }

    public String getFacilityName() {
        return this.name;
    }

    public String getFacilityLocation() {
        return this.location;
    }

    public int getFacilityCapacity() {
        return this.capacity;
    }

    public long getFacilityId() {
        return this.id;
    }


}