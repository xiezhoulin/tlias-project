package com.xzl.service;

import com.xzl.pojo.Dept;

import java.util.List;

public interface DeptService {

    /** 查询所有部门*/
    List<Dept> queryAllDept();

    /** 添加部门*/
    void save(Dept dept);

    /** 根据ID查询部门数据*/
    Dept queryInfo(Integer id);

    /** 修改部门*/
    void updateDept(Dept dept);

    /** 根据ID删除部门*/
    void delDept(Integer id);
}
