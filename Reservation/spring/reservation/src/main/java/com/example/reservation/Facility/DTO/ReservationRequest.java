package com.example.reservation.Facility.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationRequest {
    private long resId;
    private long userId;
    private long facilityId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    

    
    public LocalDate getDate()
    {
        return date;
    }
    
    public long getresId()
    {
        return resId;
    }

    public long getuserId()
    {
        return userId;
    }

    public long getfacilityId()
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
