<script setup>
  import { ref,onMounted } from 'vue'
  import { queryEmpApi } from "@/api/emp.js";
  import { ElMessage } from "element-plus";

  /* 请求处理 */
  const queryEmp = async () => {
    let loginUser = JSON.parse(localStorage.getItem('loginUser'))
    if(loginUser && loginUser.token){
      let empId = parseInt(loginUser.id)
      const result = await queryEmpApi(empId)
      if(result.code){
        emp.value = result.data
      }else{
        ElMessage.error(result.msg)
      }
    }
  }

  /* 对象实列 */
  // 员工信息对象
    // 1. 定义映射常量（集中管理，方便后续修改或国际化）
  const GENDER_MAP = {
    1: '男',
    2: '女'
  }

  const JOB_MAP = {
    1: '班主任',
    2: '讲师',
    3: '学工主管',
    4: '教研主管',
    5: '咨询师'
  }

  // 2. 封装格式化函数（带默认值兜底，防止数据异常时页面空白）
  const formatGender = (val) => GENDER_MAP[val] ?? '未知'
  const formatJob = (val) => JOB_MAP[val] ?? '未知'

  const emp = ref({
    username: "",
    gender: "",
    phone: "",
    job: "",
    deptName: "",
    image: "",
  })

  // 轮播图对象
  const bannres = [
    { url: "https://webstatic.mihoyo.com/upload/op-public/2021/10/09/6c0e9aaa5b9471e32316d11284edb710_2698450106633206665.png", alt: "铁"},
    { url: "https://webstatic.mihoyo.com/upload/op-public/2021/10/03/663fe95dff54044d7cd5d3a355f61bf7_6089937633900261947.png", alt: "崩"},
    { url: "https://webstatic.mihoyo.com/upload/op-public/2021/10/03/58574b8c47e058787cc24049413b7cea_7872872512863208275.png", alt: "原"},
    { url: "https://fastcdn.mihoyo.com/content-v2/plat/102125/ef9e5ea1dac6648dd3377f20ff83aae3_2125539924897349537.png", alt: "绝"}
  ]

onMounted(() => {
  queryEmp()
})
</script>

<template>
  <h1 style="color: cornflowerblue;">|&nbsp;&nbsp;欢迎来到首页</h1>

  <div class="index">
    <!-- 个人信息展示 -->
     <div style=" width: 600px;">
        <el-card shadow="always" style="height: 600px;">
          <div style="margin: 0px 30%;">
            <el-avatar :size="150" :src="emp.image" @error="errorHandler">
              <!-- 图片加载失败访问的图片路径 -->
              <img 
                src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
              />
            </el-avatar>  
            <el-divider border-style="dashed" />     
            <div class="empShow"><b>用户名:</b>{{ emp.username }}</div>
            <el-divider border-style="dashed" />
            <div class="empShow"><b>性别:</b>{{ formatGender(emp.gender) }}</div>
            <el-divider border-style="dashed" />
            <div class="empShow"><b>手机:</b>{{ emp.phone }}</div>
            <el-divider border-style="dashed" />
            <div class="empShow"><b>职位:</b>{{ formatJob(emp.job) }}</div>
            <el-divider border-style="dashed" />
            <div class="empShow"><b>部门:</b>{{ emp.deptName }}</div>
          </div>
        </el-card>
     </div>
    
    <!-- 轮播图 -->
    <div style=" margin: auto 10px; width: 100%; ">
      <el-carousel trigger="click" height="600px" >
        <el-carousel-item v-for=" (item,index) in bannres " :key="index">
          <img :src="item.url" alt="" style="width: 100%; height: 100%; object-fit: cover;">
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
  
</template>

<style scoped>

  .index {
    display: flex;
  }
  .emoShow {
    margin: 0px 10px;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n + 1) {
    background-color: #d3dce6;
  }

</style>
