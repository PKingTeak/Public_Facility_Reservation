package com.example.reservation.Facilitys.Controller;


import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reservation.Facilitys.Reservation;
import com.example.reservation.Facilitys.Service.ReservationService;
import com.example.reservation.Facilitys.DTO.ReservationRequest;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/reservation")
public class ReservationController {
 
    private final ReservationService reservationService;


    public ReservationController(ReservationService _ReservationService)
    {
        this.reservationService = _ReservationService;
    }


    @PostMapping
    public void addReserVation(
        @Valid @RequestBody ReservationRequest request,
        Authentication  authentication)
    {
        reservationService.addReservation(request,
            authentication.getName());
    }


    @GetMapping("date/{date}/reservation/{id}")
    public Reservation selectReservation(@PathVariable LocalDate date,@PathVariable long id)
    {
        return reservationService.getReservation(date, id);
    }
    
    @GetMapping("/{date}")
    public List<Reservation> getReservationByDate(@PathVariable LocalDate date)
    {
        return reservationService.getReservationsByDate(date);
    }

    
    @GetMapping("AllData/{date}")
    public List<Reservation> getAllReservationByDate(@PathVariable LocalDate date)
    {
        // @PathVariable이 빠져 있어서 date가 항상 null로 들어오던 버그 수정
        return reservationService.getAllReservationByDate(date);
    }
    

    @GetMapping("/{facilityId}/{date}")
    public List<Reservation> getReservationsByFacilityAndDate( @PathVariable long facilityId,@PathVariable LocalDate date)
    {
        return reservationService.getReservationsByFacilityAndDate(facilityId,date);
    }

    
    @DeleteMapping("/{id}")
    public void cancelReservation(@PathVariable long id , Authentication authentication)
    {
        reservationService.cancelReservation(id,authentication.getName());
    }
    
    
    
}
