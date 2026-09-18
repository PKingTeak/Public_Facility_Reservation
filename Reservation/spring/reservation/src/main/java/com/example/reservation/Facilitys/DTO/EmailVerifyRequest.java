package com.example.reservation.Facilitys.DTO;

import jakarta.validation.constraints.NotNull;

public class EmailVerifyRequest {
    @NotNull 
    private String email;

    private String verificationCode;

    public void setEmail(String _input)
    {
        email  = _input;
    }
    public void setVerificationCode(String _verificationCode)
    {
        verificationCode = _verificationCode;
    }

    public String getEmail()
    {
        return email;
    }

    public String getVerificationCode()
    {
        return verificationCode;
    }
}
