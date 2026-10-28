package com.xzl.mapper;

import com.xzl.pojo.Emp;
import com.xzl.pojo.EmpQueryParam;
import com.xzl.pojo.EmpUpdatePassword;
import com.xzl.pojo.reportEntity.ReportEmpJob;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {

    /** 员工登录*/
    @Select("select * from emp where username = #{username} and password = #{password}")
    Emp login(Emp emp);

    /** 员工列表查询*/
    List<Emp> getByAllQueryEmp(@Param("page")Integer page, @Param("pageSize")Integer pageSize);

    /** 查询员工表的总数*/
    @Select("select count(1) from emp")
    Integer count();

    /** 查询全部员工*/
    @Select("select * from emp")
    List<Emp> allEmp();

    /** 根据条件分页查询*/
    List<Emp> getByConditionQueryEmp(EmpQueryParam emp);

    /** 添加员工*/
    @Options(useGeneratedKeys = true,keyProperty = "id") //返回对应的字段值
    @Insert("INSERT INTO `emp` (`username`, `password`, `name`, `gender`, `phone`, `job`, `salary`, `image`, `entry_date`, `dept_id`, `create_time`, `update_time`) " +
            "VALUES (#{username},#{password},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
    Integer addEmp(Emp emp);

    /** 根据主键ID查询员工基本信息*/
    @Select("SELECT e.*,d.`name` as deptName FROM `emp` as e JOIN dept as d on e.dept_id = d.id WHERE e.id = #{id}")
    Emp getByIdQueryEmpBasic(Integer id);

    /** 根据主键ID查询员工的信息*/
    @Select("select * from emp where id = #{id}")
    Emp getByIdQueryEmp(Integer id);

    /** 修改员工*/
    @Update("UPDATE `emp` SET `username` = #{username}, `password` = '123456', `name` = #{name}, " +
            "`gender` = #{gender}, `phone` = #{phone}, `job` = #{job}, `salary` =#{salary}, `image` = #{image}, " +
            "`entry_date` = #{entryDate}, `dept_id` = #{deptId}, `update_time` = #{updateTime} WHERE `id` = #{id};")
    void getByIdUpdateEmp(Emp emp);

    /** 根据员工id修改密码*/
    @Update("UPDATE `emp` SET `password` = #{newPassword} WHERE `id` = #{empId};")
    void getByEmpIdUpdatePassword(EmpUpdatePassword emp);

    /** 根据id删除多个或一个员工*/
    void getByIdsDelEmp(@Param("ids") List<Integer> ids);

    /** 统计接口=========================================================================================================*/

    /** 统计员工性别*/
    @Select("SELECT case gender WHEN 1 THEN '男性员工' WHEN 2 THEN '女性员工' END as name,COUNT(gender) as value FROM `emp` GROUP BY gender")
    ArrayList<HashMap<String, Integer>> countEmpGender();

    /** 员工职位人数统计*/
    @Select("SELECT IFNULL(job, '其它') as jobList,COUNT(*) as dataList FROM `emp` GROUP BY job")
    ArrayList<Map<String, Object>> countEmpJob();

}
