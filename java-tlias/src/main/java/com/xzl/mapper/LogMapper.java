package com.xzl.mapper;

import com.xzl.pojo.OperateLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper {

    @Select("SELECT l.*,e.`name` AS operateEmpName FROM `operate_log` l INNER JOIN emp e on l.operate_emp_id = e.id ORDER BY operate_time DESC")
    List<OperateLog> queryAllLog();

}
