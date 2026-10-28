package com.xzl.pojo;

import lombok.Data;

/**
 * 后端统一返回结果类
 * */
@Data
public class Result {
    private Integer code;
    private Object data;
    private String msg;

    public static Result success(){
        Result result = new Result();
        result.code = 1;
        result.msg = "success";
        return result;
    }

    public static Result success(Object object){
        Result result = new Result();
        result.code = 1;
        result.data = object;
        result.msg = "success";
        return result;
    }

    public static Result error(String msg){
        Result result = new Result();
        result.code = 0;
        result.msg = msg;
        return result;
    }

}
