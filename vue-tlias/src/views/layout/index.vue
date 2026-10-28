<script setup>
  import { ref,onMounted } from "vue"
  import { updateEmpPswApi } from "@/api/emp.js";
  import { ElMessage, ElMessageBox } from 'element-plus';
  import { useRouter } from 'vue-router'

  /* 发送请求 */
  // PUT 员工修改密码请求
  const empUpdatePsw = async () => {
    const result = await updateEmpPswApi(updatePwsForm.value)
    if(result.code){
      ElMessage.success("修改成功")
      dialogUpdatePws.value = false
    }else{
      ElMessage.error(result.msg)
    }
  }

  /* 对象实列 ==========*/
  let router = useRouter()

  const name = ref([]) // 顶部功能栏展示的员工姓名对象
  // 修改密码对象实列
  const dialogUpdatePws = ref(false) // 修改密码diolog展示对象
  const updatePwsFormRef = ref() // 修改密码from表单响应式对象
  const updatePwsForm = ref({ // 表单输入框实列对象
    empId: '',
    currentPassword: '',
    newPassword: '',
    cNewPassword: ''
  })
  const rules = ref({ // 表单校验规则
    currentPassword: [
      { required: true, message: '不能为空，请输入密码', trigger: 'blur' }
    ],
    newPassword: [
      { required: true, message: '请输入新密码', trigger: 'blur' },
      { min: 6, max: 20, message: '密码长度为6-20位', trigger: 'blur' }
    ],
    cNewPassword: [
      // 1. 必填校验单独写
      { required: true, message: '请再次输入新密码', trigger: 'blur' },
    ]
  })

  /* 修改密码函数实现 ==========*/
  // 点击提交按钮校验并提交请求
  const submitUpdateForm = async (formEl) => {
    if (!formEl) return
    await formEl.validate((valid, fields) => {
      if(updatePwsForm.value.newPassword !== updatePwsForm.value.cNewPassword){
        ElMessage.error("两次输入的新密码不一致,请重新确认")
        return
      }

      if (valid) { 
        console.log("表单验证成功,提交请求");
        empUpdatePsw()
      } else {
        ElMessage.error("表单数据错误")
      }
    })
  }

  // 取消按钮消除校验提示
  const resetUpdateForm = (formEl) => {
    if (!formEl) return
    formEl.resetFields()
    dialogUpdatePws.value = false
  }

  /* 退出登录函数实现 ==========*/
  const logout = () => {
    //弹出确认框, 如果确认, 则退出登录, 跳转到登录页面
    ElMessageBox.confirm('确认退出登录吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {//确认, 则清空登录信息
      ElMessage.success('退出登录成功')
      localStorage.removeItem('loginUser')
      router.push('/login')//跳转到登录页面
    })
  }

  onMounted(() => {
    const loginUser = JSON.parse(localStorage.getItem('loginUser'))
    if(loginUser){
      name.value = loginUser.name
      updatePwsForm.value.empId = loginUser.id
    }else{
      name.value = "未知"
    }
  })
</script>

<!-- 页面布局 =================================================================================================================== -->
<template>

  <div class="common-layout">

    <el-container>

      <!-- 头区域 -->
      <el-header class="modern-header">
        <div class="head-content">
          <!-- 左侧标题 -->
          <div class="header-left">
            <!-- 可以加个图标 -->
            <!-- <el-icon class="logo-icon"><Monitor /></el-icon> -->
            <span class="title">智能学习辅助系统</span>
          </div>

          <!-- 顶部右侧功能栏 -->
          <div class="header-right">

            <div>
              <el-button @click="dialogUpdatePws = true" type="text" class="nav-link">
                <el-icon><User /></el-icon> 修改密码
              </el-button>
            </div>
            
            <el-divider direction="vertical" />

            <div class="nav-link danger">
              <a href="javascript:void(0)" @click="logout">
                <el-icon><SwitchButton /></el-icon> 退出登录 【 {{ name }} 】
              </a>
            </div>

          </div>
        </div>
      </el-header>

      <el-container>

        <!-- 左侧菜单 -->
        <div class="left">

          <el-aside width="200px" router>
            <!-- 左侧菜单栏 -->
            <el-menu class="el-menu-vertical-demo" router>

              <!-- 首页菜单 -->
              <el-menu-item index="/">
                <span>首页</span>
              </el-menu-item>

              <!-- 班级学员管理菜单 -->
              <el-sub-menu index="/student-class">
                <template #title>
                  <span>班级学员管理</span>
                </template>
                  <el-menu-item index="/clazz">班级管理</el-menu-item>
                  <el-menu-item index="/stu">学员管理</el-menu-item>
              </el-sub-menu>

              <!-- 系统信息管理菜单 -->
              <el-sub-menu index="/system">
                <template #title>
                  <span>系统信息管理</span>
                </template>
                  <el-menu-item index="/dept">部门管理</el-menu-item>
                  <el-menu-item index="/emp">员工管理</el-menu-item>
              </el-sub-menu>

              <!-- 数据统计管理菜单 -->
              <el-sub-menu index="/reportMenu">
                <template #title>
                  <span>数据统计管理</span>
                </template>
                  <el-menu-item index="/empReport">员工信息统计</el-menu-item>
                  <el-menu-item index="/stuReport">学员信息统计</el-menu-item>
                  <el-menu-item index="/log">日志统计</el-menu-item>
              </el-sub-menu>

            </el-menu>
          </el-aside>

        </div>

        <!-- 主体区域 -->
   
          <el-main>
            <router-view></router-view>
          </el-main>
        
      </el-container>
    </el-container>
  </div>

  <!-- 修改密码 dialog 对话框 -->
  <el-dialog
    v-model="dialogUpdatePws"
    title="修改密码"
    width="500"
    :modal="false"
    modal-penetrable
    class="update-pwd-dialog"
  >
    <!-- 用户名标题居中 -->
    <div class="dialog-user-title"><el-icon><User /></el-icon>当前用户：{{ name }}</div>

    <el-form
      ref="updatePwsFormRef"
      :model="updatePwsForm"
      :rules="rules"
      label-width="auto"
      class="centered-form"
    >
      <el-form-item label="原密码" prop="currentPassword">
        <el-input
          v-model="updatePwsForm.currentPassword"
          type="password"
          placeholder="请输入原密码"
          show-password
        />
      </el-form-item>

      <el-form-item label="新密码" prop="newPassword">
        <el-input
          v-model="updatePwsForm.newPassword"
          type="password"
          placeholder="请输入新密码"
          show-password
        />
      </el-form-item>

      <el-form-item label="确认新密码" prop="cNewPassword">
        <el-input
          v-model="updatePwsForm.cNewPassword"
          type="password"
          placeholder="再次确认新密码"
          show-password
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="resetUpdateForm(updatePwsFormRef)">取消</el-button>
        <el-button type="primary" @click="submitUpdateForm(updatePwsFormRef)">提交</el-button>
      </div>
    </template>
  </el-dialog>

</template>

<style scoped>

  a {
    text-decoration: none;
  }

  .common-layout {
    border: 0;
  }

  /* .left {
    border: 1px solid #000;
  }*/

/* ------------------------------现代商务风-------------------------------- */
  /* 头部容器 */
  .modern-header {
    background: linear-gradient(to right, #2c3e50, #4ca1af); /* 稳重的蓝青渐变 */
    /* 或者使用纯色: background-color: #304156; */
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 增加一点阴影增加层次感 */
    padding: 0 20px;
    display: flex;
    align-items: center;
  }

  /* 内容布局 */
  .head-content {
    width: 100%;
    display: flex;
    justify-content: space-between; /* 左右两端对齐 */
    align-items: center;
  }

  /* 左侧标题 */
  .header-left {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .logo-icon {
    font-size: 28px;
    color: #fff;
  }

  .title {
    font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif; /* 使用系统默认无衬线字体 */
    font-weight: 600;
    font-size: 24px; /* 稍微调小一点，更精致 */
    color: #fff;
    letter-spacing: 1px; /* 字间距 */
    text-shadow: 0 2px 4px rgba(0,0,0,0.2); /* 文字阴影 */
  }

  /* 右侧工具 */
  .header-right {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .nav-link {
    color: rgba(255, 255, 255, 0.8);
    font-size: 14px;
    transition: all 0.3s;
  }

  .nav-link:hover {
    color: #fff;
    background-color: rgba(255, 255, 255, 0.1);
  }

  .danger:hover {
    color: #ff4d4f; /* 退出登录悬停变红 */
  }

/* dialog 修改密码对话框样式 ----------------------------*/
  /* 1. 用户名标题样式 */
  .dialog-user-title {
    text-align: center;
    font-size: 16px;
    color: #606266;
    margin-bottom: 20px;
  }

  /* 2. 表单整体居中 + 统一输入框宽度 */
  .centered-form {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  /* 强制所有表单项的输入框宽度一致，去掉内联 style="width:240px" */
  .centered-form :deep(.el-form-item__content) {
    width: 280px;
    flex-shrink: 0;
  }

  /* 3. 底部按钮居中 */
  .dialog-footer {
    display: flex;
    justify-content: center;
    gap: 12px;
  }

</style>