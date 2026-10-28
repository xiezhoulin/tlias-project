import request from '@/utils/request'

/* 条件查询学员接口 */
export const conditionalPageQueryApi = (name,degree,clazzId,page,pageSize) => request.get(`/students?name=${name}&degree=${degree}&clazzId=${clazzId}&page=${page}&pageSize=${pageSize}`)

/* 添加学员接口 */
export const addStudentApi = (student) => request.post("/students",student)

/* 根据ID查询学员接口 */
export const getByIdQueryApi = (id) => request.get(`/students/${id}`)

/* 修改学员接口 */
export const updateStudentApi = (student) => request.put("/students",student)

/* 删除学员接口 */
export const delStudentApi = (ids) => request.delete(`/students/${ids}`)

/* 记录违纪学员接口 */
export const violationApi = (id,score) => request.put(`/students/violation/${id}/${score}`)