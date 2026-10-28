package com.xzl.mapper;

import com.github.pagehelper.Page;
import com.xzl.pojo.Student;
import com.xzl.pojo.reportEntity.ReportStudentDegree;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {

    /** 条件分页查询*/
    Page<Student> getByConditionalQueryStudents(@Param("name") String name,
                                                @Param("degree") Integer degree,
                                                @Param("clazzId") Integer clazzId);

    /** 添加学员*/
    @Insert("INSERT INTO `student` (`name`, `no`, `gender`, `phone`, `id_card`, `is_college`, `address`, `degree`, `graduation_date`, `clazz_id`, `violation_count`, `violation_score`, `create_time`, `update_time`) " +
            "VALUES (#{name},#{no},#{gender},#{phone},#{idCard},#{isCollege},#{address},#{degree},#{graduationDate},#{clazzId},#{violationCount},#{violationScore},#{createTime},#{updateTime});")
    void addStudent(Student student);

    /** 根据ID查询*/
    @Select("SELECT * FROM student WHERE `id` = #{id}")
    Student getByIdQueryStudent(Integer id);

    /** 修改学员*/
    @Update("UPDATE `student` SET `name` = #{name}, `no` = #{no}, `gender` = #{gender}, `phone` = #{phone}, `id_card` = #{idCard}, `is_college` = #{isCollege}, `address` = #{address}, `degree` = #{degree}, `graduation_date` = #{graduationDate}, `clazz_id` = #{clazzId}, `update_time` = #{updateTime} " +
            "WHERE `id` = #{id}")
    void getByIdUpdateStudent(Student student);

    /** 批量删除学员*/
    void getByIdDeleteStudents(@Param("ids") List<Integer> ids);

    /** 违纪处理*/
    @Update("UPDATE `student` SET violation_score = violation_score + #{score},violation_count = violation_count + 1 " +
            "WHERE `id` = #{id}")
    void getByIdUpdateStudentViolationData(Integer id,Integer score);

    /** 统计接口=========================================================================================================*/

    /** 根据学历分组统计人数*/
    @Select("SELECT degree as `name`,COUNT(*) as `value` FROM `student` GROUP BY degree")
    List<ReportStudentDegree> educationGrouping();

}
