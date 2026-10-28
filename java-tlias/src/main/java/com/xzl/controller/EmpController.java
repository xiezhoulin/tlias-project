package com.xzl.controller;

import com.xzl.anno.LogOperation;
import com.xzl.pojo.*;
import com.xzl.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    /** 员工列表查询*/
    @GetMapping
    public Result queryAllEmp(Integer page,Integer pageSize){
        PageResult empList = empService.queryAllEmp(page,pageSize);

        return Result.success(empList);
    }

    /** 根据条件查询*/
    @GetMapping("/condition")
    public Result conditionQueryEmp(EmpQueryParam emp){
        PageResult empList = empService.conditionQueryEmp(emp);
        return Result.success(empList);
    }

    /** 询全部员工*/
    @GetMapping("/list")
    public Result queryAllEmp(){
        List<Emp> empList = empService.allEmp();
        return Result.success(empList);
    }

    /** 添加员工*/
    @LogOperation
    @PostMapping()
    public Result addEmp(@RequestBody Emp emp){
        empService.addemp(emp);
        return Result.success();
    }

    /** 根据主键ID查询员工基本信息*/
    @GetMapping("/basic/{id}")
    public Result getByIdEmpBasic(@PathVariable Integer id){
        Emp emp = empService.getByIdEmpBasic(id);
        return Result.success(emp);
    }

    /** 根据主键ID查询员工与工作经历信息*/
    @GetMapping("/{id}")
    public Result getByIdEmp(@PathVariable Integer id){
        Emp emp = empService.getByIdEmp(id);
        return Result.success(emp);
    }

    /** 修改员工*/
    @LogOperation
    @PutMapping()
    public Result updateEmp(@RequestBody Emp emp){
        empService.updateEmp(emp);
        return Result.success();
    }

    /** 根据员工id修改密码*/
    @PutMapping("/updatePws")
    public Result updateEmpPassword(@RequestBody EmpUpdatePassword emp){
        empService.updateEmpPassword(emp);
        return Result.success();
    }

    /** 根据id删除多个或一个员工*/
    @LogOperation
    @DeleteMapping
    public Result deleteEmps(@RequestParam("ids") List<Integer> ids){
        empService.deleteEmps(ids);
        return Result.success();
    }
}
