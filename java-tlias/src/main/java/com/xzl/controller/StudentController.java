package com.xzl.controller;

import com.xzl.anno.LogOperation;
import com.xzl.pojo.PageResult;
import com.xzl.pojo.Result;
import com.xzl.pojo.Student;
import com.xzl.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /** 条件分页查询*/
    @GetMapping
    public Result conditionalPageQuery(String name, Integer degree, Integer clazzId,Integer page,Integer pageSize){
        log.info("学员分页查询参数:{},{},{},{},{}",name,degree,clazzId,page,pageSize);
        PageResult pageStudnets = studentService.conditionalPageQuery(name,degree,clazzId,page,pageSize);
        return Result.success(pageStudnets);
    }

    /** 添加学员*/
    @LogOperation
    @PostMapping
    public Result addStudent(@RequestBody Student student){
        log.info("添加学员参数:{}",student);
        studentService.addStudent(student);
        return Result.success();
    }

    /** 根据ID查询*/
    @GetMapping("/{id}")
    public Result getByIdQuery(@PathVariable Integer id){
        log.info("根据ID查询:{}",id);
        Student student = studentService.getByIdQuery(id);
        return Result.success(student);
    }

    /** 修改学员*/
    @LogOperation
    @PutMapping
    public Result updateStudent(@RequestBody Student student){
        log.info("修改学员:{}",student);
        studentService.updateStudent(student);
        return Result.success();
    }

    /** 删除学员*/
    @LogOperation
    @DeleteMapping("/{ids}")
    public Result delStudents(@PathVariable List<Integer> ids){
        log.info("删除学员id:{}",ids);
        studentService.delStudents(ids);
        return Result.success();
    }

    /** 违纪处理*/
    @LogOperation
    @PutMapping("/violation/{id}/{score}")
    public Result disciplinaryAction(@PathVariable Integer id,
                                     @PathVariable Integer score){
        log.info("违纪处理,学生ID:{},扣除分数:{}",id,score);
        studentService.disciplinaryAction(id,score);
        return Result.success();
    }

}
