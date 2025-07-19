package com.example.springboot.service.impl;

import com.example.springboot.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public boolean sendVerificationCode(String to, String code) {
        try {
            System.out.println("开始发送邮件到: " + to);
            System.out.println("发件人邮箱: " + from);
            System.out.println("验证码: " + code);
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject("学生宿舍管理系统 - 注册验证码");
            message.setText("您的注册验证码是：" + code + "\n\n验证码有效期为5分钟，请尽快完成注册。\n\n如果这不是您的操作，请忽略此邮件。");
            
            System.out.println("邮件内容设置完成，开始发送...");
            mailSender.send(message);
            System.out.println("邮件发送成功！");
            return true;
        } catch (Exception e) {
            System.err.println("邮件发送失败，错误信息: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
} 