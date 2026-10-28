<script setup>
import { ref,onMounted,watch } from 'vue'
import { ElMessage,ElMessageBox } from 'element-plus'
import { conditionalPageQueryApi,addClazzsApi,getByIdClazz,updateClazzsApi,deleteClazzsApi } from "@/api/clazz";
import { allEmpApi } from "@/api/emp";
import { queryAllEmpApi } from '../../api/emp';

  /* 钩子函数 */
  onMounted(() => {
    pageQuery()
    allEmp()
  })
  //发送请求：班级列表查询 ==========>
  const pageQuery = async () => {
    const result = await conditionalPageQueryApi(search.value.name, search.value.begin, search.value.end, currentPage.value, pageSize.value)
    if(result.code){
      total.value = result.data.total
      clazzTableData.value = result.data.rows
    }
  }

  //发送请求：查询所有员工（班主任） ==========>
  const allEmp = async () => {
    const result = await allEmpApi()
    if(result.code){
      teachers.value = result.data
    }else{
      ElMessage.error(result.msg)
    }
  }

  //发送请求：新增员工信息 ==========>
  async function addClazzs(){
    const result = await addClazzsApi(dialogForm.value)
      if(result.code){
        ElMessage.success('添加成功')
        pageQuery()
        centerDialogVisible.value = false
      }else{
        ElMessage.error(`添加失败:${result.msg}`)
      }
  }

  //发送请求：修改员工信息 ==========>
  async function updateClazzs(){
    delete dialogForm.value.createTime
    delete dialogForm.value.updateTime
    const result = await updateClazzsApi(dialogForm.value)
      if(result.code){
        ElMessage.success('修改成功')
        pageQuery()
        centerDialogVisible.value = false
      }else{
        ElMessage.error(`修改失败:${result.msg}`)
      }
  }

  //搜索栏响应式对象及函数 ==========
  const search = ref({
    name: '',
    date: [],
    begin: '',
    end: ''
  })

    //-搜索栏查询按钮
    const onSubmit = () => {
      pageQuery()
    }

    //-搜索栏重置按钮
    const reset = () => {
      search.value = {
        name: '',
        date: [],
        begin: '',
        end: ''
      }
    }

    //-watch侦听search.date属性
    watch(() => search.value.date,(newVal) => {
      if (!newVal || newVal.length !== 2) return // 如果 newVal 为空或不是数组/有效格式，直接返回
      // 赋值操作
      search.value.begin = newVal[0]
      search.value.end = newVal[1]
    })

  //新增班级按钮 ==========
  const addClassButton = () => {  
    dialogForm.value = {
      name: '',
      room: '',
      beginDate: null,
      endDate: null,
      masterId: '',
      subject: ''
    }
    centerDialogVisible.value = true
    title.value = "新增班级"
  }

  //新增班级or修改班级按钮dialog弹窗响应对象及函数 ==========
  const centerDialogVisible = ref(false) //dialog框的显示隐藏
  
  const title = ref('') //dialog标题

  const dialogFormRef = ref() //获取表单实例（用于后续调用 validate 方法）
  const dialogForm = ref({ //dialog对象的数据绑定
    name: '',
    room: '',
    beginDate: null,
    endDate: null,
    masterId: '',
    subject: ''
  })

  const teachers = ref([]) //dialog教师下拉框数据响应式对象
  const subjects = ref([{'name':'java','value':1},{'name':'前端','value':2},{'name':'大数据','value':3},{'name':'python','value':4},{'name':'go','value':5},{'name':'嵌入式','value':6}]) //dialog学科下拉框数据响应式对象
  

  const submitForm = async () => { //提交表单时的验证逻辑
    if (!dialogFormRef.value) return
    const valid = await dialogFormRef.value.validate().catch(() => false)// 调用 validate 方法进行整体校验
    if (valid) {
      console.log('验证通过，提交数据！');
      if(dialogForm.value.id != null){ //判断dialog中的id属性是否为空来执新增还是修改操作
        //修改操作
        console.log('执行修改');
        updateClazzs()
      }else{
        //新增操作
        console.log('执行添加');
        addClazzs()
      }
    } else {
      ElMessage.error('请填写完整的表单信息')
    }
  }
  const noButton = () =>{ //取消后重置表单
    centerDialogVisible.value = false
    if (!dialogFormRef.value) return
    dialogFormRef.value.resetFields()
    dialogForm.value = {
      name: '',
      room: '',
      beginDate: null,
      endDate: null,
      masterId: '',
      subject: ''
    }
  }

  const rules = ref({ //表单校验
    name: [
      { required: true, message: '请输入班级名称', trigger: 'blur' },
      { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
    ],
    // 开课时间：必填
    beginDate: [
      { required: true, message: '请选择开课时间', trigger: 'change' }
    ],
    // 结课时间：必填
    endDate: [
      { required: true, message: '请选择结课时间', trigger: 'change' }
    ],
    // 学科：必填
    subject: [
      { required: true, message: '请输入学科', trigger: 'blur' }
    ]
  })

  //表格数据响应式对象 ==========
  const clazzTableData = ref([
    /* { 
        //表格数据对象示例
        "id": 7,
        "name": "黄埔四期",
        "room": "209",
        "beginDate": "2023-08-01",
        "endDate": "2024-02-15",
        "masterId": 7,
        "createTime": "2023-06-01T17:51:21",
        "updateTime": "2023-06-01T17:51:21",
        "masterName": "纪晓芙",
        "status": "未开班 , 在读 , 已结课" 
    } */
  ])

  //编辑按钮
  const edit = async (id) => {
    centerDialogVisible.value = true
    title.value = '修改班级'
    const result = await getByIdClazz(id) //根据id查询回显班级信息
    if(result.code){
      dialogForm.value = result.data
    }else{
      ElMessage.error(result.msg)
    }
  }

  //删除按钮
  const del = (id) => {
    ElMessageBox.confirm('你要删除此数据吗？','提示',{confirmButtonText: '确定',cancelButtonText: '取消',type: 'warning',})
        .then( async () => {
          console.log("执行删除");
          const result = await deleteClazzsApi(id)
          if(result.code){
            ElMessage.success("删除成功！")
            pageQuery()
          }else{
            ElMessage.error("错误:" + result.msg)
          }
        })
        .catch(() => {
          ElMessage.info("已取消")
        })
  }

  //分页栏数据设置及函数 ==========
  const currentPage = ref(1)
  const pageSize = ref(5)
  const size = ref('default')
  const total = ref(0)

  const handleSizeChange = (val) => {
    console.log(`SizeChange${val}`)
    pageSize.value = val
    currentPage.value = 1
    pageQuery()
  }
  const handleCurrentChange = (val) => {
    console.log(`${val} 获取到页数`)
    currentPage.value = val
    pageQuery()
  }

</script>

<!-- ========================================== HTML ============================================================================================ -->
<template>
  <h3 style="color: cornflowerblue;">|&nbsp;&nbsp;班级管理</h3>

  <!-- 表单搜索对象及按钮框 ==========-->
   <el-form :inline="true" :model="search" class="demo-form-inline">

    <el-form-item label="班级名称">
      <el-input v-model="search.name" placeholder="请输入班级名称" clearable />
    </el-form-item>

    <el-form-item label="结课时间">
      <el-date-picker
        v-model="search.date"
        type="daterange"
        start-placeholder="开始时间"
        end-placeholder="结束时间"
        value-format="YYYY-MM-DD"
        range-separator="到"
        clearable
      />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="onSubmit">查询</el-button>
      <el-button type="info" @click="reset">清空</el-button>
    </el-form-item>

  </el-form>

  <!-- 新增班级按钮 ==========-->
   <el-button type="success" @click="addClassButton">+ 新增班级</el-button>

  <!-- 新增班级按钮dialog-对话框 ==========-->
  <el-dialog v-model="centerDialogVisible" :title="title" width="500">

    <el-form 
      :model="dialogForm" 
      label-width="auto" 
      class="addOrUpdate-form"
      :rules="rules"
      ref="dialogFormRef"
    >
      
      <el-form-item label="班级名称" prop="name">
        <el-input v-model="dialogForm.name" placeholder="请输入班级名称" />
      </el-form-item>

      <el-form-item label="班级教室">
        <el-input v-model="dialogForm.room" placeholder="请输入班级教室" />
      </el-form-item>

      <el-form-item label="开课时间" prop="beginDate">
        <el-date-picker
          v-model="dialogForm.beginDate"
          type="date"
          placeholder="请选择开课时间"
          style="width: 100%" 
        />
      </el-form-item>

      <el-form-item label="结课时间" prop="endDate">
        <el-date-picker
          v-model="dialogForm.endDate"
          type="date"
          placeholder="请选择结课时间"
          style="width: 100%"
        />
      </el-form-item>
      
      <el-form-item label="班主任">
        <el-select
          v-model="dialogForm.masterId"
          clearable
          placeholder="请选择班主任"
          style="width: 100%" 
        >
          <el-option v-for="teacher in teachers" 
            :key="teacher.id" 
            :label="teacher.name" 
            :value="teacher.id" 
          />
        </el-select>
      </el-form-item>

      <el-form-item label="学科" prop="subject">
        <el-select
          v-model="dialogForm.subject"
          clearable
          placeholder="请选择学科"
          style="width: 100%"
        >
          <el-option v-for="subject in subjects" 
            :key="subject.value" 
            :label="subject.name" 
            :value="subject.value" 
          />
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer >
      <div class="dialog-footer">

        <el-button @click="noButton">取消</el-button>

        <el-button type="primary" @click="submitForm">
          提交
        </el-button>

      </div>
    </template>

  </el-dialog>

  <!-- 表格数据展示 ==========-->
   <el-table :data="clazzTableData" stripe style="width: 100%">

    <el-table-column type="index" label="序号" width="60" align="center" />
    <el-table-column prop="name" label="班级名称"  align="center" />
    <el-table-column prop="room" label="班级教室" width="100" align="center" />
    <el-table-column prop="masterName" label="班主任" align="center" />
    <el-table-column prop="beginDate" label="开课时间" align="center" />
    <el-table-column prop="endDate" label="结课时间" align="center" />
    <el-table-column prop="status" label="状态" align="center" />
    <el-table-column prop="updateTime" label="最后修改时间" align="center" />

    <el-table-column label="操作" align="center">
      <template #default="scope">
        
        <!-- 编辑按钮 -->
        <el-button type="primary" @click="edit(scope.row.id)" circle >
          <el-icon><Edit /></el-icon>
        </el-button>
        <!-- 删除按钮 -->
        <el-button type="danger" @click="del(scope.row.id)" circle >
          <el-icon><Delete /></el-icon>
        </el-button>

      </template>
    </el-table-column>

  </el-table>

  <!-- 分页栏展示 ==========-->
  <div style="margin: 10px 0px">
    <el-pagination
      :current-page="currentPage"
      :page-size="pageSize"
      :page-sizes="[5, 10, 20, 30, 50]"
      :size="size"
      background
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<!-- ========================================== CSS ============================================================================================ -->
<style scoped>

  .demo-form-inline .el-input {
    --el-input-width: 220px;
  }

</style>