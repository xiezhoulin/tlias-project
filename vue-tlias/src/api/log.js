import request from '@/utils/request'

/* 查询操作日志接口 */
export const logQueryApi = (page,pageSize) => request.get(`/log/page?page=${page}&pageSize=${pageSize}`)