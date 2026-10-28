package com.xzl.interceptor;

import com.xzl.util.CurrentHolder;
import com.xzl.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/** 定义拦截器--拦截请求校验jwtToken*/
@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    //目标资源方法执行前执行。 返回true：放行    返回false：不放行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截的路径: " + request.getRequestURI());
        /*if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }*/

        //1.获取jwt令牌
        String jwt = request.getHeader("token");

        //2.判断jwt是否为空
        if (StringUtils.isEmpty(jwt)){ //为空则返回401错误信息不放行
            log.info(jwt + "令牌为空");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //3.如果不为空则校验令牌
        try {
            Claims claims = JwtUtils.parseJwt(jwt);
            Integer empId = Integer.valueOf(claims.get("id").toString());
            CurrentHolder.setEmpId(empId);
        }catch (Exception e){ //解析失败报错返回401
            log.info("令牌非法");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        //放行
        log.info("令牌解析合法，放行");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        CurrentHolder.RmEmpId();
    }
}
