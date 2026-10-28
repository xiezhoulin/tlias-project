package com.xzl.config;

import com.xzl.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /*拦截器对象*/
    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Value("${app.image-upload}")
    private String uploadDir;

    /*配置静态资源访问*/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // addResourceHandler: 浏览器访问的虚拟路径（网络路径前缀）
        // addResourceLocations: 图片实际存放的本地绝对路径（注意 file:/// 后面有三个斜杠）
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///" + uploadDir);
    }

    /*注册配置拦截器*/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器对象
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**") //设置拦截器拦截的请求路径（ /** 表示拦截所有请求）
                .excludePathPatterns("/login",
                                     "/images/**"); //设置不拦截的请求路径
    }

}
