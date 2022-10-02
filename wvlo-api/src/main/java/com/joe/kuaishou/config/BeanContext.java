package com.joe.kuaishou.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description 在多线程时使用@Autowired总是获取不到bean，原因是：new thread不在spring容器中，也就无法获得spring中的bean对象。
 *              解决方法：手动获取 通过该类来手动获取
 * @author: joe
 * @createTime: 2021-09-24 20:50
 **/
@Component
public class BeanContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanContext.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T)applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clz) throws BeansException {
        return (T)applicationContext.getBean(clz);
    }

}