import request from '@/utils/request'

/* 查询所有班级接口 */
export const queryAllClazzsApi = () => request.get("/clazzs/list")

/* 班级列表分页查询接口 */
export const conditionalPageQueryApi = (name,begin,end,page,pageSize) => request.get(`/clazzs?name=${name}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`)

/* 添加班级接口 */
export const addClazzsApi = (clazzs) => request.post('/clazzs',clazzs)

/* 根据ID查询班级接口(编辑按钮回显班级信息) */
export const getByIdClazz = (clazzId) => request.get(`/clazzs/${clazzId}`)

/* 修改班级接口 */
export const updateClazzsApi = (clazzs) => request.put('/clazzs',clazzs)

/* 删除班级接口 */
export const deleteClazzsApi = (id) => request.delete(`/clazzs/${id}`)