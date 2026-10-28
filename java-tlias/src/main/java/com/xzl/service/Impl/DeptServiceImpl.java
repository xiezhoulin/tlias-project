package com.xzl.service.Impl;

import com.xzl.handler.MyMessageException;
import com.xzl.mapper.DeptMapper;
import com.xzl.pojo.Dept;
import com.xzl.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    /** 查询所有部门*/
    @Override
    public List<Dept> queryAllDept() {
        List<Dept> dept = deptMapper.queryAllDept();
        return dept;
    }

    /** 添加部门*/
    @Override
    public void save(Dept dept) {
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());

        deptMapper.addDept(dept);
    }

    /** 根据ID查询部门数据*/
    @Override
    public Dept queryInfo(Integer id) {
        Dept dept = deptMapper.getByDeptId(id);
        return dept;
    }

    /** 修改部门*/
    @Override
    public void updateDept(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.getByIdUpdateDept(dept);
    }

    /** 根据ID删除部门*/
    @Override
    public void delDept(Integer id) {
        // 条件；查询部门下没有员工才能进行删除，否则抛异常
        Integer count = deptMapper.deptIdCountEmp(id);
        if (count > 0){
            throw new MyMessageException("该部门下有员工,不能删除！");
        }

        deptMapper.getByIdDeleteDept(id);
    }

}
