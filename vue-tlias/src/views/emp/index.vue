<!-- ==========================================JavaScript============================================================================================ -->
<script setup>
import { ref,watch,onMounted } from "vue";
import { queryAllEmpApi,queryConditionEmpApi,addEmpApi,queryInfoApi,updateEmpApi,delEmpApi } from '@/api/emp.js'
import { ElMessage, ElMessageBox } from 'element-plus'

  /* 表单搜索栏绑定响应式对象 */
  let searchEmp = ref({
     name: '',
     gender: '',
     date: [],
     begin: '',
     end: ''
  })

  /* 搜索栏查询按钮逻辑 */
  const search = async () => {
    const hasCondition = searchEmp.value.name || searchEmp.value.gender || searchEmp.value.begin
    let result
    if(hasCondition){
      result = await queryConditionEmpApi(searchEmp.value.name,searchEmp.value.gender,searchEmp.value.begin,searchEmp.value.end,currentPage.value,pageSize.value)
    }else{
      result = await queryAllEmpApi(currentPage.value,pageSize.value)
    }
    if(result.code){
      empList.value = result.data.rows
      total.value = result.data.total
    }else{
      ElMessage.error(result.msg)
    }
  }
  
  /* 搜索栏清空按钮逻辑 */
  const clear = () => {
    searchEmp.value = {
      name: '',
      gender: '',
      date: [],
      begin: '',
      end: ''
    }
  }

  /* 表单侦听对象属性 */
  watch(() => searchEmp.value.date, (newval,oldval) =>{
    /* console.log(`值为：${newval},oldval:${oldval}`); */
    let date = searchEmp.value.date
    if(!newval || newval.length === 0){
      searchEmp.value.begin = ''
      searchEmp.value.end = ''
    }else{
      searchEmp.value.begin = date[0]
      searchEmp.value.end = date[1]
    }
  })

  /* 员工表格数据展示响应式对象 */
  /* 
        "id": 1,
        "username": "jinyong",
        "password": "123456",
        "name": "金庸",
        "gender": 1,
        "image": "https://web-framework.oss-cn-hangzhou.aliyuncs.com/2022-09-02-00-27-53B.jpg",
        "job": 2,
        "salary": 8000,
        "entryDate": "2015-01-01",
        "deptId": 2,
        "deptName": "教研部",
        "createTime": "2022-09-01T23:06:30",
        "updateTime": "2022-09-02T00:29:04"
  */
  const empList = ref([])

  /* 分页数据 */
  const currentPage = ref(1) //页码
  const pageSize = ref(10)   //每页展示数据
  const total = ref(0)       //总数据

  // 分页处理
  const handleSizeChange = (val) => {
    console.log(`用户切换了：每页 ${val} 条`)
    pageSize.value = val
    currentPage.value = 1 // 【重要】切换条数后，通常重置回第一页
    search()
  }
  const handleCurrentChange = (val) => {
    console.log(`用户点击了：第 ${val} 页`)
    currentPage.value = val
    search()  
  }

  /* Dialog 对话框交互代码 */
  const dialogVisible = ref(false)
  const disabled = ref(false) //显示表单的禁用功能

  //新增or修改表单
  const employeeFormRef = ref(null) //表单验证绑定对象
  const employee = ref({ //表单响应式数据
    username: '',
    name: '',
    gender: '',
    phone: '',
    job: '',
    salary: '',
    deptId: '',
    entryDate: '',
    image: '',
    exprList: []
})

  //职位列表数据
  const jobs = ref([{'name': '班主任','value':1},{'name': '讲师','value':2},{'name': '学工主管','value':3},{'name': '教研主管','value':4},{'name': '咨询师','value':5}])
  const genders = ref([{'name': '男','value':1},{'name': '女','value': 2}])
  //部门列表数据
  const depts = ref([{'name': '研发部','value':1},{'name':'市场部','value':2}])
  //经历列表数据
  const exprList = ref([])


//新增员工弹窗标题响应式对象------------------------------
  const dialogTitle = ref('')
  const addEmp = () => { //新增员工按钮
    dialogVisible.value = true
    dialogTitle.value = '新增员工'
  }

//新增员工or修改员工提交保存按钮
  const onSubmit = async () => { 
    employeeFormRef.value.validate(async valid => { //判断表单选项合法 valid 合法则返回true
        if(valid){ // 校验通过
            let result = null;
            //校验过后判断是新增还是修改来发送异步请求到服务器
            if(employee.value.id){
              delete employee.value.createTime
              delete employee.value.updateTime
              result = await updateEmpApi(employee.value)
              if(result.code){ //数据保存成功，关闭对话框，重新加载列表数据。
                ElMessage.success('修改员工成功')
                dialogVisible.value = false
                search()
              }else{ //失败，提示错误信息
                ElMessage.error(result.msg)
              }
            }else{
              result = await addEmpApi(employee.value)
              if(result.code){ //数据保存成功，关闭对话框，重新加载列表数据。
                ElMessage.success('新增员工成功')
                dialogVisible.value = false
                search()
              }else{ //失败，提示错误信息
                ElMessage.error(result.msg)
              }
            }
          }
      })
  }

  const handleClose = (done) => { //新增员工关闭按钮
    dialogVisible.value = false
    employee.value = {
      username: '',
      name: '',
      gender: '',
      phone: '',
      job: '',
      salary: '',
      deptId: '',
      entryDate: '',
      image: '',
      exprList: []
    }
    if(employeeFormRef.value){
      employeeFormRef.value.resetFields() //清空表单验证的警告信息
  }
  }

//编辑按钮
  const edit = async (id) => {
    disabled.value = true
    dialogVisible.value = true
    dialogTitle.value = '编辑员工'
    const result = await queryInfoApi(id)
    if(result.code){
      employee.value = result.data

      //对工作经历进行处理
      let exprList = employee.value.exprList;
      if(exprList && exprList.length > 0){
        exprList.forEach( (expr) => {
          expr.exprDate = [expr.begin, expr.end];
        })
      }
    }else{
      ElMessage.error(result.msg)
    }
  }

//点击删除单个按钮触发的事件
  const deleteEmp = (id) => {
    //弹出确认框
    ElMessageBox.confirm('您确认删除该员工吗?','提示',
      { confirmButtonText: '确认',cancelButtonText: '取消',type: 'warning'}
    ).then(async () => { //确认
      const result = await delEmpApi(id);
      if(result.code){
        ElMessage.success('删除成功');
        search();
      }else{
        ElMessage.error(result.msg);
      }
    }).catch(() => { 
      //取消
    })
  }

//批量删除
  const deleteByIds = () => {
    //弹出确认框
    ElMessageBox.confirm('您确认删除该员工吗?','提示',
      { confirmButtonText: '确认',cancelButtonText: '取消',type: 'warning'}
    ).then(async () => { //确认
      if(selectedIds.value && selectedIds.value.length > 0){
        const result = await delEmpApi(selectedIds.value);
        if(result.code){
          ElMessage.success('删除成功');
          search();
        }else{
          ElMessage.error(result.msg);
        }
      }else {
        ElMessage.info('您没有选择任何要删除的数据');
      }
    }).catch(() => { //取消
      ElMessage.info('您已取消删除');
    })
  }

//复选框绑定事件
  //用来存放多个id
  const selectedIds = ref([])
  //复选框勾选发生变化时触发 - selection: 当前选中的记录 (数组)
  const handleSelectionChange = (selection) => {
    selectedIds.value = selection.map( item => item.id )
  }

//表单校验规则
// 验证规则
  const rules = ref({
    username: [
      { required: true, message: '请输入用户名', trigger: 'blur' },
      { min: 2, max: 20, message: '用户名长度应在2到20个字符之间', trigger: 'blur' }
    ],
    name: [
      { required: true, message: '请输入姓名', trigger: 'blur' },
      { min: 2, max: 10, message: '姓名长度应在2到10个字符之间', trigger: 'blur' }
    ],
    gender: [
      { required: true, message: '请选择性别', trigger: 'change' }
    ],
    phone: [
      { required: true, message: '请输入手机号', trigger: 'blur' },
      { pattern: /^1\d{10}$/g, message: '请输入有效的手机号', trigger: 'blur' }
    ]
  });

/* 头像上传 */
// 图片文件上传成功后触发
  const handleAvatarSuccess = (response, uploadFile) => {
   employee.value.image = response.data
  }

// 文件上传之前触发
  const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('只支持上传图片')
    return false
  } else if (rawFile.size / 1024 / 1024 > 10) {
    ElMessage.error('只能上传10M以内图片')
    return false
  }
  return true
}
//文件上传携带的token
const token = ref('')

const getToken = () => {
  const loginuser = JSON.parse(localStorage.getItem('loginUser'));
  if(loginuser && loginuser.token){
    token.value = loginuser.token
  }

}

//工作经历
//动态添加工作经历 .
  const addExprItem = () => {
    employee.value.exprList.push({exprDate: [], begin: '', end: '', company: '', job: ''})
  }

//动态删除工作经历 .
  const delExprItem = (index) => {
    employee.value.exprList.splice(index, 1)
  }

//监听-employee员工对象中的工作经历数据
  watch(()=>employee.value.exprList, (newValue, oldValue) => {
    if(employee.value.exprList && employee.value.exprList.length > 0) {
      employee.value.exprList.forEach(expr => {
        expr.begin = expr.exprDate[0]
        expr.end = expr.exprDate[1]
      })
    }
  }, {deep: true});

/* 钩子函数 */
onMounted(() => {
  search()
  getToken()
})
</script>

<!-- ========================================== HTML ============================================================================================ -->
<template>
  <h3 style="color: cornflowerblue;">|&nbsp;&nbsp;员工管理</h3>
  
  <!-- 页面搜索栏 -->
  <div>
    <el-form :inline="true" :model="searchEmp" class="demo-form-inline">
      <el-form-item label="姓名">
        <el-input v-model="searchEmp.name" placeholder="请输入姓名" clearable />
      </el-form-item>

      <el-form-item label="性别">
        <el-select
          v-model="searchEmp.gender"
          placeholder="请选择性别"
          clearable
        >
          <el-option label="男" value="1" />
          <el-option label="女" value="2" />
        </el-select>
      </el-form-item>

      <el-form-item label="入职日期">

        <el-date-picker
          v-model="searchEmp.date"
          type="daterange"
          range-separator="到"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button type="info" @click="clear">清空</el-button>
      </el-form-item>

    </el-form>
  </div>

  <!-- 表格展示通用操作按钮 -->
  <div>
    <el-button type="primary" @click="addEmp">+&nbsp;新增员工</el-button>
    <el-button type="danger" @click="deleteByIds">-&nbsp;批量删除</el-button>
    <br><br>
  </div>

  <!-- 员工管理表格数据展示 -->
  <div>
    <el-table :data="empList" border style="width: 100%" @selection-change="handleSelectionChange"> <!-- 这是顶行复选框 -->
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="name" label="姓名" width="120" align="center"></el-table-column>
      <el-table-column label="性别" width="170" align="center">
        <template #default="scope" >
          {{ scope.row.gender == 1 ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column label="头像" width="100" align="center">
        <template #default="scope" >
          <img :src="scope.row.image" width="50" alt="Avatar" class="avatar" />
        </template>
      </el-table-column>
      <el-table-column prop="deptName" label="部门名称" width="170" align="center"></el-table-column>
      <el-table-column label="职位" width="120" align="center">
        <template #default="scope">
            <span v-if="scope.row.job == 1">班主任</span>
            <span v-else-if="scope.row.job == 2">讲师</span>
            <span v-else-if="scope.row.job == 3">学工主管</span>
            <span v-else-if="scope.row.job == 4">教研主管</span>
            <span v-else-if="scope.row.job == 5">咨询师</span>
            <span v-else style="color: red;">暂无部门</span>
          </template>
      </el-table-column>
      <el-table-column prop="entryDate" label="入职日期" width="180" align="center"></el-table-column>
      <el-table-column prop="updateTime" label="最后操作时间" width="210" align="center"></el-table-column>
      <el-table-column label="操作" fixed="right" align="center">
        <template #default="scope">
          <el-button size="small" type="primary" @click="edit(scope.row.id)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteEmp(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <br>

  <!-- 分页栏 -->
  <div>
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

  <!-- 新增员工表单数据Dialog 对话框 --> 
   <el-dialog
    v-model="dialogVisible"
    :title="dialogTitle" 
    width="800"
    :before-close="handleClose"
  >
    <!-- ref="employeeFormRef" : 表单绑定的校验规则对象 ; :rules="rules" : 表单绑定的校验规则 -->
    <el-form ref="employeeFormRef" :model="employee" :rules="rules" class="my-custom-form">

      <!-- 第一行 -->
      <el-row :gutter="20">

        <el-col :span="12">
          <!-- prop="" : 绑定的校验规则唯一名 -->
          <el-form-item label="用户名" prop="username">
            <el-input v-model="employee.username" placeholder="请输入员工用户名,2-20个字" clearable />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="employee.name"  placeholder="请输入员工姓名,2-10个字" clearable />
          </el-form-item>
        </el-col>

      </el-row>
      
      <!-- 第二行 -->
      <el-row :gutter="20">

        <el-col :span="12">
          <el-form-item label="性别"  prop="gender">
            <el-select
              v-model="employee.gender"
              placeholder="请选择性别"
              clearable
            >
              <el-option v-for="(gender, index) in genders" :key="index" :label="gender.name" :value="gender.value" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="手机号"  prop="phone">
            <el-input v-model="employee.phone"  placeholder="请填入员工手机号" clearable />
          </el-form-item>
        </el-col>

      </el-row>

      <!-- 第三行 -->
      <el-row :gutter="20">

        <el-col :span="12">
          <el-form-item label="职位">
            <el-select
              v-model="employee.job"
              placeholder="请选择"
              clearable
            >
              <!-- <el-option label="Zone one" value="shanghai" /> -->
              <el-option v-for="(job, index) in jobs" :key="index" :label="job.name" :value="job.value" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="薪资">
            <el-input v-model="employee.salary"  placeholder="请输入员工薪资" clearable />
          </el-form-item>
        </el-col>

      </el-row>

      <!-- 第四行 -->
      <el-row :gutter="20">

        <el-col :span="12">
          <el-form-item label="所属部门">
            <el-select
              v-model="employee.deptId"
              placeholder=""
              clearable
            >
              <el-option v-for="(dept, index) in depts" :key="index" :label="dept.name" :value="dept.value" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="入职日期">
            <el-date-picker
              :disabled="disabled"
              v-model="employee.entryDate"
              type="date"
              placeholder="请选择入职日期"
              clearable
            />
          </el-form-item>
        </el-col>

      </el-row>

      <!-- 第五行 -->
      <el-row>
        <el-col :span="24">
          <el-form-item>
            头像：
            <el-upload
              class="avatar-uploader"
              action="/api/upload" 
              :headers="{'token': token}"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
            >

              <img v-if="employee.image" :src="employee.image" class="avatar" style="width: 150px; height: 150px;" />
              <el-icon v-else class="avatar-uploader-icon">|选择文件|</el-icon>
              
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 工作经历 -->
        <!-- 第六行 -->
        <br>
        <el-row :gutter="10">
          <el-col :span="24">
            <el-form-item label="工作经历">
              <el-button type="success" size="small" @click="addExprItem">+ 添加工作经历</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第七行 ...  工作经历 -->
        <el-row :gutter="3" v-for="(expr, index) in employee.exprList">
          <el-col :span="10">
            <el-form-item size="small" label="时间" label-width="80px">
              <el-date-picker type="daterange" v-model="expr.exprDate" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD" ></el-date-picker>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item size="small" label="公司" label-width="60px">
              <el-input placeholder="请输入公司名称" v-model="expr.company"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item size="small" label="职位" label-width="60px">
              <el-input placeholder="请输入职位" v-model="expr.job"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="2">
            <el-form-item size="small" label-width="0px">
              <el-button type="danger" @click="delExprItem(index)">- 删除</el-button>
            </el-form-item>
          </el-col>
        </el-row>

    </el-form>

    <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="onSubmit">提交</el-button>
    </div>
  </el-dialog>

</template>

<!-- ==========================================CSS============================================================================================ -->
<style scoped>
  /* 1. 强制所有表单项的 label 宽度一致 */
  .my-custom-form .el-form-item__label {
    width: 90px;       /* 固定宽度，根据你最长的文字调整，比如 100px */
    text-align: left;  /* 左对齐，这样“用户名”和“所属部门”左边是对齐的 */
    padding-right: 10px; /* 增加一点 label 和输入框之间的间距 */
  }

  /* 2. 确保输入框占满剩余空间 */
  .my-custom-form .el-input,
  .my-custom-form .el-select,
  .my-custom-form .el-date-picker {
    width: 100%; 
  }

  /* 3. (可选) 如果想让 label 文字垂直居中，可以微调行高 */
  .my-custom-form .el-form-item {
    margin-bottom: 20px; /* 统一行间距 */
  }

  .dialog-footer{
    display: flex;           /* 使用 Flex 布局 */
    justify-content: flex-end; /* 内容靠右对齐 (如果想居中改成 center) */
    gap: 20px;               /* 按钮之间的间距 */
    padding-top: 10px;       /* 顶部留白，与上方表单区分开 */
    margin: 10px 0;
    border-top: 1px solid #eee; /* (可选) 加一条分割线，更有层次感 */
  }

</style>