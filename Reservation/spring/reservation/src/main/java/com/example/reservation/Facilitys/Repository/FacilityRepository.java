package com.example.reservation.Facilitys.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.reservation.Facilitys.Facility;

import jakarta.persistence.LockModeType;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

    Optional<Facility> findByName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select f from Facility f where f.id = :id")
    Optional<Facility> findByIdWithLock(@Param("id") Long id);
}
