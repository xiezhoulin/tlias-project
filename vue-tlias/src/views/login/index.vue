<script setup>
import { ref } from 'vue'
//import { User, Lock } from '@element-plus/icons-vue' // 引入图标（如果没安装可去掉图标相关代码）:由于已经下载了全局图标组件，可以不用尾部导入，直接写图标标签就可生效
import { loginApi } from '@/api/login.js'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

// 登录用户数据响应式对象
let formInline = ref({
  username: '',
  password: ''
})

let router = useRouter()

// 登录按钮绑定
const loginSubmit = async () => {
  // 简单的非空校验
  if (!formInline.value.username || !formInline.value.password) {
    ElMessage.warning('请输入账号和密码')
    return
  }
  console.log('提交登录数据：', formInline.value)
  // 这里可以调用后端的登录接口
  const result = await loginApi(formInline.value)
  if(result.code){ //登录成功
    ElMessage.success('登录成功')
    localStorage.setItem('loginUser', JSON.stringify(result.data))
    router.push('/')// 跳转
  }else{ //登录失败
    ElMessage.error(result.msg)
  }
}


// 重置按钮绑定
const delSubmit = () => {
  // 修复：不要直接替换整个对象，而是清空属性，保持响应式
  formInline.value.username = ''
  formInline.value.password = ''
}
</script>

<template>
  <div class="home-container">
    <!-- 背景图层 -->
    <div class="background"></div>

    <!-- 页面内容层 -->
    <div class="content">
      <div class="login-box">
        <!-- 标题区域 -->
        <div class="login-header">
          <h2>xxx智能学习辅助系统</h2>
        </div>

        <!-- 表单区域 -->
        <el-form :model="formInline" class="login-form">
          <el-form-item>
            <el-icon :size="15" color="#409EFF"><User /></el-icon>
            <el-input 
              v-model="formInline.username" 
              placeholder="请输入账号" 
              size="large"
            />
          </el-form-item>

          <el-form-item>
            <el-icon :size="15" color="#409EFF"><Lock /></el-icon>
            <el-input 
              v-model="formInline.password" 
              placeholder="请输入密码" 
              type="password" 
              show-password 
              size="large"
              @keyup.enter="loginSubmit" 
            />
          </el-form-item>
          
          <el-form-item class="button-group">
            <el-button type="primary" size="large" @click="loginSubmit" class="login-btn">登 录</el-button>
            <el-button size="large" @click="delSubmit" class="reset-btn">重 置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 根容器 */
.home-container {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}

/* 背景图 */
.background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  /* 建议找一张偏暗或者模糊一点的背景图，更能凸显登录框 */
  background-image: url('@/assets/preview.png'); 
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  z-index: -1;
}

/* 内容居中容器 */
.content {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

/* 登录框主体 - 毛玻璃效果 */
.login-box {
  width: 400px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.15); /* 更透明的白色 */
  backdrop-filter: blur(10px); /* 核心：背景模糊 */
  -webkit-backdrop-filter: blur(10px);
  border-radius: 16px; /* 大圆角 */
  border: 1px solid rgba(255, 255, 255, 0.2); /* 淡淡的边框 */
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37); /* 柔和的阴影 */
}

/* 标题样式 */
.login-header {
  text-align: center;
  margin-bottom: 30px;
}
.login-header h2 {
  margin: 0;
  font-size: 24px;
  color: #fff; /* 标题改为白色，在深色背景上更清晰 */
  letter-spacing: 2px;
  text-shadow: 0 2px 4px rgba(0,0,0,0.3);
}
.login-header p {
  margin: 10px 0 0;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

/* 表单微调 */
.login-form :deep(.el-input__wrapper) {
  background-color: rgba(255, 255, 255, 0.9); /* 输入框背景稍微不透明一点 */
  box-shadow: none; /* 去掉默认阴影 */
  border-radius: 8px;
}

.button-group {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  gap: 15px;
}

.login-btn {
  flex: 1;
  font-weight: bold;
  letter-spacing: 1px;
}

.reset-btn {
  flex: 1;
}
</style>