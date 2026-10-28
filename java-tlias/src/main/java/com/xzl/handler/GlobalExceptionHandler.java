package com.xzl.handler;

import com.xzl.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/*全局异常处理类*/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /** 捕获数据库唯一约束异常*/
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result SqlException(SQLIntegrityConstraintViolationException ex){
        log.error("数据库唯一约束冲突: {}", ex.getMessage());

        String message = ex.getMessage();

        // MySQL 唯一约束冲突的错误信息格式：
        // Duplicate entry 'zhangsan' for key 'idx_username'
        if (message.contains("Duplicate entry")) {
            String[] split = message.split(" ");
            // split 就是违反唯一约束的那个值，例如 'zhangsan'
            String msg = split[2] + " 已存在";
            return Result.error(msg);
        }

        // 其他 SQL 约束异常
        return Result.error("数据保存失败");
    }

    /**
     * 方案一：专门捕获 Spring 翻译后的唯一键冲突异常 (推荐)
     * 这能直接匹配到日志中的 org.springframework.dao.DuplicateKeyException
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException ex) {
        log.error("数据唯一性冲突");

        // 解析异常信息，提取出冲突的值和字段
        String message = ex.getMessage();
        if (message != null && message.contains("Duplicate entry")) {
            try {
                // 格式: Duplicate entry '重复数据' for key 'id_card'
                String[] parts = message.split(" ");
                String duplicateValue = parts[9];

                // 这里可以根据不同的 key ('id_card', 'username' 等) 返回更具体的提示
                // 简单示例：直接提示值已存在
                return Result.error("[" + duplicateValue + "] 已存在，请勿重复添加");

            } catch (Exception e) {
                // 解析失败，使用默认提示
                return Result.error("数据保存失败，违反唯一约束");
            }
        }

        return Result.error("数据保存失败，请检查输入信息");
    }

    /** 自定义捕获消息异常*/
    @ExceptionHandler
    public Result myMessage(MyMessageException e){//方法形参中指定能够处理的异常类型
        log.error("异常: ", e.getMessage());
        //捕获到异常之后，响应一个标准的Result
        return Result.error(e.getMessage());
    }

    /** 捕获所有未处理的异常*/
    @ExceptionHandler(Exception.class)
    public Result ex(Exception e){//方法形参中指定能够处理的异常类型
        log.error("系统未知异常: ", e);
        //捕获到异常之后，响应一个标准的Result
        return Result.error("抱歉！服务器出现异常，请联系管理员");
    }

}
