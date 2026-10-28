package com.xzl.controller;

import com.xzl.pojo.Emp;
import com.xzl.pojo.LoginInfo;
import com.xzl.pojo.Result;
import com.xzl.service.EmpService;
import com.xzl.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class LoginInfoController {

    @Autowired
    private EmpService empService;

    @PostMapping("/login")
    public Result login(@RequestBody Emp data){
        LoginInfo loginInfo = empService.login(data);
        if (loginInfo != null){ //登录成功，生成jwt令牌并封装数据返回
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",loginInfo.getId());
            map.put("username",loginInfo.getUsername());

            String toKen = JwtUtils.generateJwt(map);

            loginInfo.setToken(toKen);

            return Result.success(loginInfo);
        }
        return Result.error("账号或密码错误!");
    }

}
