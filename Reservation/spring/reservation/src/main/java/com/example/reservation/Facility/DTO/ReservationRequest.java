package com.example.reservation.Facility.DTO;

import java.time.LocalDate;
import java.time.LocalTime;


public class ReservationRequest {
   
    private long userId;
    private long facilityId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public void setUserId(long _userId)
    {
        userId = _userId;
    }
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
    
   
    public long getUserId()
    {
        return userId;
    }

    public long getFailityId()
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
