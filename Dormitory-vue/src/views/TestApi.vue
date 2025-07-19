<template>
  <div class="test-container">
    <h2>API 测试页面</h2>
    
    <div class="test-section">
      <h3>测试基础连接</h3>
      <el-button @click="testBasicConnection">测试基础连接</el-button>
      <p v-if="basicResult">结果: {{ basicResult }}</p>
    </div>
    
    <div class="test-section">
      <h3>测试验证码发送</h3>
      <el-input v-model="testEmail" placeholder="输入测试邮箱" style="width: 300px; margin-right: 10px;" />
      <el-button @click="testSendCode" :loading="sendingTest">发送验证码</el-button>
      <p v-if="codeResult">结果: {{ codeResult }}</p>
    </div>
    
    <div class="test-section">
      <h3>测试注册流程</h3>
      <el-form :model="testRegisterForm" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="testRegisterForm.username" placeholder="用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="testRegisterForm.password" type="password" placeholder="密码" />
        </el-form-item>
        <el-form-item label="学号">
          <el-input v-model="testRegisterForm.studentId" placeholder="学号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="testRegisterForm.email" placeholder="邮箱" />
        </el-form-item>
        <el-form-item label="验证码">
          <el-input v-model="testRegisterForm.verificationCode" placeholder="验证码" />
        </el-form-item>
        <el-form-item>
          <el-button @click="testRegister" :loading="registeringTest">测试注册</el-button>
        </el-form-item>
      </el-form>
      <p v-if="registerResult">结果: {{ registerResult }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const basicResult = ref('')
const codeResult = ref('')
const registerResult = ref('')
const testEmail = ref('')
const sendingTest = ref(false)
const registeringTest = ref(false)

const testRegisterForm = reactive({
  username: 'testuser',
  password: '123456',
  studentId: '12345',
  email: 'test@example.com',
  verificationCode: '123456'
})

const testBasicConnection = async () => {
  try {
    const response = await request.get('/test/hello')
    basicResult.value = JSON.stringify(response)
    ElMessage.success('基础连接测试成功')
  } catch (error) {
    basicResult.value = '错误: ' + error.message
    ElMessage.error('基础连接测试失败')
  }
}

const testSendCode = async () => {
  if (!testEmail.value) {
    ElMessage.error('请输入邮箱')
    return
  }
  
  try {
    sendingTest.value = true
    const response = await request.post('/stu/sendVerificationCode', {
      email: testEmail.value
    })
    codeResult.value = JSON.stringify(response)
    ElMessage.success('验证码发送测试完成')
  } catch (error) {
    codeResult.value = '错误: ' + error.message
    ElMessage.error('验证码发送测试失败')
  } finally {
    sendingTest.value = false
  }
}

const testRegister = async () => {
  try {
    registeringTest.value = true
    
    const requestData = {
      username: testRegisterForm.studentId,
      password: testRegisterForm.password,
      name: testRegisterForm.username,
      phoneNum: '13800138000',
      age: 20,
      gender: '男',
      email: testRegisterForm.email,
      classroom: '计算机1班',
      verificationCode: testRegisterForm.verificationCode
    }
    
    const response = await request.post('/stu/register', requestData)
    registerResult.value = JSON.stringify(response)
    ElMessage.success('注册测试完成')
  } catch (error) {
    registerResult.value = '错误: ' + error.message
    ElMessage.error('注册测试失败')
  } finally {
    registeringTest.value = false
  }
}
</script>

<style scoped>
.test-container {
  padding: 20px;
}

.test-section {
  margin-bottom: 30px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.test-section h3 {
  margin-top: 0;
  color: #333;
}
</style> 