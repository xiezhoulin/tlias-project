<!-- ==========================================JavaScript============================================== -->
<script setup>
  import { ref,onMounted,reactive } from "vue";
  import { queryAllApi,addDeptApi,queryInfoApi,updateDeptApi,deleteDeptApi } from "@/api/dept";
  import { ElMessage,ElMessageBox } from 'element-plus'

  /* 部门表格数据模型 */
  /* const deptList = ref([
        "id": 1,
        "name": "学工部",
        "createTime": "2023-09-25/09:47:40",
        "updateTime": "2024-07-25/09:47:40"
      ]) */
  let deptList = ref([])

  /* 异步请求查询数据 */
  const queryAll = async () => {
    const re = await queryAllApi()
    deptList.value = re.data
  }

  /* 新增和编辑按钮绑定的响应式模型；默认false，true为打开隐藏dialog对话框 */
  const depts = ref({id: '',name: ''})
  let dialogTitleName = ref({name: ''})

  const dialogFormVisible = ref(false)

  //点击确定提交按钮
  const ok = async () => { 
    await deptFormRef.value.validate( async valid => { //校验表单合法
      if(valid){
        
        let result = null;
        if(!deptList.value.id){
          result = await updateDeptApi(depts.value) //修改部门数据
        }else{
          result = await addDeptApi(depts.value) //添加部门数据
        }
        
        if(reseult.code){ //隐式表达式：数字0为false，其它则为true
          dialogFormVisible.value = false
          ElMessage.success('提交成功！')
          deptFormRef.value.resetFields() //重置表单
          queryAll() //查询最新数据
        }else{
          ElMessage.error(`提交失败：${reseult.msg}`)
          dialogFormVisible.value = true
        }

      }else{
        ElMessage.error('提交失败!请确保格式正确...')
        dialogFormVisible.value = true
      }

    })
  }  

  const no = () => { //点击取消提交
    dialogFormVisible.value = false
    deptFormRef.value.resetFields() //重置表单
}

  const addButton = () => {
    dialogFormVisible.value = true,
    dialogTitleName.value.name = '新增部门'
    depts.value.name = ''
  }

  //点击编辑按钮进行编辑
  const updateButton = async (deptId) => { 
    dialogFormVisible.value = true,
    dialogTitleName.value.name = '修改部门'
    //根据id回显当前部门
    const result = await queryInfoApi(deptId) 
    if(result.code){
      depts.value.id = result.data.id
      depts.value.name = result.data.name
    }else{
      ElMessage.error('回显失败')
    }
  }

  //点击删除按钮进行删除操作
  const deleteButton = (id) => {
    console.log(`Delete item with ID ${id}`);
    //删除部门时, 需要弹出一个确认框, 如果是确认, 则删除部门
    ElMessageBox.confirm('此操作将永久删除该部门, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(async () => {
      // 删除部门
      const result = await deleteDeptApi(id)
      if(result.code){
        ElMessage.success('删除成功')
        queryAll()
      }else{
        ElMessage.error(result.msg)
      }
    })
  }

  /* 表单校验规则 */
  const deptFormRef = ref(null)

  const rules = reactive(
    {
      name: [
        { required: true, message: '选项不能为空'},
        { min: 3, max: 5, message: '长度必须是3到5个字符', trigger: 'blur' }
      ]
    })

  /* 钩子函数 */
  onMounted(() => {
    queryAll()
  })
</script>

<!-- ==========================================HTML============================================== -->
<template>
  <div class="deptTitle">
    <h3>|&nbsp;&nbsp;部门管理</h3>
  </div>

  <!-- 表单内容 -->
  <div class="from">
    <!-- 新增部门按钮 -->
    <div class="button">
      <el-button type="primary" @click="addButton">+新增部门</el-button>
    </div>
    <!-- 表格数据 -->
    <el-table :data="deptList" border style="width: 100%">
      <el-table-column type="index" label="序号" width="100" align="center" />
      <el-table-column prop="name" label="部门名称" width="180" align="center" />
      <el-table-column prop="updateTime" label="最后修改时间" align="center" />
      <el-table-column label="操作" align="center">
        <!-- #default="scope": 定义作用域插槽，"scope" 是接收到的参数对象; scoped.row: 代表当前行的数据对象; scoped.row.id: 获取当前行数据中的 id 字段-->
        <template #default="scope"> 
          <el-button size="small" @click="updateButton(scope.row.id)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteButton(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  

  <!-- 点击新增部门弹出的dialog对话框-默认隐藏；新增和编辑公用一个 -->
  <el-dialog v-model="dialogFormVisible" :title='dialogTitleName.name' width="500">
    <!-- {{ depts }} -->
    <el-form :model="depts" :rules="rules" ref="deptFormRef">
      <el-form-item label="部门名称" prop="name" :label-width="formLabelWidth">
        <el-input v-model="depts.name" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="no">取消</el-button>&nbsp;
        <el-button type="primary" :plain="true" @click="ok">确定</el-button>
      </div>
    </template>
  </el-dialog>

</template>

<!-- ==========================================CSS============================================== -->
<style scoped>
  .deptTitle {color: cornflowerblue;margin: 10px 0px;}

  .from{margin: 0px 20px;}
    .button {
      margin: 10px 0px;
      display: flex;
      justify-content: flex-end; /* 关键属性：主轴末端对齐 */
    }

  .dialog-footer {margin: 0px 35%;}

</style>
