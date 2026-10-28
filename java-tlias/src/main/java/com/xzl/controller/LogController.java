package com.xzl.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xzl.mapper.LogMapper;
import com.xzl.pojo.OperateLog;
import com.xzl.pojo.PageResult;
import com.xzl.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LogController {

    @Autowired
    LogMapper logMapper;

    /** 分页查询日志*/
    @GetMapping("/log/page")
    public Result queryLog(Integer page,Integer pageSize){

        if(page == null || pageSize == null){ // 等于空的时设置默认值
            page = 1;
            pageSize = 10;
        }

        PageHelper.startPage(page,pageSize);

        List<OperateLog> logList = logMapper.queryAllLog();
        PageInfo<OperateLog> pageInfo = new PageInfo<>(logList);
        long total = pageInfo.getTotal();

        return Result.success(new PageResult(total,logList));
    }

}
