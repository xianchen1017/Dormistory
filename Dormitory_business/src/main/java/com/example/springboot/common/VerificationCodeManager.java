package com.example.springboot.common;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class VerificationCodeManager {
    
    // 存储邮箱和验证码的映射关系
    private final Map<String, CodeInfo> codeMap = new ConcurrentHashMap<>();
    
    // 定时任务执行器
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    /**
     * 存储验证码
     * @param email 邮箱
     * @param code 验证码
     */
    public void storeCode(String email, String code) {
        // 移除旧的验证码
        codeMap.remove(email);
        
        // 存储新的验证码
        CodeInfo codeInfo = new CodeInfo(code, System.currentTimeMillis());
        codeMap.put(email, codeInfo);
        
        // 5分钟后自动删除验证码
        scheduler.schedule(() -> {
            codeMap.remove(email);
        }, 5, TimeUnit.MINUTES);
    }
    
    /**
     * 验证验证码
     * @param email 邮箱
     * @param code 验证码
     * @return 是否验证成功
     */
    public boolean verifyCode(String email, String code) {
        CodeInfo codeInfo = codeMap.get(email);
        if (codeInfo == null) {
            return false;
        }
        
        // 检查验证码是否过期（5分钟）
        long currentTime = System.currentTimeMillis();
        if (currentTime - codeInfo.getTimestamp() > 5 * 60 * 1000) {
            codeMap.remove(email);
            return false;
        }
        
        // 验证码匹配
        if (codeInfo.getCode().equals(code)) {
            codeMap.remove(email); // 验证成功后删除验证码
            return true;
        }
        
        return false;
    }
    
    /**
     * 检查邮箱是否已有验证码
     * @param email 邮箱
     * @return 是否已有验证码
     */
    public boolean hasCode(String email) {
        CodeInfo codeInfo = codeMap.get(email);
        if (codeInfo == null) {
            return false;
        }
        
        // 检查验证码是否过期
        long currentTime = System.currentTimeMillis();
        if (currentTime - codeInfo.getTimestamp() > 5 * 60 * 1000) {
            codeMap.remove(email);
            return false;
        }
        
        return true;
    }
    
    /**
     * 验证码信息内部类
     */
    private static class CodeInfo {
        private final String code;
        private final long timestamp;
        
        public CodeInfo(String code, long timestamp) {
            this.code = code;
            this.timestamp = timestamp;
        }
        
        public String getCode() {
            return code;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
    }
} 