package com.example.reservation.Facility.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateDataException extends RuntimeException {
    //예약이 중복되었을때
    public DuplicateDataException(String msg)
    {
        super(msg);
    }

 
}
