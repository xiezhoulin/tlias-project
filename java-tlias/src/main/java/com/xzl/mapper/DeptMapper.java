package com.xzl.mapper;

import com.xzl.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {

    /** 查询所有部门*/
    @Select("select * from dept")
    List<Dept> queryAllDept();

    /** 添加部门*/
    @Insert("INSERT INTO `dept` (`name`, `create_time`, `update_time`) VALUES (#{name},#{createTime},#{updateTime})")
    void addDept(Dept dept);

    /** 根据ID查询部门数据*/
    @Select("select * from dept where id=#{id}")
    Dept getByDeptId(Integer id);

    /** 修改部门*/
    @Update("UPDATE `dept` SET `name` = #{name} , `update_time` = #{updateTime}  WHERE `id` = #{id}")
    void getByIdUpdateDept(Dept dept);

    /** 根据ID删除部门*/
    @Delete("delete from dept where id = #{id}")
    void getByIdDeleteDept(Integer id);

    /** 根据部门id统计部门下的员工*/
    @Select("SELECT COUNT(0) FROM `dept` d JOIN emp e on d.id = e.dept_id WHERE d.id = #{id}")
    Integer deptIdCountEmp(Integer id);

}
