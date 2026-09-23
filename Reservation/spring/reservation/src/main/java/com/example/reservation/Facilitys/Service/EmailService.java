package com.example.reservation.Facilitys.Service;

import org.springframework.stereotype.Service;
import com.example.reservation.Facilitys.EmailVerification;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {

  private final Map<String, EmailVerification> verifications;
  private final JavaMailSender mailSender;
  private final SecureRandom random = new SecureRandom();

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
    verifications = new ConcurrentHashMap<String, EmailVerification>();
  }

  public void sendVerificationCode(String _email) {
    String code = createVerificationCode();
    SimpleMailMessage message = new SimpleMailMessage();

    message.setTo(_email);
    message.setSubject("이메일 인증번호");
    message.setText("이메일 인증번호 : " + code + "입니다");

    LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(3);
    EmailVerification verification = new EmailVerification(_email, code, expiresAt);
    System.out.println("저장된 코드 = [" + verification.getVerificationCode() + "]");
    verifications.put(_email, verification);
    mailSender.send(message);
  }

  public boolean checkVerify(String _email) {
    EmailVerification verification = verifications.get(_email);

    if (verification == null) {
      return false;
    }
    return verification.getVerified();
  }

  public boolean verifyCode(String _email, String _inputCode) {
    EmailVerification verification = verifications.get(_email);
    if (verification == null) {
      // 이메일 존재
      return false;
    }
    if (verification.isExpires()) {
      // 시간 비교
      verifications.remove(_email);
      return false;
    }
    if (!verification.getVerificationCode().equals(_inputCode)) {
      return false;
    }
    verification.verify();
    return true;
  }

  private String createVerificationCode() {
    int code = random.nextInt(900000) + 100000;
    return String.valueOf(code);
  }

}