<!-- ========================================== JS ============================================================================================ -->
<script setup>
  import { ref, onMounted, onBeforeUnmount } from 'vue'
  import * as echarts from 'echarts'
  import { queryReportStudentCountApi,queryReportStudentDegreeDataApi } from '@/api/reportStu.js'
  import { ElMessage } from "element-plus"

  // 组件挂载后初始化
  onMounted(() => {
    queryReportStudentCount(),
    queryReportStudentDegreeData(),
    barInitChart(),
    pieInitchart()
  })

  // 请求：GET查询班级人数统计 ==========
  const queryReportStudentCount = async () => {
    const result = await queryReportStudentCountApi()
    if(result.code){
      barData.value = result.data
      // 数据拿到后，再初始化或更新图表
      if (myChart) {
        // 如果图表已经初始化了，直接更新数据
        updateChart(myChart,barData.value.clazzList,barData.value.dataList)
      } else {
        // 如果图表还没初始化，先初始化
        barInitChart()
      }
    }else{
      ElMessage.error("接口响应异常")
    }
  }

  // 请求：GET查询学历分组统计人数 ==========
  const queryReportStudentDegreeData = async () => {
    const result = await queryReportStudentDegreeDataApi()
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
        pieInitchart()
      }
    }else{
      ElMessage.error("接口响应异常")
    }
  }

  // 更新柱状图表数据 ==========
  const updateChart = (chart,xValue,yValue) => {
    if (chart) {
      chart.setOption({
        xAxis: {
          data: xValue
        },
        series: [
          {
            data: yValue
          }
        ]
      })
    }
  }

  // 获取 DOM 容器的引用 --柱状图 ==========
  const chartRef = ref(null)
  let myChart = null

  const barData = ref({
    clazzList: [],
    dataList: []
  })

  // 初始化图表
  const barInitChart = () => {
    if (chartRef.value) {
      myChart = echarts.init(chartRef.value)
      
      const bar = {
        title: { text: '班级人数统计',left: 'center' },
        xAxis: {
            type: 'category',
            data: barData.value.clazzList,
            axisLabel: {
              interval: 0  // 强制显示所有标签，不间隔
            }
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: barData.value.dataList,
              type: 'bar',
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#83bff6' },    // 顶部颜色
                  { offset: 1, color: '#188df0' }     // 底部颜色
                ])
              }
            }
          ]
      }

      myChart.setOption(bar)
    }

  }

  // 获取 DOM 容器的引用 --饼图 ==========
  const pieRef = ref(null)
  let myPie = null

  const pieData = ref([])

  const pieInitchart = () => {
    if (pieRef.value) {
      myPie = echarts.init(pieRef.value)
      
      const pie = {
          title: { text: '学员学历统计',left: 'center' },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            top: '7%',
            left: 'center'
          },
          series: [
            {
              name: '人数',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
              },
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: 40,
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data: [pieData.value]
            }
          ]
      };
      
      myPie.setOption(pie)
    }
  }

  // 组件销毁前销毁图表实例，防止内存泄漏 ==========
  onBeforeUnmount(() => {
    if (myChart) {
      myChart.dispose()
      myChart = null
    }
    if (myPie) {
      myPie.dispose()
      myPie = null
    }
  })
</script>

<!-- ========================================== HTML ============================================================================================ -->
<template>

  <h3 style="color: cornflowerblue;">|&nbsp;&nbsp;学员信息统计</h3>
  <!-- 图表容器，必须设置宽高 -->
   <div style="display: flex; margin: 120px 0px;">
    <div ref="chartRef" style="width: 80%; height: 400px;"></div>
    <div ref="pieRef" style="width: 60%; height: 400px;"></div>
   </div>

</template>

<!-- ========================================== CSS ============================================================================================ -->
<style scoped>

</style>
