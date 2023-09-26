package fr.eventually.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public Map<String,String> postEmail(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getTo());
        message.setSubject(email.getSubject());
        message.setText(email.getBody());
        emailSender.send(message);

        Map<String,String> response = new HashMap<>();
        response.put("contents","Message envoyé, merci \uD83D\uDCE8");
        response.put("type","work");
        return response;
    }
}
