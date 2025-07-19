# QQ邮箱配置详细步骤

## 问题分析
当前错误：`535 Login fail. Account is abnormal, service is not open, password is incorrect, login frequency limited, or system is busy.`

## 解决步骤

### 1. 开启QQ邮箱SMTP服务

1. **登录QQ邮箱**
   - 访问：https://mail.qq.com
   - 使用您的QQ账号登录

2. **进入设置**
   - 点击右上角的"设置"按钮
   - 在左侧菜单中点击"账户"

3. **开启SMTP服务**
   - 找到"POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV服务"
   - 开启"POP3/SMTP服务"（点击开启）
   - 开启"IMAP/SMTP服务"（点击开启）

4. **身份验证**
   - 系统会要求您进行身份验证
   - 可能需要输入QQ密码或手机验证码
   - 按照提示完成验证

### 2. 获取授权码

1. **生成授权码**
   - 在SMTP服务开启后，点击"生成授权码"
   - 系统会生成一个16位的授权码

2. **保存授权码**
   - 复制生成的授权码
   - 这个授权码就是您需要在配置文件中使用的密码

### 3. 修改配置文件

在 `Dormitory_business/src/main/resources/application.properties` 中：

```properties
# 邮件配置
spring.mail.host=smtp.qq.com
spring.mail.port=587
spring.mail.username=您的QQ邮箱@qq.com
spring.mail.password=您的16位授权码
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.ssl.enable=false
```

**重要提示**：
- `spring.mail.username` 必须是完整的QQ邮箱地址
- `spring.mail.password` 必须是16位授权码，不是QQ密码
- 确保没有多余的空格或特殊字符

### 4. 测试配置

1. **重启后端服务**
   ```bash
   cd Dormitory_business
   mvn spring-boot:run
   ```

2. **测试邮件发送**
   - 访问：http://localhost:8080/test
   - 输入您的邮箱地址
   - 点击"发送验证码"

### 5. 常见问题解决

#### 问题1：授权码不正确
- 确保使用的是最新的授权码
- 如果怀疑授权码泄露，可以重新生成

#### 问题2：SMTP服务未开启
- 确保在QQ邮箱设置中开启了SMTP服务
- 检查是否有未完成的身份验证

#### 问题3：登录频率限制
- QQ邮箱有登录频率限制
- 如果频繁测试，可能需要等待一段时间

#### 问题4：端口问题
- 如果587端口不工作，可以尝试465端口：
  ```properties
  spring.mail.port=465
  spring.mail.properties.mail.smtp.ssl.enable=true
  ```

### 6. 替代方案

如果QQ邮箱配置困难，可以考虑：

1. **使用Gmail**
   ```properties
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your-gmail@gmail.com
   spring.mail.password=your-app-password
   ```

2. **使用163邮箱**
   ```properties
   spring.mail.host=smtp.163.com
   spring.mail.port=25
   spring.mail.username=your-email@163.com
   spring.mail.password=your-authorization-code
   ```

### 7. 开发测试模式

在开发阶段，如果邮件配置有问题，系统会自动进入测试模式：
- 验证码会显示在控制台中
- 可以直接使用控制台显示的验证码进行测试

### 8. 验证成功标志

配置成功后，您应该看到：
```
开始发送邮件到: your-email@qq.com
发件人邮箱: your-email@qq.com
验证码: 123456
邮件内容设置完成，开始发送...
邮件发送成功！
```

如果看到"邮件发送成功！"，说明配置正确。 