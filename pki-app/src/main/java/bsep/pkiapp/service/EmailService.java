package bsep.pkiapp.service;

import bsep.pkiapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    @Async
    public void sendRegistrationEmail(User user, String token) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom(env.getProperty("spring.mail.username"));
        mail.setSubject("Registration confirmation");
        mail.setText("Hello " + user.getName() + ",\n\nTo confirm your account, please click here : "
                + "https://localhost:4200/account-validation/" + token);
        javaMailSender.send(mail);
    }

    @Async
    public void sendRecoveryEmail(User user, String token) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom(env.getProperty("spring.mail.username"));
        mail.setSubject("Account recovery");
        mail.setText("Hello " + user.getName() + ",\n\nTo set up a new password, please click here : "
                + "https://localhost:4200/password-reset/" + token);
        javaMailSender.send(mail);
    }

    @Async
    public void sendLoginEmail(User user, String token) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom(env.getProperty("spring.mail.username"));
        mail.setSubject("Login link");
        mail.setText("Hello " + user.getName() + ",\n\nTo login into you account, please click the following link : "
                + "https://localhost:4200?token=" + token);
        javaMailSender.send(mail);
    }
}
