import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

// 代码自动生成器
public class CodeProduct {
    public static void main(String[] args) {
        // 需要构建一个 代码自动生成器 对象
        AutoGenerator mpg = new AutoGenerator();

        // 配置策略
        // 1、全局配置
        GlobalConfig gc = new GlobalConfig();
        //代码生成的路径
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath+"/src/main/java/auto");
        //作者
        gc.setAuthor("joe");
        gc.setOpen(false);
        // 是否覆盖
        gc.setFileOverride(false);
        // 去Service的I前缀
        gc.setServiceName("%sService");

        //ID生成策略
        gc.setIdType(IdType.ID_WORKER);
        //时间生成策略
        gc.setDateType(DateType.ONLY_DATE);
        //开启Swagger2
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);


        //2、设置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/crawler?characterEncoding=utf-8&useSSL=false&serverTimezone=CTT&allowPublicKeyRetrieval=true");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        //数据库类型
        dsc.setDbType(DbType.MYSQL);
        //设置数据源
        mpg.setDataSource(dsc);

        //3、包的配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.joe.kuaishou");
        pc.setEntity("bean");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");
        mpg.setPackageInfo(pc);

        //4、策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 设置要映射的表名
        strategy.setInclude("anchor_info","anchor_live_info","anchor_live_info_temp"
            //    "sys_menu","sys_role","sys_role_menu","sys_user","sys_user_role"
        );
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 自动lombok；
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);
        //执行
        mpg.execute();
    }
}
