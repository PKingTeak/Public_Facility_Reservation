package com.example.reservation.Facility.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) //잘못된 값을 요청했을때 사용하는 클래스
public class InvalidRequestException extends RuntimeException {
    
    public InvalidRequestException(String msg)
    {
        super(msg);
    }
}
