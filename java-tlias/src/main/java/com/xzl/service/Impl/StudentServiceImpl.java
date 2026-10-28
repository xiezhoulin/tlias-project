package com.xzl.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xzl.mapper.StudentMapper;
import com.xzl.pojo.PageResult;
import com.xzl.pojo.Result;
import com.xzl.pojo.Student;
import com.xzl.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    /** 条件分页查询*/
    public PageResult conditionalPageQuery(String name, Integer degree, Integer clazzId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);

        Page<Student> students = studentMapper.getByConditionalQueryStudents(name, degree, clazzId);

        PageInfo<Student> pageInfo = new PageInfo<>(students);

        return new PageResult(pageInfo.getTotal(),students);
    }

    /** 添加学员*/
    public void addStudent(Student student) {
        student.setViolationCount((short) 0);
        student.setViolationScore((short) 0);
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());

        studentMapper.addStudent(student);
    }

    /** 根据ID查询*/
    public Student getByIdQuery(Integer id) {
        Student student = studentMapper.getByIdQueryStudent(id);
        return student;
    }

    /** 修改学员*/
    public void updateStudent(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        student.setGraduationDate(student.getGraduationDate().plusDays(1));

        studentMapper.getByIdUpdateStudent(student);
    }

    /** 删除学员*/
    public void delStudents(List<Integer> ids) {
        studentMapper.getByIdDeleteStudents(ids);
    }

    /** 违纪处理*/
    public void disciplinaryAction(Integer id, Integer score) {
        studentMapper.getByIdUpdateStudentViolationData(id,score);
    }

}
