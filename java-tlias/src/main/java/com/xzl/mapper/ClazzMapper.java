package com.xzl.mapper;

import com.xzl.pojo.Clazz;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface ClazzMapper {

    /** 查询所有班级*/
    @Select("SELECT * FROM clazz;")
    List<Clazz> queryAllClazzs();

    /** 班级列表数据的条件分页查询*/
    List<Clazz> getByConditionalPageQuery(@Param("name") String name,
                                          @Param("begin") LocalDate begin,
                                          @Param("end") LocalDate end);

    /** 添加班级*/
    @Insert("INSERT INTO `clazz` (`name`, `room`, `begin_date`, `end_date`, `master_id`, `subject`, `create_time`, `update_time`)" +
            "                     VALUES (#{name},#{room},#{beginDate},#{endDate},#{masterId},#{subject},#{createTime},#{updateTime});")
    void addClazz(Clazz clazz);

    /** 根据ID查询*/
    @Select("SELECT * FROM clazz WHERE id = #{id};")
    Clazz getByIdQueryClazz(Integer id);

    /** 修改班级*/
    @Update("UPDATE clazz SET `name` = #{name},`room` = #{room},`begin_date` = #{beginDate},`end_date` = #{endDate},`master_id` = #{masterId},`subject` = #{subject},`update_time` = #{updateTime} WHERE `id` = #{id};")
    void getByIdUpdateClazz(Clazz clazz);

    /** 删除班级*/
    @Delete("DELETE FROM clazz WHERE `id` = #{id}")
    void deleteGetByIdClazz(Integer id);

    /** 统计接口=========================================================================================================*/

    /** 查询各个班级人数*/
    @Select("SELECT c.`name`,COUNT(s.clazz_id) AS size FROM `clazz` AS c LEFT JOIN student AS s ON c.id = s.clazz_id GROUP BY c.`name`")
    List<Map<String, Object>> studentCountData();

}
