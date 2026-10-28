<script setup>
  import { ref,onMounted } from "vue";
  import { logQueryApi } from "@/api/log.js";
  import { ElMessage } from "element-plus";
  
  /* 请求发送 */
  // GET查询日志 ==========
  const logQuery = async () => {
    const result = await logQueryApi(currentPage.value,pageSize.value)
    if(result.code){
      total.value = result.data.total
      logData.value = result.data.rows
    }else{
      ElMessage.error(result.msg)
    }
  }

  /* 日志表格数据展示 */
  const logData = ref(
    [
      /* { // 数据展示样式
        "id": 15,
        "operateEmpId": 2,
        "operateTime": "2023-12-18T17:37:15",
        "className": "com.itheima.controller.DeptController",
        "methodName": "add",
        "methodParams": "[Dept(id=null, name=投资部, createTime=null, updateTime=null)]",
        "returnValue": "Result(code=1, msg=null, data=null)",
        "costTime": 8,
        "operateEmpName": "宋江"
      } */
    ]
  )

  // 定义tag标签样式逻辑 =========
  function tagStyle(methodName){
    if (!methodName) return 'primary'
    const prefix = methodName.slice(0, 2)
    if (prefix === 'up') return 'success'
    if (prefix === 'de') return 'danger'
    return 'primary'
  }

  /* 分页组件对象及实列 */

  const currentPage = ref(1)
  const pageSize = ref(10)
  const size = ref('default')
  const total = ref(0)

  const handleSizeChange = (val) => {
    console.log(`${val} 每页项目数`)
    currentPage.value = 1
    pageSize.value = val
  }
  const handleCurrentChange = (val) => {
    console.log(`当前页: ${val}`)
    currentPage.value = val
  }

  onMounted(() => {
    logQuery()
  })

</script>

<template>
  <h3 style="color: cornflowerblue;">|&nbsp;&nbsp;日志管理</h3>

  <div>
    <el-table :data="logData" border style="width: 100% " size="small" >
      <el-table-column prop="operateEmpName" label="操作人" width="100" align="center" />
      <el-table-column prop="operateTime" label="操作时间" align="center" width="150" />
      <el-table-column prop="className" label="类名" align="center" />
      <el-table-column label="方法名" align="center" >
        <template #default="scope">
          <el-tag :type="tagStyle(scope.row.methodName)"> {{ scope.row.methodName }} </el-tag>  
        </template>
      </el-table-column>
      <el-table-column prop="costTime" label="操作耗时(ms/毫秒)" width="100" align="center" />
      <el-table-column prop="methodParams" label="请求参数" width="300" show-overflow-tooltip align="center" />
      <el-table-column prop="returnValue" label="返回值" align="center" />
    </el-table>
  </div>
  
  <div style="margin: 30px 0px;">
    <el-pagination
      :current-page="currentPage"
      :page-size="pageSize"
      :page-sizes="[10, 20, 30, 40]"
      :size="size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>

</template>

<style scoped>

</style>
