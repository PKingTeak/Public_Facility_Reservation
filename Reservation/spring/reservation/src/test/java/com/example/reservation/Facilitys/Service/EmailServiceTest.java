package com.example.reservation.Facilitys.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.reservation.Facilitys.EmailVerification;
import com.example.reservation.Facilitys.Repository.EmailVerificationRepository;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    private static final String EMAIL = "test@test.com";
    private static final String CODE = "123456";

    @Mock
    EmailVerificationRepository repository;
    @Mock
    JavaMailSender mailSender;

    @InjectMocks
    EmailService emailService;

    @DisplayName("올바른 코드를 입력하면 인증된다")
    @Test
    void correctCodeVerifies() {
        EmailVerification v = valid();
        when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(v));

        assertTrue(emailService.verifyCode(EMAIL, CODE));
        assertTrue(v.getVerified());
    }

    @DisplayName("틀린 코드를 입력하면 실패 횟수가 늘어난다")
    @Test
    void wrongCodeIncreasesAttempts() {
        EmailVerification v = valid();
        when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(v));

        assertFalse(emailService.verifyCode(EMAIL, "000000"));
        assertEquals(1, v.getFailedAttempts());
    }

    @DisplayName("[경계값] 5번 틀리면 이후에는 올바른 코드도 거절된다 (무차별 대입 방지)")
    @Test
    void lockedAfterMaxAttempts() {
        EmailVerification v = valid();
        when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(v));

        for (int i = 0; i < EmailVerification.MAX_ATTEMPTS; i++) {
            assertFalse(emailService.verifyCode(EMAIL, "000000"));
        }

        assertFalse(emailService.verifyCode(EMAIL, CODE), "잠긴 뒤에는 정답도 실패");
        assertFalse(v.getVerified());
    }

    @DisplayName("만료된 코드는 실패하고 DB에서 삭제된다")
    @Test
    void expiredCodeIsDeleted() {
        EmailVerification v = new EmailVerification(EMAIL, CODE, LocalDateTime.now().minusSeconds(1));
        when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(v));

        assertFalse(emailService.verifyCode(EMAIL, CODE));
        verify(repository).delete(v);
    }

    @DisplayName("인증 요청 기록이 없으면 실패한다")
    @Test
    void unknownEmailFails() {
        when(repository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertFalse(emailService.verifyCode(EMAIL, CODE));
        assertFalse(emailService.checkVerify(EMAIL));
    }

    @DisplayName("재발급하면 기존 행을 갱신하고 실패 횟수가 초기화된다")
    @Test
    void resendRenewsExistingRow() {
        EmailVerification v = valid();
        v.recordFailedAttempt();
        v.recordFailedAttempt();
        when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(v));

        emailService.sendVerificationCode(EMAIL);

        assertEquals(0, v.getFailedAttempts());
        assertFalse(v.getVerified());
        verify(repository).save(v);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    private EmailVerification valid() {
        return new EmailVerification(EMAIL, CODE, LocalDateTime.now().plusMinutes(3));
    }
}
