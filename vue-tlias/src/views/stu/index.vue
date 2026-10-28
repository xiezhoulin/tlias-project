<!-- ========================================== HTML ============================================================================================ -->
<template>
  <h3 style="color: cornflowerblue;">|&nbsp;&nbsp;学员管理</h3>

  <!-- 搜索栏布局 ==========-->
  <el-form :inline="true" :model="FromStudent">

    <el-form-item label="姓名">
      <el-input v-model="FromStudent.name" placeholder="请输入姓名" clearable />
    </el-form-item>

    <el-form-item label="最高学历">
      <el-select
        v-model="FromStudent.degree"
        placeholder="请选择"
        clearable
      >
        <el-option v-for=" (d,index) in degrees" :key="index" :label="d.name" :value="d.value" />
      </el-select>
    </el-form-item>

    <el-form-item label="所属班级">
      <el-select
        v-model="FromStudent.clazzId"
        placeholder="请选择"
        clearable
      >
        <el-option v-for=" clazz in clazzs" :key="clazz.id" :label="clazz.name" :value="clazz.id" />
      </el-select>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="onSubmit">查询</el-button>
      <el-button type="info" @click="clear()">清空</el-button>
    </el-form-item>
  </el-form>

  <!-- 表格展示通用操作按钮 ==========-->
  <div>
    <el-button type="success" @click="addStudentButton()">+&nbsp;新增学员</el-button>
    <el-button type="danger" @click="delStudent(selectedIds)">-&nbsp;批量删除</el-button>
    <br><br>
  </div>

  <!-- 表格数据展示 ==========-->
  <div>
    <el-table :data="students" border style="width: 100%" @selection-change="handleSelectionChange"> <!-- 顶行复选框 -->
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="name" label="姓名" width="120" align="center" />
      <el-table-column prop="no" label="学号" width="120" align="center" />
      <el-table-column prop="clazzName" label="班级" width="140" align="center" />
      <el-table-column label="性别" width="80" align="center">
        <template #default="scope" >
          {{ scope.row.gender == 1 ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="120" align="center" />
      <el-table-column label="最高学历" width="100" align="center">
        <template #default="scope" >
          <span v-if="scope.row.degree == 1">初中</span>
          <span v-else-if="scope.row.degree == 2">高中</span>
          <span v-else-if="scope.row.degree == 3">大专</span>
          <span v-else-if="scope.row.degree == 4">本科</span>
          <span v-else-if="scope.row.degree == 5">硕士</span>
          <span v-else style="color: red;">暂无</span>
        </template>
      </el-table-column>
      <el-table-column prop="violationCount" label="违纪次数" width="90" align="center" />
      <el-table-column prop="violationScore" label="违纪扣分" width="90" align="center" />
      <el-table-column prop="updateTime" label="最后修改时间" width="210" align="center" />

      <el-table-column label="操作" fixed="right" align="center">
        <template #default="scope">
          <el-button size="small" type="primary" @click="studentEditButton(scope.row.id)">编辑</el-button>
          <el-button size="small" type="warning" @click="violationButton(scope.row.id)">违纪</el-button>
          <el-button size="small" type="danger" @click="delStudent(selectedIds.value = scope.row.id)">删除</el-button>
        </template>
      </el-table-column>

    </el-table>
  </div>
  
  <!-- 分页栏 ==========-->
  <div style="margin-top: 10px;">
    <el-pagination
      :currentPage
      v-model="pageSize"
      :page-sizes="[5, 10, 20, 30]"
      layout="total, sizes, prev, pager, next, jumper"
      :total
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>  

  <!-- 新增员工及修改员工dialog对话框 ==========-->
  <el-dialog
    v-model="dialogVisible"
    :title="dialogVisibleTitle"
    width="700"
    class="dialog"
    align-center
    draggable
  >
    <!-- form表单 -->
    <el-form 
      ref="ruleFormRef"
      :rules="rules"
      :model="dialogStudent"
      label-width="auto"
    >
      <!-- 第一行 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="dialogStudent.name" placeholder="请输入学员姓名" clearable/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="学号" prop="no">
            <el-input v-model="dialogStudent.no" placeholder="请输入学员学号" clearable/>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 第二行 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="性别" prop="gender">
              <el-select
                v-model="dialogStudent.gender"
                style="width: 240px"
                clearable
                placeholder="请选择"
              >
                <el-option label="男" :value="1"/>
                <el-option label="女" :value="2"/>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="dialogStudent.phone" placeholder="请输入学员学号" maxlength="11" clearable/>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 第三行 -->
      <el-row :gutter="20">

        <el-col :span="12">
          <el-form-item label="身份证号" prop="idCard">
            <el-input v-model="dialogStudent.idCard" placeholder="请输入身份证号" clearable/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="是否于院校" prop="isCollege">
              <el-select
                v-model="dialogStudent.isCollege"
                style="width: 240px"
                clearable
                placeholder="请选择"
              >
                <el-option label="是" :value="1"/>
                <el-option label="否" :value="0"/>
            </el-select>
          </el-form-item>
        </el-col>

      </el-row>

      <!-- 第四行 -->
      <el-row :gutter="20">

        <el-col :span="12">
          <el-form-item label="联系地址">
            <el-input v-model="dialogStudent.address" placeholder="请输入联系地址" clearable/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="做高学历">
              <el-select
                v-model="dialogStudent.degree"
                style="width: 240px"
                clearable
                placeholder="请选择"
              >
                <el-option v-for=" (d,index) in degrees" :key="index" :label="d.name" :value="d.value" />
            </el-select>
          </el-form-item>
        </el-col>

      </el-row>

      <!-- 第五行 -->
      <el-row :gutter="20">

        <el-col :span="12">
          <el-form-item label="毕业时间">
            <el-date-picker
              v-model="dialogStudent.graduationDate"
              type="date"
              placeholder="请选择毕业时间"
              style="width: 240px"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="所属班级">
              <el-select
                v-model="dialogStudent.clazzId"
                style="width: 240px"
                clearable
                placeholder="请选择"
              >
                <el-option v-for=" clazz in clazzs" :key="clazz.id" :label="clazz.name" :value="clazz.id" />
            </el-select>
          </el-form-item>
        </el-col>

      </el-row>

    </el-form>

    <!-- 操作按钮 -->
    <template #footer>
        <div>
          <el-button @click="dialogCancel()">取消</el-button>
          <el-button type="primary" @click="submitButton()">提交</el-button>
        </div>
      </template>
    
  </el-dialog>

  <!-- 违纪处理dialog对话框 ==========-->

  <el-dialog v-model="violationDialogVisible" title="提示" width="500" align-center>
    <span>请输入违纪扣分</span>
    <p>学员ID:{{ studentId }}</p>
    <p>扣除分数:{{ violation }}</p>
    <el-form :model="violation">
      <el-input-number v-model="violation" :min="1" :max="100" @change="handleChange" size="large"/>
    </el-form>

    <template #footer>
        <el-button @click="violationDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="violationStudent()">
          确认
        </el-button>
    </template>
  </el-dialog>

</template>

<!-- ========================================== js ============================================================================================ -->
<script setup>
import { ref,onMounted } from 'vue'
import { queryAllClazzsApi } from "@/api/clazz.js";
import { conditionalPageQueryApi,addStudentApi,getByIdQueryApi,updateStudentApi,delStudentApi,violationApi } from "@/api/student.js";
import { ElMessage,ElMessageBox } from "element-plus";

/* 钩子函数 ========== */
onMounted(() => {
  queryAllClazz()
  pageQueryStudents()
})

/* 发送请求函数 ========== */

//GET请求：查询所有班级 =
async function queryAllClazz(){
  const result = await queryAllClazzsApi()
  if(result.code){
    clazzs.value = result.data
  }
}

//GET请求：根据条件查询学员 =
const pageQueryStudents = async () => {
  const result = await conditionalPageQueryApi(FromStudent.value.name,FromStudent.value.degree,FromStudent.value.clazzId,currentPage.value,pageSize.value)
  if(result.code){
    //提交成功后展示数据
    students.value = result.data.rows
    total.value = result.data.total
  }
}

//POST请求：添加学员信息 =
const addStudent = async () => {
  const result = await addStudentApi(dialogStudent.value)
  if(result.code){
    dialogVisible.value = false
    ElMessage.success("添加成功！")
  }else{
    ElMessage.error(result.msg)
  }
}

//GET请求：根据ID查询学员信息 =
const getByIdQuery = async (id) => {
  const result = await getByIdQueryApi(id)
  if(result.code){
    dialogStudent.value = result.data
  }else{
    ElMessage.error(result.msg)
  }
}

//PUT请求：修改学员信息 =
const updateStudent = async () => {
  const result = await updateStudentApi(dialogStudent.value)
  if(result.code){
    dialogVisible.value = false
    ElMessage.success("修改成功")
  }else{
    ElMessage.error(result.msg)
  }
}

//DELETE请求：删除学员信息
const delStudent = (id) => {
   ElMessageBox.confirm('你要删除此数据吗？','提示',{confirmButtonText: '确定',cancelButtonText: '取消',type: 'warning',})
        .then( async () => {
          const result = await delStudentApi(id)
          if(result.code){
            console.log("执行删除");
            ElMessage.success("删除成功")
            pageQueryStudents()
          }else{
            ElMessage.error(result.msg)
          }
        })
        .catch(() => {
          ElMessage.info("已取消")
        })
}

//PUT请求：违纪学员信息 =
const violationStudent = async () => {
  const result = await violationApi(studentId,violation.value)
  if(result.code){
    violationDialogVisible.value = false
    ElMessage.success("记录违纪成功")
    pageQueryStudents()
  }else{
    ElMessage.error(result.msg)
  }
}

//搜索栏绑定的对象及函数 ==========
//搜索栏响应式对象

const degrees = ref([{'name' : '初中','value' : 1},{'name' : '高中','value' : 2},{'name' : '大专','value' : 3},{'name' : '本科','value' : 4},{'name' : '硕士','value' : 5}])
const clazzs = ref([])

const FromStudent = ref({
  name: '',
  degree: '',
  clazzId: ''
})

//搜索栏提交按钮函数
const onSubmit = () => {
  pageQueryStudents()
}

//搜索栏清空按钮函数
function clear(){
  FromStudent.value = ref({
    user: '',
    degree: '',
    clazzId: ''
  })
}

///页面数据展示绑定的对象及函数 ==========

//表格展示的学生列表响应式对象
const students = ref([
  /* {
    "id": 3,
    "name": "Lily",
    "no": "2023001003",
    "gender": 2,
    "phone": "13309230912",
    "degree": 4,
    "idCard": "110090110090110090",
    "isCollege": 0,
    "address": "回龙观东大街110号",
    "graduationDate": "2020-07-01",
    "violationCount": 2,
    "violationScore": 5,
    "clazzId": 1,
    "createTime": "2023-06-01T18:35:23",
    "updateTime": "2023-06-01T19:37:42",
    "clazzName": "黄埔班一期"
  } */
])

//复选框绑定事件
  //用来存放多个id
  const selectedIds = ref([])
  //复选框勾选发生变化时触发 - selection: 当前选中的记录 (数组)
  const handleSelectionChange = (selection) => {
    selectedIds.value = selection.map( item => item.id )
  }

//分页栏对象绑定及函数 ==========
  /* 分页数据 */
  const currentPage = ref(1) //页码
  const pageSize = ref(10)   //每页展示数据
  const total = ref(0)       //总数据

  // 分页处理
  const handleSizeChange = (val) => {
    console.log(`用户切换了：每页 ${val} 条`)
    pageSize.value = val
    currentPage.value = 1 // 【重要】切换条数后，通常重置回第一页
    pageQueryStudents()
  }
  const handleCurrentChange = (val) => {
    console.log(`用户点击了：第 ${val} 页`)
    currentPage.value = val
    pageQueryStudents()
  }

//新增员工及修改员工dialog对话框对象及函数 ==========
const dialogVisible = ref(false)
let dialogVisibleTitle = ref('')

//dialog数据响应式对象
const dialogStudent = ref({
  "name": "",
  "no": "",
  "gender": '',
  "phone": "",
  "idCard": "",
  "isCollege": '',
  "address": "",
  "degree": '',
  "graduationDate": null,
  "clazzId": ''
})

//dialog提交按钮函数
async function submitButton(){
  if (!ruleFormRef.value) return
  await ruleFormRef.value.validate((valid, fields) => {
    if (valid) {
      console.log('表单数据验证通过')
      if(dialogStudent.value.id != null){
        //修改请求
        delete dialogStudent.value.createTime
        delete dialogStudent.value.updateTime
        updateStudent()
      }else{
        //添加请求
        addStudent()
      }
      pageQueryStudents()
    } else {
      console.log('验证失败', fields)
    }
  })
}
//dialog取消按钮函数
function dialogCancel(){
  if (!ruleFormRef.value) return
  ruleFormRef.value.resetFields()
  dialogVisible.value = false
}

//表单校验对象及校验规则
const ruleFormRef = ref()
const rules = ref({
  name: [
    { required: true, message: '必填', trigger: 'blur' },
    { min: 2, max: 10, message: '姓名长度须在2-10字之间', trigger: 'blur' }
  ],
  no: [
    { required: true, message: '必填', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '必填', trigger: 'change' }
  ],
  phone: [
    { required: true, message: '必填', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的11位手机号码', trigger: 'blur' }
  ],
  idCard: [
    { required: true, message: '必填', trigger: 'blur' },
    { pattern: /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2]\d|3[0-1])\d{3}[0-9Xx]$/, message: '请输入正确的身份证号码', trigger: 'blur' }
  ],
  isCollege: [
    { required: true, message: '必填', trigger: 'change' }
  ]
})

/* 违纪处理dialog对话框对象及函数 ========== */
const violationDialogVisible = ref(false)

//违纪分数
let studentId = ref()
const violation = ref(1)

const handleChange = (value) => {
  console.log(value)
}

//违纪处理按钮
function violationButton(id){
  studentId = id
  violation.value = 1
  violationDialogVisible.value = true
}

/* 通用组件函数 ========== */
//新增学员按钮函数
function addStudentButton(){
  dialogStudent.value = { "name": "","no": "","gender": "","phone": "", "idCard": "","isCollege": "","address": "","degree": '',"graduationDate": null, "clazzId": ''}
  dialogVisible.value = true
  ruleFormRef.value.resetFields()
  dialogVisibleTitle.value = '新增学员'
}

//编辑学员按钮函数
const studentEditButton = (id) => {
  dialogVisible.value = true
  dialogVisibleTitle.value = '修改学员'
  getByIdQuery(id)
}

</script>

<!-- ========================================== CSS ============================================================================================ -->
<style scoped>
  .dialog .el-input {
    --el-input-width: 240px;
  }
</style>
