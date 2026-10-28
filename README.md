# 项目简介

一个简单的全栈 Web 项目，主要用于实现基本的增删改查及图标统计功能。项目包含前端页面、后端接口和数据库初始化脚本，适合本地开发、部署练习或作为全栈项目参考。

## 技术栈

- 前端：Vue 3、Vite、Vue Router、Element Plus、Axios
- 后端：Spring Boot、MyBatis
- 数据库: MySQL

## 项目结构
.
├── java-tlias/     # Spring Boot 后端项目
├── sql/            # 数据库初始化脚本
├── vue-tlias/      # Vue3 前端项目
├── README.md

## 项目部署

### 前端
- 将vue-tlias文件夹拖入编辑器打开即可
### 后端
- 需要jdk17及以上的版本
- 将sjava-tlias文件夹拖入编辑器打开
- 注意事项: 需要将application.yml文件数据库配置设置为自己的配置以及设置本地图片的保存路径
### 数据库文件部署
- 在本地数据库里执行sql脚本即可