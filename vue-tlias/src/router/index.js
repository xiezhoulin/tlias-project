import { createRouter, createWebHistory } from 'vue-router'

import layoutView from '@/views/layout/index.vue' /* 基础页面 */
import clazzView from '@/views/clazz/index.vue'
import deptView from '@/views/dept/index.vue'
import empView from '@/views/emp/index.vue'
import indexView from '@/views/index/index.vue'
import logView from '@/views/log/index.vue'
import loginView from '@/views/login/index.vue'
import empReportView from '@/views/report/emp.vue'
import stuReportView from '@/views/report/stu.vue'
import stuView from '@/views/stu/index.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: layoutView,
      redirect : '/index', /* 重定向到首页 */
      children : [
        {path: '/index',name: 'index',component: indexView},
        {path: '/clazz',name: 'clazz',component: clazzView},
        {path: '/stu',name: 'stu',component: stuView},
        {path: '/dept',name: 'dept',component: deptView},
        {path: '/emp',name: 'emp',component: empView},
        {path: '/empReport',name: 'empReport',component: empReportView},
        {path: '/stuReport',name: 'stuReport',component: stuReportView},
        {path: '/log',name: 'log',component: logView},
      ]
    },
    {path: '/login',name: 'login',component: loginView}, /* 登录页面 */
  ]
})

// 全局路由守卫
router.beforeEach((to, from ,next) => {
  const loginUser = JSON.parse(localStorage.getItem('loginUser'))

  // 1.如果去登录页面，直接放行
  if(to.path === '/login'){
    next()
  }else{
    // 2.如果是去其它页面，检查是否有token
    if(loginUser && loginUser.token){
      next() //已登录，放行
    }else{
      //3.未登录，强行跳转到登录页面
      next('/login')
    }
  }
})

export default router
