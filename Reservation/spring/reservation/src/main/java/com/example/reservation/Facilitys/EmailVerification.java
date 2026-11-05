package com.example.reservation.Facilitys;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * 이메일 인증 정보.
 *
 * 이전에는 EmailService 안의 ConcurrentHashMap(서버 메모리)에 저장했지만,
 * 서버가 재시작되면 인증 정보가 사라지고, 서버를 여러 대로 늘리면
 * 코드를 받은 서버와 확인하는 서버가 달라 인증이 실패하는 문제가 있었다.
 * 그래서 DB에 저장하도록 바꿨다. (이메일당 1건만 유지)
 */
@Entity
public class EmailVerification {

    /** 인증번호를 연속으로 틀릴 수 있는 최대 횟수 (6자리 코드 무차별 대입 방지) */
    public static final int MAX_ATTEMPTS = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String email;
    private String verificationCode; // 인증번호
    private LocalDateTime expiresAt;
    private boolean verified;
    private int failedAttempts;

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
        failedAttempts = 0;
    }

    /** 인증번호를 새로 발급할 때 기존 행을 재사용한다 (이메일당 1건 유지) */
    public void renew(String _verificationCode, LocalDateTime _expiresAt)
    {
        verificationCode = _verificationCode;
        expiresAt = _expiresAt;
        verified = false;
        failedAttempts = 0;
    }

    public boolean isExpires() {
        return LocalDateTime.now().isAfter(expiresAt); // 만료 시간이 지났으면 true
    }

    public boolean isLocked() {
        return failedAttempts >= MAX_ATTEMPTS;
    }

    public void recordFailedAttempt() {
        failedAttempts++;
    }

    public void verify() {
        verified = true;
    }

    public long getId()
    {
        return id;
    }

    public String getEmail()
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

    public int getFailedAttempts()
    {
        return failedAttempts;
    }

    public LocalDateTime getExpiresAt()
    {
        return expiresAt;
    }
}
