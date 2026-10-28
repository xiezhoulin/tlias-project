import request from '@/utils/request'

export const queryAllEmpApi = (page,pageSize) => request.get(`/emps?page=${page}&pageSize=${pageSize}`) //分页查询所有员工数据
export const queryConditionEmpApi = (name,gender,begin,end,page,pageSize) => request.get(`/emps/condition?name=${name}&gender=${gender}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`) //条件分页查询员工数据

export const allEmpApi = () => request.get("/emps/list") //查询所有员工数据

export const addEmpApi = (emp) => request.post('/emps',emp) //添加员工

export const queryInfoApi = (id) => request.get(`/emps/${id}`) //根据id查询员工与工作经历

export const queryEmpApi = (id) => request.get(`/emps/basic/${id}`) //根据id查询员工基本信息

export const updateEmpApi = (emp) => request.put('/emps',emp) //修改员工

export const updateEmpPswApi = (data) => request.put('/emps/updatePws',data) //修改员工密码

export const delEmpApi = (ids) => request.delete(`/emps?ids=${ids}`) //删除员工

export const empGenderDataApi = () => request.get("/report/empGenderData") // 员工性别统计

export const empJobDataApi = () => request.get("/report/empJobData") // 员工职位人数统计
