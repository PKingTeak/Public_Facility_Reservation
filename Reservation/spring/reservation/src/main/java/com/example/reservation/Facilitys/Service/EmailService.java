package com.example.reservation.Facilitys.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.reservation.Facilitys.EmailVerification;
import com.example.reservation.Facilitys.Repository.EmailVerificationRepository;

@Service
public class EmailService {

  private static final int EXPIRE_MINUTES = 3;

  private final EmailVerificationRepository verificationRepository;
  private final JavaMailSender mailSender;
  private final SecureRandom random = new SecureRandom();

  public EmailService(JavaMailSender mailSender, EmailVerificationRepository verificationRepository) {
    this.mailSender = mailSender;
    this.verificationRepository = verificationRepository;
  }

  /**
   * 인증번호를 발급해 DB에 저장하고 메일로 보낸다.
   * 같은 이메일로 다시 요청하면 기존 행을 새 코드로 갱신한다 (재발급 시 실패 횟수도 초기화).
   * 메일 전송이 실패하면 예외가 나서 트랜잭션이 롤백되므로, 받지도 못한 코드가 DB에 남지 않는다.
   */
  @Transactional
  public void sendVerificationCode(String _email) {
    String code = createVerificationCode();
    LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(EXPIRE_MINUTES);

    EmailVerification verification = verificationRepository.findByEmail(_email)
        .map(existing -> {
          existing.renew(code, expiresAt);
          return existing;
        })
        .orElseGet(() -> new EmailVerification(_email, code, expiresAt));
    verificationRepository.save(verification);

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(_email);
    message.setSubject("이메일 인증번호");
    message.setText("이메일 인증번호 : " + code + " 입니다. (" + EXPIRE_MINUTES + "분 이내 입력)");
    mailSender.send(message);
  }

  /** 회원가입 직전에 호출: 인증을 마친 이메일인지 확인 */
  @Transactional(readOnly = true)
  public boolean checkVerify(String _email) {
    return verificationRepository.findByEmail(_email)
        .map(EmailVerification::getVerified)
        .orElse(false);
  }

  /**
   * 사용자가 입력한 코드를 확인한다.
   * - 만료됐으면 삭제 후 실패
   * - 5번 이상 틀렸으면 더 이상 확인하지 않음 (재발급 필요)
   * - 틀리면 실패 횟수 +1 (트랜잭션 커밋 시 DB에 반영)
   */
  @Transactional
  public boolean verifyCode(String _email, String _inputCode) {
    EmailVerification verification = verificationRepository.findByEmail(_email).orElse(null);
    if (verification == null) {
      return false;
    }
    if (verification.isExpires()) {
      verificationRepository.delete(verification);
      return false;
    }
    if (verification.isLocked()) {
      return false;
    }
    if (_inputCode == null || !verification.getVerificationCode().equals(_inputCode)) {
      verification.recordFailedAttempt();
      return false;
    }
    verification.verify();
    return true;
  }

  /** 회원가입이 끝나면 인증 정보를 지워서 같은 인증으로 재가입하지 못하게 한다 */
  @Transactional
  public void consumeVerification(String _email) {
    verificationRepository.findByEmail(_email).ifPresent(verificationRepository::delete);
  }

  private String createVerificationCode() {
    int code = random.nextInt(900000) + 100000;
    return String.valueOf(code);
  }

}
