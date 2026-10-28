package com.xzl.service;

import com.xzl.pojo.PageResult;
import com.xzl.pojo.Student;

import java.util.List;

public interface StudentService {

    /** 条件分页查询*/
    PageResult conditionalPageQuery(String name, Integer degree, Integer clazzId, Integer page, Integer pageSize);

    /** 添加学员*/
    void addStudent(Student student);

    /** 根据ID查询*/
    Student getByIdQuery(Integer id);

    /** 修改学员*/
    void updateStudent(Student student);

    /** 删除学员*/
    void delStudents(List<Integer> ids);

    /** 违纪处理*/
    void disciplinaryAction(Integer id, Integer score);

}
