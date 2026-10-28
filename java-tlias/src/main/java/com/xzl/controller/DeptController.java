package com.xzl.controller;

import com.xzl.anno.LogOperation;
import com.xzl.pojo.Dept;
import com.xzl.pojo.Result;
import com.xzl.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/depts")
public class DeptController {

    @Autowired
    private DeptService deptService;

    /** 查询所有部门*/
    @GetMapping
    public Result queryAllDept(){
        List<Dept> deptList = deptService.queryAllDept();
        return Result.success(deptList);
    }

    /** 添加部门*/
    @LogOperation
    @PostMapping
    public Result saveDept(@RequestBody Dept dept){
        deptService.save(dept);
        return Result.success();
    }

    /** 根据ID查询部门数据*/
    @GetMapping("/{id}")
    public Result queryInfo(@PathVariable Integer id){
        Dept dept = deptService.queryInfo(id);
        return Result.success(dept);
    }

    /** 根据ID修改部门*/
    @LogOperation
    @PutMapping
    public Result updateDept(@RequestBody Dept dept){
        deptService.updateDept(dept);
        return Result.success();
    }

    /** 根据ID删除部门*/
    @LogOperation
    @DeleteMapping
    public Result delDept(Integer id){
        deptService.delDept(id);
        return Result.success();
    }

}
