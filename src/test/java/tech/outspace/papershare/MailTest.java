package tech.outspace.papershare;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;
import java.util.Random;

@SpringBootTest
public class MailTest {

    @Autowired
    JavaMailSender javaMailSender;

    @Test
    public void sendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject("PaperShare 测试邮件");
        message.setFrom("papershare2022@163.com");
        message.setTo("xw1216@outlook.com");

        Random random = new Random();
        int code = random.nextInt(9000) + 1000;
        message.setSentDate(new Date());
        message.setText("注册 PaperShare 的验证码为 :" + code + "\n有效期：30 分钟");

        javaMailSender.send(message);
    }
}
