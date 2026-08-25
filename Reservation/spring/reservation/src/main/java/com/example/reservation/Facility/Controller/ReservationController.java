package com.example.reservation.Facility.Controller;


import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reservation.Facility.Reservation;
import com.example.reservation.Facility.ReservationService;
import com.example.reservation.Facility.DTO.ReservationRequest;;;


@RestController
@RequestMapping("/reservation")
public class ReservationController {
 
    private final ReservationService reservationService;


    public ReservationController(ReservationService _ReservationService)
    {
        this.reservationService = _ReservationService;
    }

    @GetMapping("/{date}/{id}")
    public Reservation selectReservation(@PathVariable LocalDate date,@PathVariable long id)
    {
        return reservationService.getReservation(date, id);
    }


    @PostMapping
    public void addReserVation(@RequestBody ReservationRequest request)
    {
        reservationService.addReservation(request); //오버라이드로 하나 생성해서 값 넣어주는방식으로 수정할 예정
    }

    
    
}
