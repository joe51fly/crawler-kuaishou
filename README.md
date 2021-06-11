# crawler-kuaishou

## 快手直播web项目

### 功能：

**1.通过cookie登录，可以看以关注主播的直播**

**2.可以通过输入视频链接，在网页上观看视频**

# 使用说明

**clone到本地，只能打war包，打jar包的话写入Cookie的时候会报错，找不到配置文件的错误。**

1.把**index.html**里两处this.$http.jsonp('http://192.168.2.10:8080/data') ip地址改成本机ip。

```html
this.$http.jsonp('http://192.168.2.10:8080/data', {
    params: {inputCookie: app.inputCookie},
    jsonp: 'callback'
})
```

2.更改**KuaishouLiveKit**类里**KsCrawler3.0.py**的路径，一定是绝对路径

```java
String[] args = new String[]{"python", "F:\\Applications\\workSpaces\\crawler-kuaishou\\KsCrawler3.0.py", headers, url};
```

3.更改**KsApplication**类

```java
/*extends SpringBootServletInitializer*/
@SpringBootApplication
public class KsApplication{
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(KsApplication.class);
//    }
    public static void main(String[] args) {
        SpringApplication.run(KsApplication.class, args);
    }
}
```

为

```java
/*extends SpringBootServletInitializer*/
@SpringBootApplication
public class KsApplication extends SpringBootServletInitializer{
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(KsApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(KsApplication.class, args);
    }
}
```

4.更改pom.xml,去掉下面位置的注释

```xml
<packaging>war</packaging>

<exclusions>
    <exclusion>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
    </exclusion>
</exclusions>
```

5.完成上面的步骤，就到最后一步--打war包

​	<font color=#FF0000>**点击idea右边的maven->crawler-kuaishou->Lifecycle->package**</font>
