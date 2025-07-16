<template>
  <div class="register-container">
    <vue-particles
        color="#ffffff"
        :particleOpacity="0.5"
        :particlesNumber="80"
        shapeType="circle"
        :particleSize="5"
        linesColor="#ffffff"
        :linesWidth="1"
        :lineLinked="true"
        :lineOpacity="0.5"
        :linesDistance="150"
        :moveSpeed="2"
        :hoverEffect="true"
        hoverMode="repulse"
        :clickEffect="true"
        clickMode="push"
    />
    <div class="register-box">
      <h1 class="title">学生宿舍管理系统</h1>
      <h2 class="subtitle">用户注册</h2>

      <el-form
          ref="registerFormRef"
          :model="registerForm"
          :rules="rules"
          label-position="top"
          class="register-form"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
              v-model="registerForm.username"
              placeholder="请输入用户名"
              prefix-icon="User"
          />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="Lock"
              show-password
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="请再次输入密码"
              prefix-icon="Lock"
              show-password
          />
        </el-form-item>

        <el-form-item label="学号" prop="studentId">
          <el-input
              v-model="registerForm.studentId"
              placeholder="请输入学号"
              prefix-icon="Document"
          />
        </el-form-item>

        <el-form-item label="年龄" prop="age">
          <el-input
              v-model.number="registerForm.age"
              type="number"
              placeholder="请输入年龄"
              prefix-icon="User"
          />
        </el-form-item>

        <!-- 新增性别字段 -->
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="registerForm.gender">
            <el-radio label="男" />
            <el-radio label="女" />
            <el-radio label="未知" />
          </el-radio-group>
        </el-form-item>

        <!-- 新增邮箱字段 -->
        <el-form-item label="邮箱" prop="email">
          <el-input
              v-model="registerForm.email"
              type="email"
              placeholder="请输入邮箱"
              prefix-icon="Message"
          />
        </el-form-item>

        <el-form-item label="手机号" prop="phone">
          <el-input
              v-model="registerForm.phone"
              placeholder="请输入手机号"
              prefix-icon="Iphone"
          />
        </el-form-item>

        <!--新增班级字段-->
        <el-form-item label="班级" prop="classroom">
          <el-input
              v-model="registerForm.classroom"
              placeholder="请输入班级"
              prefix-icon="ClassRoom"
          />
        </el-form-item>

        <el-form-item>
          <el-button
              type="primary"
              @click="submitForm"
              class="register-btn"
              :loading="loading"
          >
            立即注册
          </el-button>
        </el-form-item>

        <div class="login-link">
          已有账号？<router-link to="/login">立即登录</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { useRegister } from '@/assets/js/Register'

const {
  registerForm,
  rules,
  loading,
  registerFormRef,
  submitForm
} = useRegister()
</script>

<style scoped>
.register-container {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100%;
  overflow: hidden;
}
.register-container >>> #particles-js {
  position: absolute;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
  z-index: 0;
}

#tsparticles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
}

.register-box {
  position: relative;
  z-index: 1;
  width: 450px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 10px;
  box-shadow: 0 15px 25px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(5px);
  margin: 20px;
}

.title {
  margin: 0 0 10px;
  padding: 0;
  color: #1e3c72;
  text-align: center;
  font-size: 28px;
  font-weight: bold;
}

.subtitle {
  margin: 0 0 30px;
  padding: 0;
  color: #666;
  text-align: center;
  font-size: 18px;
}

.register-form {
  margin-top: 20px;
}

.captcha-container {
  display: flex;
  gap: 10px;
}

.captcha-btn {
  width: 120px;
}

.register-btn {
  width: 100%;
  margin-top: 10px;
  height: 40px;
  font-size: 16px;
}

.login-link {
  margin-top: 20px;
  text-align: center;
  color: #666;
}

.login-link a {
  color: #1e3c72;
  text-decoration: none;
  font-weight: bold;
}

.login-link a:hover {
  text-decoration: underline;
}

:deep(.el-form-item__label) {
  font-weight: bold;
  color: #333;
}
</style>