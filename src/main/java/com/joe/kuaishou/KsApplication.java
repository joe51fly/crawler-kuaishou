package com.joe.kuaishou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.mybatis.spring.annotation.MapperScan;


/*extends SpringBootServletInitializer*/
@MapperScan("com.joe.kuaishou.mapper") //扫描的mapper
@SpringBootApplication
public class KsApplication{
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(KsApplication.class);
//    }
    public static void main(String[] args) {
        SpringApplication.run(KsApplication.class, args);
    }
}
