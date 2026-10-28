package com.xzl.controller;

import com.xzl.anno.LogOperation;
import com.xzl.pojo.Clazz;
import com.xzl.pojo.PageResult;
import com.xzl.pojo.Result;
import com.xzl.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {

    @Autowired
    ClazzService clazzService;

    /** 查询所有班级*/
    @GetMapping("/list")
    public Result queryAllClazzs(){
        log.info("查询所有班级");
        List<Clazz> clazzList = clazzService.queryAllClazzs();
        return Result.success(clazzList);
    }

    /** 班级列表数据的条件分页查询*/
    @GetMapping
    public Result conditionalPageQuery(String name, LocalDate begin, LocalDate end, Integer page, Integer pageSize){
        log.info("班级列表查询：{},{},{},{},{}",name,begin,end,page,pageSize);
        PageResult clazzeList = clazzService.conditionalPageQuery(name,begin,end,page,pageSize);
        return Result.success(clazzeList);
    }

    /** 添加班级*/
    @LogOperation
    @PostMapping
    public Result addClazz(@RequestBody Clazz clazz){
        log.info("添加班级对象数据:{}",clazz);
        clazzService.addClazz(clazz);
        return Result.success();
    }

    /** 根据ID查询*/
    @GetMapping("/{id}")
    public Result getByIdClazz(@PathVariable Integer id){
        log.info("根据ID查询:{}",id);
        Clazz clazzObject = clazzService.getByIdClazz(id);
        return Result.success(clazzObject);
    }

    /** 修改班级*/
    @LogOperation
    @PutMapping
    public Result updateClazz(@RequestBody Clazz clazz){
        log.info("修改班级:{}",clazz);
        clazzService.updateClazz(clazz);
        return Result.success();
    }

    /** 删除班级*/
    @LogOperation
    @DeleteMapping("/{id}")
    public Result deleteClazz(@PathVariable Integer id){
        log.info("删除班级:{}",id);
        clazzService.deleteClazz(id);
        return Result.success();
    }

}
