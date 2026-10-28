import request from '@/utils/request'

  export const loginApi = (data) => request.post("/login",data) //用户登录接口