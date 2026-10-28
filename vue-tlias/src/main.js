import { createApp } from 'vue'
import App from './App.vue' /* 根组件 */
import router from './router'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue' //全局注册所有element-plus-icons图标
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs' // 引入中文语言包

/* 创建应用实例 */
const app = createApp(App)

/* 注册组件 */
app.use(router)

app.use(ElementPlus,{ //设置全局中文
  locale: zhCn,
})

for (const [key, component] of Object.entries(ElementPlusIconsVue)) { //遍历并全局注册所有图标组件
  app.component(key, component)
}

app.mount('#app')
