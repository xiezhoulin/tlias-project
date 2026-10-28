package com.xzl.util;

/**
 * 当前线程员工ID管理工具类
 * */
public class CurrentHolder {

    // 存储员工id的线程局部变量
    private static final ThreadLocal<Integer>  CURRENT_LOCAL = new ThreadLocal<>();

    // 设置线程局部变量员工id
    public static void setEmpId(Integer employeeId){
        CURRENT_LOCAL.set(employeeId);
    }

    // 获取线程局部变量员工id
    public static Integer getEmpId(){
        return CURRENT_LOCAL.get();
    }

    // 移除线程局部变量员工id
    public static void RmEmpId(){
        CURRENT_LOCAL.remove();
    }

}
