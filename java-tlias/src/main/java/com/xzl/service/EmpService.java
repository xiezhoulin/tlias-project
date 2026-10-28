package com.xzl.service;

import com.xzl.handler.MyMessageException;
import com.xzl.pojo.*;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {

    /** 员工登录*/
    LoginInfo login(Emp emp);

    /** 员工列表查询*/
    PageResult queryAllEmp(Integer page, Integer pageSize);

    /** 根据条件查询*/
    PageResult conditionQueryEmp(EmpQueryParam emp);

    /** 查询全部员工*/
    List<Emp> allEmp();

    /** 添加员工*/
    void addemp(Emp emp);

    /** 根据主键ID查询员工基本信息*/
    Emp getByIdEmpBasic(Integer id);

    /** 根据主键ID查询员工与工作经历信息*/
    Emp getByIdEmp(Integer id);

    /** 修改员工*/
    void updateEmp(Emp emp);

    /** 根据id删除多个或一个员工*/
    void deleteEmps(List<Integer> ids);

    /** 根据员工id修改密码*/
    void updateEmpPassword(EmpUpdatePassword emp) throws MyMessageException;

}
