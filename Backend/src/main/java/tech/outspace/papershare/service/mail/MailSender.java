package tech.outspace.papershare.service.mail;


import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import tech.outspace.papershare.utils.network.EmailFormat;

import java.util.Date;

@Slf4j
@Service
public class MailSender {
    private static final String mailSubject = "PaperShare 账号管理";
    private static final String mailFrom = "papershare2022@163.com";
    private final JavaMailSenderImpl mailSender;

    public MailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendCheckCodeEmail(String to, String code, String mailNo) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject(mailSubject);
        message.setFrom(mailFrom);
        message.setTo(to);

        Date date = new Date();
        message.setSentDate(date);
        message.setText(EmailFormat.emailContGen(code, mailNo, date));
        try {
            mailSender.send(message);
            log.debug("A verify email has sent to " + to);
            return true;
        } catch (Exception e) {
            log.debug("Mail send to " + to + " failed");
            return false;
        }
    }
}
