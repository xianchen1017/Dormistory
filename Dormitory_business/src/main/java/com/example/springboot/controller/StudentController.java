package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.pojo.Student;
import com.example.springboot.pojo.User;
import com.example.springboot.service.StudentService;
import com.example.springboot.service.DormRoomService;
import com.example.springboot.service.EmailService;
import com.example.springboot.common.VerificationCodeManager;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/stu")
public class StudentController {

    @Resource
    private StudentService studentService;
    
    @Resource
    private DormRoomService dormRoomService;
    
    @Resource
    private EmailService emailService;
    
    @Resource
    private VerificationCodeManager verificationCodeManager;

    /**
     * 发送注册验证码
     */
    @PostMapping("/sendVerificationCode")
    public Result<?> sendVerificationCode(@RequestBody Student student) {
        try {
            String email = student.getEmail();
            if (email == null || email.trim().isEmpty()) {
                return Result.error("-1", "邮箱不能为空");
            }
            
            // 检查邮箱格式
            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                return Result.error("-1", "邮箱格式不正确");
            }
            
            // 检查是否已有验证码且未过期
            if (verificationCodeManager.hasCode(email)) {
                return Result.error("-1", "验证码已发送，请稍后再试");
            }
            
            // 生成验证码
            String code = emailService.generateVerificationCode();
            
            // 发送邮件
            boolean sent = emailService.sendVerificationCode(email, code);
            if (sent) {
                // 存储验证码
                verificationCodeManager.storeCode(email, code);
                return Result.success("验证码已发送到您的邮箱");
            } else {
                // 如果邮件发送失败，使用测试模式（仅用于开发测试）
                System.out.println("邮件发送失败，使用测试模式");
                System.out.println("测试验证码: " + code + " (仅用于开发测试)");
                verificationCodeManager.storeCode(email, code);
                return Result.success("测试模式：验证码已生成，请查看控制台输出。实际验证码: " + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("-1", "发送验证码失败: " + e.getMessage());
        }
    }

    /**
     * 学生注册
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody Student student) {
        try {
            System.out.println("接收到的注册数据: " + student);

            // 1. 检查用户名(学号)是否已存在
            Student existStudent = studentService.stuInfo(student.getUsername());
            System.out.println("检查用户存在性结果: " + existStudent);

            if (existStudent != null) {
                return Result.error("-1", "该学号已注册");
            }

            // 2. 验证邮箱验证码
            String email = student.getEmail();
            String verificationCode = student.getVerificationCode();
            
            if (email == null || email.trim().isEmpty()) {
                return Result.error("-1", "邮箱不能为空");
            }
            
            if (verificationCode == null || verificationCode.trim().isEmpty()) {
                return Result.error("-1", "验证码不能为空");
            }
            
            if (!verificationCodeManager.verifyCode(email, verificationCode)) {
                return Result.error("-1", "验证码错误或已过期");
            }

            // 3. 设置默认值
            if(student.getName() == null) {
                student.setName(student.getUsername());
            }
            student.setAvatar("https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png");

            // 4. 保存到数据库
            int i = studentService.addNewStudent(student);
            System.out.println("插入结果: " + i);

            if (i == 1) {
                return Result.success();
            } else {
                return Result.error("-1", "注册失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("-1", "注册过程中发生错误: " + e.getMessage());
        }
    }

    /**
     * 添加学生信息
     */
    @PostMapping("/add")
    public Result<?> add(@RequestBody Student student) {
        int i = studentService.addNewStudent(student);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "添加失败");
        }

    }

    /**
     * 更新学生信息
     */
    @PutMapping("/update")
    public Result<?> update(@RequestBody Student student) {
        int i = studentService.updateNewStudent(student);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error("-1", "更新失败");
        }
    }

    /**
     * 删除学生信息
     */
    @DeleteMapping("/delete/{username}")
    public Result<?> delete(@PathVariable String username) {
        try {
            System.out.println("开始删除学生: " + username);
            
            // 1. 先释放该学生的床位
            System.out.println("步骤1: 释放学生床位");
            dormRoomService.releaseBedByStudent(username);
            
            // 2. 删除学生信息
            System.out.println("步骤2: 删除学生信息");
            int i = studentService.deleteStudent(username);
            if (i == 1) {
                System.out.println("学生 " + username + " 删除成功");
                return Result.success();
            } else {
                System.out.println("学生 " + username + " 删除失败");
                return Result.error("-1", "删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("删除学生 " + username + " 时发生异常: " + e.getMessage());
            return Result.error("-1", "删除学生时发生错误: " + e.getMessage());
        }
    }

    /**
     * 查找学生信息
     */
    @GetMapping("/find")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        Page page = studentService.find(pageNum, pageSize, search);
        if (page != null) {
            return Result.success(page);
        } else {
            return Result.error("-1", "查询失败");
        }
    }

    /**
     * 学生登录
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user, HttpSession session) {
        System.out.println("登录请求 - 用户名: " + user.getUsername() + ", 密码: " + user.getPassword());
        try {
            Student student = studentService.stuInfo(user.getUsername());
            System.out.println("查询到的学生: " + student);
            if (student != null) {
                System.out.println("数据库密码: " + student.getPassword() + ", 输入密码: " + user.getPassword());
                if (student.getPassword().equals(user.getPassword())) {
                    System.out.println("密码验证成功");
                    // 设置session
                    session.setAttribute("User", student);
                    session.setAttribute("Identity", "stu");
                    return Result.success(student);
                }
            }
            return Result.error("-1", "用户名或密码错误");
        } catch (Exception e) {
            System.out.println("登录异常" + e);
            return Result.error("-1", "登录异常: " + e.getMessage());
        }
    }

    /**
     * 主页顶部：学生统计
     */
    @GetMapping("/stuNum")
    public Result<?> stuNum() {
        int num = studentService.stuNum();
        if (num > 0) {
            return Result.success(num);
        } else {
            return Result.error("-1", "查询失败");
        }
    }


    /**
     * 床位信息，查询是否存在该学生
     * 床位信息，查询床位上的学生信息
     */
    @GetMapping("/exist/{value}")
    public Result<?> exist(@PathVariable String value) {
        Student student = studentService.stuInfo(value);
        if (student != null) {
            return Result.success(student);
        } else {
            return Result.error("-1", "不存在该学生");
        }
    }
}
