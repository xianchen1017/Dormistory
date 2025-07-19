package com.example.springboot.service;

public interface EmailService {
    /**
     * 发送验证码邮件
     * @param to 收件人邮箱
     * @param code 验证码
     * @return 是否发送成功
     */
    boolean sendVerificationCode(String to, String code);
    
    /**
     * 生成6位数字验证码
     * @return 验证码
     */
    String generateVerificationCode();
} 