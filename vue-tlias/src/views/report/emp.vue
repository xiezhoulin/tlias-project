<!-- ========================================== JS ============================================================================================ -->
<script setup>
  import * as echarts from 'echarts';
  import { empGenderDataApi,empJobDataApi } from "@/api/emp.js";
  import { ref,onMounted,onBeforeUnmount } from "vue";

  //请求：GET员工性别统计 ==========
  const empGenderData = async () => {
    const result = await empGenderDataApi()
    if(result.code){
      pieData.value = result.data
      // 数据拿到后，再初始化或更新图表
      if (myPie) {
        // 如果图表已经初始化了，直接更新数据
        myPie.setOption({
          series: {
            data: pieData.value
          }
        })
      } else {
        // 如果图表还没初始化，先初始化
        pieCharInit()
      }
    }
  }

  //请求：GET员工职位人数统计 ==========
  const empJobData = async () => {
    const result = await empJobDataApi()
    if(result.code){
      lineData.value = result.data
      // 数据拿到后，再初始化或更新图表
      if (myLine) {
        // 如果图表已经初始化了，直接更新数据
        myLine.setOption({
          xAxis: {
            data: lineData.value.jobList
          },
          series: {
            data: lineData.value.dataList
          }
        })
      } else {
        // 如果图表还没初始化，先初始化
        lineCharInit()
      }
    }
  }

  // 图表响应式对象和实列 ==========
  const pieCharRef = ref(null)
  const lineCharRef = ref(null)

  let myPie = null
  let myLine = null

  // 图表数据展示对象
  const pieData = ref([])
  const lineData = ref([])

  // 饼图初始化 - 员工性别统计
  const pieCharInit = () => {
    if(pieCharRef.value){
      myPie = echarts.init(pieCharRef.value)

      const peiOption = {
        title: {
          text: '员工性别统计',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '性别',
            type: 'pie',
            radius: '60%',
            data: pieData.value,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };

      myPie.setOption(peiOption)
    }
  }

  // 折线图初始化 - 员工职位人数统计
  const lineCharInit = () => {
    if(lineCharRef.value){
      myLine = echarts.init(lineCharRef.value)

      const lineOption = {
        title: { text: '员工职位人数统计',left: 'center' },
        xAxis: {
          type: 'category',
          data: []
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [],
            type: 'line'
          }
        ]
      };
      myLine.setOption(lineOption)
    }
  }

// 钩子函数 ============
onMounted(() => {
  empGenderData(),
  empJobData(),
  pieCharInit(),
  lineCharInit()
})

// 组件销毁前销毁图表实例，防止内存泄漏 ==========
onBeforeUnmount(() => {
  if (myPie) { myPie.dispose(), myPie = null }
  if (myLine) { myLine.dispose(), myLine = null }
})

</script>

<!-- ========================================== HTML ============================================================================================ -->
<template>
  <h3 style="color: cornflowerblue;">|&nbsp;&nbsp;员工信息统计</h3>
  <div style=" display: flex; margin: 120px 0px; ">
    <div ref="pieCharRef" style="width: 40%; height: 400px;"></div>
    <div ref="lineCharRef" style="width: 60%; height: 400px;"></div>
  </div>
</template>

<!-- ========================================== CSS ============================================================================================ -->
<style scoped>

</style>
