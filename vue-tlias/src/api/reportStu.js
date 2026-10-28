import request from '@/utils/request'

/* 查询班级人数统计接口 */
export const queryReportStudentCountApi = () => request.get("/report/studentCountData")

/* 查询学历分组统计人数接口 */
export const queryReportStudentDegreeDataApi = () => request.get("/report/studentDegreeData")