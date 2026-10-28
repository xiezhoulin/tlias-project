package com.xzl.controller;

import com.xzl.mapper.ClazzMapper;
import com.xzl.mapper.EmpMapper;
import com.xzl.mapper.StudentMapper;
import com.xzl.pojo.Result;
import com.xzl.pojo.reportEntity.ReportClassSize;
import com.xzl.pojo.reportEntity.ReportEmpJob;
import com.xzl.pojo.reportEntity.ReportStudentDegree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/** 数据统计*/
@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController {

    @Autowired
    ClazzMapper clazzMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    EmpMapper empMapper;

    /** 班级人数统计*/
    @GetMapping("/studentCountData")
    public Result studentCount(){

        List<Map<String,Object>> list = clazzMapper.studentCountData();

        String[] strings = new String[list.size()];
        Integer[] integers = new Integer[list.size()];

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            strings[i] = (String) map.get("name");
            integers[i] = ((Number) map.get("size")).intValue();
        }

        ReportClassSize reportClassSize = new ReportClassSize();
        reportClassSize.setClazzList(strings);
        reportClassSize.setDataList(integers);

        return Result.success(reportClassSize);
    }

    /** 学员学历人数统计*/
    @GetMapping("/studentDegreeData")
    public Result studentDegreeCount(){
        /*log.info("学员学历人数统计");
        List<ReportStudentDegree> groderByCount = null;
        try {
            groderByCount = studentMapper.educationGrouping();

            for (ReportStudentDegree s : groderByCount) {
                String name = s.getName();

                switch (name) { // 1:初中, 2:高中, 3:大专, 4:本科, 5:硕士, 6:博士
                    case "1" :
                        s.setName("初中");
                        break;
                    case "2" :
                        s.setName("高中");
                        break;
                    case "3" :
                        s.setName("大专");
                        break;
                    case "4" :
                        s.setName("本科");
                        break;
                    case "5" :
                        s.setName("硕士");
                        break;
                    case "6" :
                        s.setName("博士");
                        break;
                    default: s.setName("未知");
                }
            }
        }catch (Exception e){
            return null;
        }
        return Result.success(groderByCount);*/

        /** AI优化---------------*/
        log.info("学员学历人数统计");
        // 1. 定义所有学历（编码 -> 中文）
        Map<String, String> degreeMap = new LinkedHashMap<>();
        degreeMap.put("1", "初中");
        degreeMap.put("2", "高中");
        degreeMap.put("3", "大专");
        degreeMap.put("4", "本科");
        degreeMap.put("5", "硕士");
        degreeMap.put("6", "博士");

        // 2. 初始化结果列表，所有学历默认值为 0
        Map<String, ReportStudentDegree> result = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : degreeMap.entrySet()) {
            result.put(entry.getKey(), new ReportStudentDegree(entry.getValue(), 0));
        }

        // 3. 查询数据库实际数据，覆盖默认值
        try {
            List<ReportStudentDegree> dbResult = studentMapper.educationGrouping();
            for (ReportStudentDegree item : dbResult) {
                String code = item.getName();  // 数据库存的是编码 "1", "2"...
                String name = degreeMap.getOrDefault(code, "未知");
                result.get(code).setValue(item.getValue());  // 覆盖为实际统计值
                result.get(code).setName(name);              // 转换为中文名称
            }
        } catch (Exception e) {
            log.error("学员学历统计失败");
            return Result.error("查询错误");
        }

        // 4. 返回完整结果（包含为 0 的分组）
        return Result.success(new ArrayList<>(result.values()));
    }

    /** 统计员工性别*/
    @GetMapping("/empGenderData")
    public Result countEmpGender(){
        log.info("统计员工性别");
        ArrayList<HashMap<String,Integer>> empGenderData = empMapper.countEmpGender();
        return Result.success(empGenderData);
    }

    /** 员工职位人数统计*/
    @GetMapping("/empJobData")
    public Result countEmpJob(){
        log.info("员工职位人数统计");
        ArrayList<Map<String,Object>> list = empMapper.countEmpJob();

        String[] strings = new String[list.size()];
        Integer[] integers = new Integer[list.size()];

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            String job = (String) map.get("jobList");
            switch (job) { // 1 班主任, 2 讲师 , 3 学工主管, 4 教研主管, 5 咨询师
                case "1" :
                    strings[i] = "班主任";
                    break;
                case "2" :
                    strings[i] = "讲师";
                    break;
                case "3" :
                    strings[i] = "学工主管";
                    break;
                case "4" :
                    strings[i] = "教研主管";
                    break;
                case "5" :
                    strings[i] = "咨询师";
                    break;
                default:
                    strings[i] = "其它";
            }
            integers[i] = ((Number) map.get("dataList")).intValue();
        }

        ReportEmpJob reportEmpJob = new ReportEmpJob();
        reportEmpJob.setJobList(strings);
        reportEmpJob.setDataList(integers);

        return Result.success(reportEmpJob);
    }

}
