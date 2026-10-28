package com.xzl.service.Impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xzl.mapper.ClazzMapper;
import com.xzl.pojo.Clazz;
import com.xzl.pojo.PageResult;
import com.xzl.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    ClazzMapper clazzMapper;

    /** 查询所有班级*/
    public List<Clazz> queryAllClazzs() {
        List<Clazz> clazzList = clazzMapper.queryAllClazzs();
        return clazzList;
    }

    /** 班级列表数据的条件分页查询*/
    public PageResult conditionalPageQuery(String name, LocalDate begin, LocalDate end, Integer page, Integer pageSize) {

        //开始分页
        PageHelper.startPage(page,pageSize);

        //条件查询
        List<Clazz> clazzList = clazzMapper.getByConditionalPageQuery(name,begin,end);

        /*计算班级的班级状态
            - 显示为：未开班、已结课、在读中 这三种
                - 当前时间 > 结课时间：状态为 = 已结课。
                - 当前时间 < 开课时间：状态为 = 未开班。
                - 否则，就是 = 在读中
        */

        LocalDate now = LocalDate.now();
        clazzList.forEach(clazz -> {
            // 返回负数表示“小/早”，返回正数表示“大/晚”，返回 0 表示相等
            int i = now.compareTo(clazz.getEndDate());
            int j = now.compareTo(clazz.getBeginDate());

            if(i > 0){
                clazz.setStatus("已结课");
            } else if (j < 0) {
                clazz.setStatus("未开班");
            }else {
                clazz.setStatus("在读中");
            }
        });

        //把数据封装到PageInfo对象里
        PageInfo<Clazz> pageInfo = new PageInfo<>(clazzList);
        long total = pageInfo.getTotal();

        //封装返回对象
        PageResult pageResult = new PageResult();
        pageResult.setTotal(total);
        pageResult.setRows(pageInfo.getList());

        return pageResult;
    }

    /** 添加班级*/
    public void addClazz(Clazz clazz) {
        //设置班级的创建时间及修改时间
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());

        clazzMapper.addClazz(clazz);
    }

    /** 根据ID查询*/
    public Clazz getByIdClazz(Integer id) {
        Clazz clazz = clazzMapper.getByIdQueryClazz(id);
        return clazz;
    }

    /** 修改班级*/
    public void updateClazz(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.getByIdUpdateClazz(clazz);
    }

    /** 删除班级*/
    public void deleteClazz(Integer id) {
        clazzMapper.deleteGetByIdClazz(id);
    }

}
