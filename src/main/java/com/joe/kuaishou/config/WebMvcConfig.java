package com.joe.kuaishou.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: joe
 * @createTime: 2021-12-15 15:10
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")   //所有方法都做处理跨域
                .allowedOrigins("http://localhost:8081")  //允许跨域的请求头
                .allowedMethods("*")  //润许通过地请求方法
                .allowedHeaders("*");  //允许的请求头
    }
}
