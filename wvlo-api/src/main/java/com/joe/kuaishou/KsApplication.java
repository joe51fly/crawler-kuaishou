package com.joe.kuaishou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/*extends SpringBootServletInitializer*/
@EnableTransactionManagement
@EnableScheduling // 开启定时任务功能
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
