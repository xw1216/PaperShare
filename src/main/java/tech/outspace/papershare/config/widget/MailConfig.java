package tech.outspace.papershare.config.widget;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    JavaMailSenderImpl javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.163.com");
        mailSender.setUsername("papershare2022@163.com");
        mailSender.setPassword("MZWPNPUFLOSOLWWD");
        mailSender.setPort(465);
        mailSender.setDefaultEncoding("UTF-8");
        Properties prop = new Properties();
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.setProperty("mail.smtp.starttls.enable", "true");
        mailSender.setJavaMailProperties(prop);
        return mailSender;
    }

}
