package tech.outspace.papershare;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import tech.outspace.papershare.utils.network.EmailFormat;

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
        message.setTo("1135118349@qq.com");

        Random random = new Random();
        int code = random.nextInt(9000) + 1000;
        message.setSentDate(new Date());
        message.setText("您注册 PaperShare 的验证码为：\n\t" + code + "\n有效期：10 分钟");

        javaMailSender.send(message);
    }

    @Test
    public void checkEmail() {
        String email = "xw1216@outlook.com";
        System.out.println(email);
        System.out.println(EmailFormat.checkEmailFormat(email));
    }


}
