package com.photoChallenger.tripture.domain.login.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailAuthenticationService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail= "${spring.mail.username}";
    private static int number;

    public static void createNumber() {
        number = (int)(Math.random() * (90000)) + 100000; //(int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    public MimeMessage CreateMail(String mail) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h2>" + "[Tripture] 안녕하세요 Triptrue입니다. 회원가입을 위한 인증 번호입니다." + "</h2>";
            body += "<h2>" + "회원가입을 위해 요청하신 인증 번호입니다." + "</h2><br>";
            body += "<h1>" + number + "</h1><br>";
            body += "<h3>" + "회원가입 창으로 돌아가 인증 번호를 입력해 주세요.";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            log.info(e.toString());
        }

        return message;
    }

    public int sendMail(String mail) {
        MimeMessage message = CreateMail(mail);
        javaMailSender.send(message);

        return number;
    }
}
