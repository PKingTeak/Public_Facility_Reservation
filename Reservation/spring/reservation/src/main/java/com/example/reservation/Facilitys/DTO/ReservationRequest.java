package com.example.reservation.Facilitys.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public class ReservationRequest {
   //Vaildation을 사용하여 들어오는 값이 유효한 값인지 확인 
    @Positive
    private long facilityId;
    @NotNull
    private LocalDate date;
    @NotNull 
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;

  
    public void setFacilityId(long _facilityId)
    {
        facilityId = _facilityId;
    }

    public void setDate(LocalDate _date)
    {
        date = _date;
    }
    public void setStartTime(LocalTime _startTime)
    {
        startTime = _startTime;
    }
    public void setEndTime(LocalTime _endTime)
    {
        endTime = _endTime;
    }
    
    public LocalDate getDate()
    {
        return date;
    }
    
   
  
    public long getFacilityId()
    {
        return facilityId;
    }

    public LocalTime getStartTime()
    {
        return startTime;
    }

    public LocalTime getEndTime()
    {
        return endTime;
    }
    




}
