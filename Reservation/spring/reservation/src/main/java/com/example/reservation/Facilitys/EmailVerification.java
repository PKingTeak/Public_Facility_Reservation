package com.example.reservation.Facilitys;

import java.time.LocalDateTime;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String verificationCode; // 인증번호
    private LocalDateTime expiresAt;
    private boolean verified;


    protected EmailVerification()
    {
        //JPA Entity
    }

    public EmailVerification(String _email, String _verificationCode, LocalDateTime _expiresAt)
    {
        email = _email;
        verificationCode = _verificationCode;
        expiresAt = _expiresAt;
        verified = false;
        
    }

    public boolean isExpires() {
        return LocalDateTime.now().isAfter(expiresAt); // 해당 시간이 아니면 false
    }

    public void verify() {
        verified = true;
    }

    public long getId()
    {
        return id;
    }

    public String getemail()
    {
        return email;
    }

    public String getVerificationCode()
    {
     return verificationCode;   
    }

    public boolean getVerified()
    {
        return verified;
    }
    public LocalDateTime getExpirTime()
    {
        return expiresAt;
    }
}
