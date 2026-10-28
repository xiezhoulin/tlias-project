package com.xzl.mapper;

import com.xzl.pojo.Emp;
import com.xzl.pojo.EmpExpr;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmpExprMapper {

    /** 批量保存工作经历*/
    void addEmpExpr(@Param("exprList") List<EmpExpr> exprList);

    /** 根据员工ID查询员工的工作经历信息*/
    @Select("SELECT * FROM `emp_expr` where emp_id = #{empId}")
    List<EmpExpr> getByEmpIdQuery(Integer empId);

    /** 根据员工ID删除员工的工作经历信息*/
    @Delete("delete from emp_expr where emp_id = #{empId}")
    void getByIdDelEmpExp(Integer empId);

    /** 根据员工Id删除工作经历信息*/
    void getByIdsDelEmpExpr(@Param("ids") List<Integer> ids);
}
