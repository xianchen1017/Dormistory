<template>
  <el-menu
      :default-active="this.path"
      router
      style="width: 200px; height:100%; min-height: calc(100vh - 40px)"
      unique-opened
  >
    <div style="display: flex;align-items: center;justify-content: center;padding: 11px 0;">
      <img alt="" src="@/assets/logo.png" style="width: 60px;">
    </div>
    <el-menu-item index="/home">
      <el-icon>
        <house/>
      </el-icon>
      <span>首页</span>
    </el-menu-item>
    <!-- 用户管理：教师和管理员可见，教师只显示学生信息 -->
    <el-sub-menu v-if="this.judgeIdentity()===1 || this.judgeIdentity()===2" index="2">
      <template #title>
        <el-icon>
          <user/>
        </el-icon>
        <span>用户管理</span>
      </template>
      <el-menu-item v-if="this.judgeIdentity()===1 || this.judgeIdentity()===2" index="/stuInfo">学生信息</el-menu-item>
      <el-menu-item v-if="this.judgeIdentity()===2" index="/dormManagerInfo">宿管信息</el-menu-item>
    </el-sub-menu>
    <!-- 宿舍管理：教师和管理员可见 -->
    <el-sub-menu v-if="this.judgeIdentity()===1 || this.judgeIdentity()===2" index="3">
      <template #title>
        <el-icon>
          <coin/>
        </el-icon>
        <span>宿舍管理</span>
      </template>
      <el-menu-item v-if="this.judgeIdentity()===1 || this.judgeIdentity()===2" index="/buildingInfo">楼宇信息</el-menu-item>
      <el-menu-item v-if="this.judgeIdentity()===1 || this.judgeIdentity()===2" index="/roomInfo">房间信息</el-menu-item>
      <el-menu-item v-if="this.judgeIdentity()===1 || this.judgeIdentity()===2" index="/comeBackLateInfo">晚归记录</el-menu-item>
    </el-sub-menu>
    <!-- 信息管理：只有管理员可见 -->
    <el-sub-menu v-if="this.judgeIdentity()===2" index="4">
      <template #title>
        <el-icon>
          <message/>
        </el-icon>
        <span>信息管理</span>
      </template>
      <el-menu-item v-if="this.judgeIdentity()===2" index="/noticeInfo">公告信息</el-menu-item>
      <el-menu-item v-if="this.judgeIdentity()===2" index="/repairInfo">报修信息</el-menu-item>
    </el-sub-menu>
    <!-- 申请管理：教师和管理员可见 -->
    <el-sub-menu v-if="this.judgeIdentity()===1 || this.judgeIdentity()===2" index="5">
      <template #title>
        <el-icon>
          <pie-chart/>
        </el-icon>
        <span>申请管理</span>
      </template>
      <el-menu-item v-if="this.judgeIdentity()===1 || this.judgeIdentity()===2" index="/adjustRoomInfo">调宿申请</el-menu-item>
    </el-sub-menu>
    <!-- 访客管理：只有管理员可见 -->
    <el-menu-item v-if="this.judgeIdentity()===2" index="/visitorInfo">
      <svg class="icon" data-v-042ca774="" style="height: 18px; margin-right: 11px;"
           viewBox="0 0 1024 1024"
           xmlns="http://www.w3.org/2000/svg">
        <path
            d="M512 160c320 0 512 352 512 352S832 864 512 864 0 512 0 512s192-352 512-352zm0 64c-225.28 0-384.128 208.064-436.8 288 52.608 79.872 211.456 288 436.8 288 225.28 0 384.128-208.064 436.8-288-52.608-79.872-211.456-288-436.8-288zm0 64a224 224 0 110 448 224 224 0 010-448zm0 64a160.192 160.192 0 00-160 160c0 88.192 71.744 160 160 160s160-71.808 160-160-71.744-160-160-160z"
            fill="currentColor"></path>
      </svg>
      <span>访客管理</span>
    </el-menu-item>
    <!-- 学生专属菜单项 -->
    <el-menu-item v-if="this.judgeIdentity()===0" index="/myRoomInfo">
      <el-icon>
        <school/>
      </el-icon>
      <span>我的宿舍</span>
    </el-menu-item>
    <el-menu-item v-if="this.judgeIdentity()===0" index="/applyChangeRoom">
      <el-icon>
        <takeaway-box/>
      </el-icon>
      <span>申请调宿</span>
    </el-menu-item>
    <el-menu-item v-if="this.judgeIdentity()===0" index="/applyRepairInfo">
      <el-icon>
        <set-up/>
      </el-icon>
      <span>报修申请</span>
    </el-menu-item>
    <!-- 管理员专属菜单项 -->
    <el-menu-item v-if="this.judgeIdentity() === 2" index="/report">
      <el-icon>
        <pie-chart/>
      </el-icon>
      <span>入住报表</span>
    </el-menu-item>
    <!-- 个人信息所有人可见 -->
    <el-menu-item index="/selfInfo">
      <el-icon>
        <setting/>
      </el-icon>
      <span>个人信息</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
import request from "@/utils/request";
import {ElMessage} from "element-plus";

export default {
  name: "Aside",
  data() {
    return {
      user: {},
      identity: '',
      path: this.$route.path
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      try {
        // 首先尝试从sessionStorage获取身份信息
        const storedIdentity = sessionStorage.getItem('identity');
        const storedUser = sessionStorage.getItem('user');
        
        if (storedIdentity && storedUser) {
          this.identity = storedIdentity;
          this.user = JSON.parse(storedUser);
        }
        
        // 然后尝试从后端验证会话
        Promise.all([
          request.get("/main/loadIdentity"),
          request.get("/main/loadUserInfo")
        ]).then(([identityRes, userRes]) => {
          if (identityRes.code === "0" && userRes.code === "0") {
            this.identity = identityRes.data;
            this.user = userRes.data;
            sessionStorage.setItem("identity", identityRes.data);
            sessionStorage.setItem("user", JSON.stringify(userRes.data));
          } else {
            // 如果后端验证失败，但sessionStorage中有信息，继续使用
            if (!storedIdentity) {
              throw new Error("会话验证失败");
            }
          }
        }).catch(error => {
          // 只有在sessionStorage中也没有信息时才跳转到登录页
          if (!storedIdentity) {
            ElMessage.error("会话过期，请重新登录");
            sessionStorage.clear();
            this.$router.push("/login");
          } else {
            console.warn("后端会话验证失败，使用本地存储的信息");
          }
        });
      } catch (error) {
        ElMessage.error("初始化异常");
        console.error(error);
      }
    },
    judgeIdentity() {
      console.log("当前身份:", this.identity); // 添加调试信息
      if (this.identity === 'stu') {
        return 0
      } else if (this.identity === 'dormManager') {
        return 1
      } else if (this.identity === 'admin') {
        return 2
      } else {
        console.warn("未知身份:", this.identity);
        return 0 // 默认为学生身份
      }
    }
  }
}
</script>

<style scoped>
.icon {
  margin-right: 6px;
}

.el-sub-menu .el-menu-item {
  height: 50px;
  line-height: 50px;
  padding: 0 45px;
  min-width: 199px;
}
</style>