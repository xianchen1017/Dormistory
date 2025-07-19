<template>
  <div class="test-container">
    <h2>调宿申请功能测试</h2>
    
    <div class="test-section">
      <h3>当前用户信息</h3>
      <p>用户名: {{ userInfo.username }}</p>
      <p>姓名: {{ userInfo.name }}</p>
      <p>身份: {{ userInfo.identity }}</p>
    </div>
    
    <div class="test-section">
      <h3>我的房间信息</h3>
      <el-button @click="getMyRoom" :loading="loadingRoom">获取房间信息</el-button>
      <div v-if="roomInfo">
        <p>房间号: {{ roomInfo.dormRoomId }}</p>
        <p>楼宇号: {{ roomInfo.dormBuildId }}</p>
        <p>床位1: {{ roomInfo.firstBed }}</p>
        <p>床位2: {{ roomInfo.secondBed }}</p>
        <p>床位3: {{ roomInfo.thirdBed }}</p>
        <p>床位4: {{ roomInfo.fourthBed }}</p>
      </div>
    </div>
    
    <div class="test-section">
      <h3>我的调宿申请</h3>
      <el-button @click="getMyApplications" :loading="loadingApps">获取申请记录</el-button>
      <div v-if="applications.length > 0">
        <h4>申请记录:</h4>
        <ul>
          <li v-for="app in applications" :key="app.id">
            ID: {{ app.id }} | 当前房间: {{ app.currentRoomId }} | 目标房间: {{ app.towardsRoomId }} | 状态: {{ app.state }}
          </li>
        </ul>
      </div>
      <p v-else-if="!loadingApps">暂无申请记录</p>
    </div>
    
    <div class="test-section">
      <h3>所有调宿申请记录（调试用）</h3>
      <el-button @click="getAllApplications" :loading="loadingAllApps">获取所有申请记录</el-button>
      <div v-if="allApplications.length > 0">
        <h4>所有申请记录:</h4>
        <ul>
          <li v-for="app in allApplications" :key="app.id">
            ID: {{ app.id }} | 学号: {{ app.username }} | 姓名: {{ app.name }} | 当前房间: {{ app.currentRoomId }} | 目标房间: {{ app.towardsRoomId }} | 状态: {{ app.state }}
          </li>
        </ul>
      </div>
      <p v-else-if="!loadingAllApps">数据库中暂无任何申请记录</p>
    </div>
    
    <div class="test-section">
      <h3>提交测试申请</h3>
      <el-form :model="testForm" label-width="120px">
        <el-form-item label="目标房间号">
          <el-input v-model.number="testForm.towardsRoomId" placeholder="输入目标房间号" />
        </el-form-item>
        <el-form-item label="目标床位号">
          <el-input v-model.number="testForm.towardsBedId" placeholder="输入目标床位号(1-4)" />
        </el-form-item>
        <el-form-item>
          <el-button @click="submitTestApplication" :loading="submitting">提交测试申请</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <div class="test-section">
      <h3>快速测试数据</h3>
      <el-button @click="createTestData" :loading="creatingTestData">创建测试数据</el-button>
      <el-button type="success" @click="createSimpleTestData" :loading="creatingTestData" style="margin-left: 10px">创建简单测试数据</el-button>
      <p>这将为当前用户创建一个测试调宿申请</p>
    </div>
    
    <div class="test-section">
      <h3>测试结果</h3>
      <pre>{{ testResult }}</pre>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const userInfo = reactive({
  username: '',
  name: '',
  identity: ''
})

const roomInfo = ref(null)
const applications = ref([])
const allApplications = ref([]) // 新增：用于存储所有申请记录
const loadingRoom = ref(false)
const loadingApps = ref(false)
const loadingAllApps = ref(false) // 新增：用于加载所有申请记录
const submitting = ref(false)
const creatingTestData = ref(false)
const testResult = ref('')

const testForm = reactive({
  towardsRoomId: '',
  towardsBedId: ''
})

onMounted(() => {
  getUserInfo()
})

const getUserInfo = () => {
  try {
    const user = JSON.parse(sessionStorage.getItem('user') || '{}')
    const identity = sessionStorage.getItem('identity')
    userInfo.username = user.username || ''
    userInfo.name = user.name || ''
    userInfo.identity = identity || ''
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

const getMyRoom = async () => {
  if (!userInfo.username) {
    ElMessage.error('请先登录')
    return
  }
  
  try {
    loadingRoom.value = true
    const res = await request.get(`/room/getMyRoom/${userInfo.username}`)
    if (res.code === "0") {
      roomInfo.value = res.data
      testResult.value = `房间信息获取成功: ${JSON.stringify(res.data, null, 2)}`
    } else {
      testResult.value = `房间信息获取失败: ${res.msg}`
    }
  } catch (error) {
    testResult.value = `房间信息获取异常: ${error.message}`
  } finally {
    loadingRoom.value = false
  }
}

const getMyApplications = async () => {
  if (!userInfo.username) {
    ElMessage.error('请先登录')
    return
  }
  
  try {
    loadingApps.value = true
    
    console.log('=== 测试页面查询调试信息 ===')
    console.log('查询参数:')
    console.log('  用户学号:', userInfo.username)
    console.log('  页码:', 1)
    console.log('  页大小:', 10)
    
    const res = await request.get('/adjustRoom/find', {
      params: {
        pageNum: 1,
        pageSize: 10,
        search: userInfo.username
      }
    })
    
    console.log('查询响应:')
    console.log('  完整响应:', res)
    console.log('  响应code:', res.code)
    console.log('  响应data:', res.data)
    if (res.data) {
      console.log('    总记录数:', res.data.total)
      console.log('    记录数组:', res.data.records)
    }
    console.log('=== 测试页面调试信息结束 ===')
    
    if (res.code === "0") {
      applications.value = res.data.records || []
      testResult.value = `申请记录获取成功: ${JSON.stringify(res.data, null, 2)}`
    } else {
      testResult.value = `申请记录获取失败: ${res.msg}`
    }
  } catch (error) {
    testResult.value = `申请记录获取异常: ${error.message}`
  } finally {
    loadingApps.value = false
  }
}

const getAllApplications = async () => {
  try {
    loadingAllApps.value = true
    const res = await request.get('/adjustRoom/findAll')
    
    if (res.code === "0") {
      allApplications.value = res.data || []
      testResult.value = `所有申请记录获取成功: ${JSON.stringify(res.data, null, 2)}`
    } else {
      testResult.value = `所有申请记录获取失败: ${res.msg}`
    }
  } catch (error) {
    testResult.value = `所有申请记录获取异常: ${error.message}`
  } finally {
    loadingAllApps.value = false
  }
}

const submitTestApplication = async () => {
  if (!userInfo.username || !roomInfo.value) {
    ElMessage.error('请先获取房间信息')
    return
  }
  
  if (!testForm.towardsRoomId || !testForm.towardsBedId) {
    ElMessage.error('请填写目标房间和床位信息')
    return
  }
  
  try {
    submitting.value = true
    
    // 确定当前床位号
    let currentBedId = 0
    if (roomInfo.value.firstBed === userInfo.username) currentBedId = 1
    else if (roomInfo.value.secondBed === userInfo.username) currentBedId = 2
    else if (roomInfo.value.thirdBed === userInfo.username) currentBedId = 3
    else if (roomInfo.value.fourthBed === userInfo.username) currentBedId = 4
    
    const applicationData = {
      username: userInfo.username,
      name: userInfo.name,
      currentRoomId: roomInfo.value.dormRoomId,
      currentBedId: currentBedId,
      towardsRoomId: testForm.towardsRoomId,
      towardsBedId: testForm.towardsBedId,
      state: '未处理',
      applyTime: new Date().toISOString().slice(0, 19).replace('T', ' ')
    }
    
    const res = await request.post('/adjustRoom/add', applicationData)
    
    if (res.code === "0") {
      testResult.value = `申请提交成功: ${JSON.stringify(res, null, 2)}`
      ElMessage.success('申请提交成功')
      // 刷新申请记录
      getMyApplications()
    } else {
      testResult.value = `申请提交失败: ${res.msg}`
      ElMessage.error(res.msg || '申请提交失败')
    }
  } catch (error) {
    testResult.value = `申请提交异常: ${error.message}`
    ElMessage.error('申请提交异常')
  } finally {
    submitting.value = false
  }
}

const createTestData = async () => {
  if (!userInfo.username) {
    ElMessage.error('请先登录')
    return
  }
  
  try {
    creatingTestData.value = true
    
    // 先获取房间信息
    const roomRes = await request.get(`/room/getMyRoom/${userInfo.username}`)
    let currentRoomId = 101
    let currentBedId = 1
    
    if (roomRes.code === "0" && roomRes.data) {
      currentRoomId = roomRes.data.dormRoomId
      // 确定当前床位号
      if (roomRes.data.firstBed === userInfo.username) currentBedId = 1
      else if (roomRes.data.secondBed === userInfo.username) currentBedId = 2
      else if (roomRes.data.thirdBed === userInfo.username) currentBedId = 3
      else if (roomRes.data.fourthBed === userInfo.username) currentBedId = 4
    }
    
    const testApplicationData = {
      username: userInfo.username,
      name: userInfo.name || '测试用户',
      currentRoomId: currentRoomId,
      currentBedId: currentBedId,
      towardsRoomId: currentRoomId + 1, // 目标房间号比当前房间号大1
      towardsBedId: currentBedId === 4 ? 1 : currentBedId + 1, // 目标床位号
      state: '未处理',
      applyTime: new Date().toISOString().slice(0, 19).replace('T', ' ')
    }
    
    console.log('提交的测试数据:', testApplicationData)
    
    const res = await request.post('/adjustRoom/add', testApplicationData)
    
    if (res.code === "0") {
      testResult.value = `测试数据创建成功: ${JSON.stringify(res, null, 2)}`
      ElMessage.success('测试数据创建成功')
      // 刷新申请记录
      getMyApplications()
      getAllApplications()
    } else {
      testResult.value = `测试数据创建失败: ${res.msg}`
      ElMessage.error(res.msg || '测试数据创建失败')
    }
  } catch (error) {
    testResult.value = `测试数据创建异常: ${error.message}`
    ElMessage.error('测试数据创建异常')
  } finally {
    creatingTestData.value = false
  }
}

const createSimpleTestData = async () => {
  if (!userInfo.username) {
    ElMessage.error('请先登录')
    return
  }
  
  try {
    creatingTestData.value = true
    
    console.log('=== 测试页面创建简单测试数据调试信息 ===')
    console.log('创建参数:')
    console.log('  用户学号:', userInfo.username)
    
    // 使用后端提供的简单测试数据创建接口
    const res = await request.post('/adjustRoom/createTestData', null, {
      params: {
        username: userInfo.username
      }
    })
    
    console.log('创建响应:')
    console.log('  完整响应:', res)
    console.log('  响应code:', res.code)
    console.log('  响应msg:', res.msg)
    console.log('=== 测试页面创建调试信息结束 ===')
    
    if (res.code === "0") {
      testResult.value = `简单测试数据创建成功: ${JSON.stringify(res, null, 2)}`
      ElMessage.success('简单测试数据创建成功')
      // 刷新申请记录
      getMyApplications()
      getAllApplications()
    } else {
      testResult.value = `简单测试数据创建失败: ${res.msg}`
      ElMessage.error(res.msg || '简单测试数据创建失败')
    }
  } catch (error) {
    testResult.value = `简单测试数据创建异常: ${error.message}`
    ElMessage.error('简单测试数据创建异常')
  } finally {
    creatingTestData.value = false
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

.test-section h4 {
  color: #666;
}

pre {
  background-color: #f5f5f5;
  padding: 10px;
  border-radius: 3px;
  overflow-x: auto;
}
</style> 