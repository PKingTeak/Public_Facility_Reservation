package com.example.reservation.Facility.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.reservation.Facility.Facility;

public interface FacilityRepository extends JpaRepository<Facility,Long>{ 
    
    Optional<Facility> findByName(String name);
}
