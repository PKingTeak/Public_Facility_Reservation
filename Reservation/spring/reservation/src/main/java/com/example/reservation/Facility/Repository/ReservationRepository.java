package com.example.reservation.Facility.Repository;

import java.time.LocalDate;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.reservation.Facility.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation,Long>{
    
    List<Reservation> getReservationByFacilityIdAndDate(Long facilityId,LocalDate _Date);
    List<Reservation> getReservationByFacilityNameAndDate(String facilityName , LocalDate _Date);
    List<Reservation> getReservationByUserIdAndDate(Long userId,LocalDate _Date);

    List<Reservation> getReservationByDate(LocalDate _Date);

}
