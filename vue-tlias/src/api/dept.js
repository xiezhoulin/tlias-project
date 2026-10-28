import request from '@/utils/request'

export const queryAllApi = () => request.get('/depts') //查询所有部门
export const addDeptApi = (data) => request.post('/depts',data) //添加部门请求
export const queryInfoApi = (id) => request.get(`/depts/${id}`) //根据id回显部门数据请求
export const updateDeptApi = (data) => request.put(`/depts`,data) //修改部门请求
export const deleteDeptApi = (id) => request.delete(`/depts?id=${id}`) //修改部门请求
