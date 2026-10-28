package com.xzl.pojo;

import lombok.Data;

/** 员工修改密码接收实体类*/

@Data
public class EmpUpdatePassword {

    private Integer empId; // 员工id
    private String currentPassword; // 原密码
    private String newPassword; // 新密码

}
