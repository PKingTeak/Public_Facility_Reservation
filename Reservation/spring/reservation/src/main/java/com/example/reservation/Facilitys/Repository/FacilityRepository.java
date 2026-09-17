package com.example.reservation.Facilitys.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.reservation.Facilitys.Facility;

public interface FacilityRepository extends JpaRepository<Facility,Long>{ 
    
    Optional<Facility> findByName(String name);
}
