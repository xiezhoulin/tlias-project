package com.xzl.service;


import com.xzl.pojo.Clazz;
import com.xzl.pojo.PageResult;

import java.time.LocalDate;
import java.util.List;


public interface ClazzService {

    /** 查询所有班级*/
    List<Clazz> queryAllClazzs();

    /** 班级列表数据的条件分页查询*/
    PageResult conditionalPageQuery(String name, LocalDate begin, LocalDate end, Integer page, Integer pageSize);

    /** 添加班级*/
    void addClazz(Clazz clazz);

    /** 根据ID查询*/
    Clazz getByIdClazz(Integer id);

    /** 修改班级*/
    void updateClazz(Clazz clazz);

    /** 删除班级*/
    void deleteClazz(Integer id);

}
