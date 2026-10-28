package com.xzl.service.Impl;

import com.xzl.handler.MyMessageException;
import com.xzl.mapper.EmpExprMapper;
import com.xzl.mapper.EmpMapper;
import com.xzl.pojo.*;
import com.xzl.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private EmpExprMapper empExprMapper;

    /** 员工登录*/
    @Override
    public LoginInfo login(Emp emp) {
        Emp empResult = empMapper.login(emp);
        if (empResult != null){
            return new LoginInfo(empResult.getId(),empResult.getUsername(),empResult.getName(),null);
        }
        return null;
    }

    /** 员工列表查询*/
    public PageResult queryAllEmp(Integer page, Integer pageSize) {

        page = (page-1) * pageSize; //计算页码索引公式
        List<Emp> empList = empMapper.getByAllQueryEmp(page,pageSize);

        Integer count = empMapper.count(); //查询总数据
        /*int count = empList.size();*/

        PageResult empsPageResult = new PageResult();
        //封装到分页对象并返回
        empsPageResult.setRows(empList);
        empsPageResult.setTotal(Long.valueOf(count));

        return empsPageResult;
    }

    /** 根据条件查询*/
    @Override
    public PageResult conditionQueryEmp(EmpQueryParam emp) {

        Integer page = (emp.getPage()-1) * emp.getPageSize(); //计算页码索引公式
        emp.setPage(page);

        List<Emp> empList = empMapper.getByConditionQueryEmp(emp);

        PageResult empsPageResult = new PageResult();
        //封装到分页对象并返回
        empsPageResult.setRows(empList);
        empsPageResult.setTotal((long) empList.size());

        return empsPageResult;
    }

    /** 查询全部员工*/
    @Override
    public List<Emp> allEmp() {
        List<Emp> empList = empMapper.allEmp();

        empList.forEach(emp -> {
            emp.setPassword("******");
        });

        return empList;
    }

    /** 添加员工*/
    @Transactional
    @Override
    public void addemp(Emp emp) {
        emp.setPassword("123456"); //设置初始密码
        emp.setCreateTime(LocalDateTime.now()); //设置创建时间
        emp.setUpdateTime(LocalDateTime.now()); //设置修改时间

        empMapper.addEmp(emp);
        Integer empId = emp.getId();
        List<EmpExpr> exprList = emp.getExprList();
        if(!CollectionUtils.isEmpty(exprList)){ //这里写调用插入工作经历的sql
            /*分析:
            一个员工，是可以有多段工作经历的，所以在页面上将来用户录入员工信息时，可以自己根据需要添加多段工作经历。*/
            exprList.forEach(empExpr -> empExpr.setEmpId(empId));
            empExprMapper.addEmpExpr(exprList);
        }

    }

    /** 根据主键ID查询员工基本信息*/
    @Override
    public Emp getByIdEmpBasic(Integer id) {
        Emp emp = empMapper.getByIdQueryEmpBasic(id);

        emp.setPassword(null);

        return emp;
    }

    /** 根据主键ID查询员工的信息*/
    @Override
    public Emp getByIdEmp(Integer id) {
        //查询员工的基本信息
        Emp emp = empMapper.getByIdQueryEmp(id);
        emp.setPassword("******");

        //查询员工的工作经历信息
        List<EmpExpr> exprList = empExprMapper.getByEmpIdQuery(id);

        emp.setExprList(exprList);

        return emp;
    }

    /** 根据id修改员工*/
    @Transactional
    @Override
    public void updateEmp(Emp emp) {
        /*分析：1.修改员工要修改两张表，需要用到事物注解。2.修改员工经历根据员工id把数据删除，然后在添加现在的数据*/
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.getByIdUpdateEmp(emp);

        List<EmpExpr> exprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(exprList)){
            Integer empId = emp.getId();
            empExprMapper.getByIdDelEmpExp(empId); //先根据id删除对应的工作经历
            exprList.forEach(empExpr -> empExpr.setEmpId(empId));
            empExprMapper.addEmpExpr(exprList);
        }

    }

    /** 根据员工id修改密码*/
    @Override
    public void updateEmpPassword(EmpUpdatePassword emp){
        // 1.查询原来的密码进行校验
        Emp resultEmp = empMapper.getByIdQueryEmp(emp.getEmpId());

        if(!resultEmp.getPassword().equals(emp.getCurrentPassword())){
            throw new MyMessageException("密码输入不正确");
        }
        if(emp.getEmpId() == null){
            throw new MyMessageException("员工id为空");
        }

        // 2.修改密码
        empMapper.getByEmpIdUpdatePassword(emp);

    }

    /** 根据id删除多个或一个员工*/
    @Transactional
    @Override
    public void deleteEmps(List<Integer> ids) {
        /*分析：不仅要删除员工还要删除员工工作经历*/
        //1.删除员工
        empMapper.getByIdsDelEmp(ids);

        //2.根据员工Id删除工作经历信息
        empExprMapper.getByIdsDelEmpExpr(ids);

    }

}
