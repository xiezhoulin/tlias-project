package com.xzl.anno;

import com.xzl.mapper.OperateLogMapper;
import com.xzl.pojo.OperateLog;
import com.xzl.util.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class OperationLogAspect {

    @Autowired
    OperateLogMapper operateLogMapper;

    /* 记录操作日志*/
    @Around("@annotation(log)")
    public Object around(ProceedingJoinPoint joinPoint,LogOperation log) throws Throwable {
        /* 记录执行的方法耗时*/
        long startTime = System.currentTimeMillis(); // 获取方法执行开始的时间
        Object result = joinPoint.proceed(); // 执行目标方法
        long endTime = System.currentTimeMillis();// 获取方法执行后的时间
        long costTime = endTime - startTime; // 执行方法的耗时

        /* 构建日志对象*/
        OperateLog operateLog = new OperateLog();
        operateLog.setOperateEmpId(getCurrentUserId());
        operateLog.setOperateTime(LocalDateTime.now());
        operateLog.setClassName(joinPoint.getTarget().getClass().getName());
        operateLog.setMethodName(joinPoint.getSignature().getName());
        operateLog.setMethodParams(Arrays.toString(joinPoint.getArgs()));
        operateLog.setReturnValue(result.toString());
        operateLog.setCostTime(costTime);

        /* 插入日志*/
        operateLogMapper.insert(operateLog);

        return result;
    }

    /* 获取操作员工的id*/
    private Integer getCurrentUserId() {
        return CurrentHolder.getEmpId();
    }

}
