package top.conanan.shiroweb;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CodeGenerator {

    /**
     * 以下注释掉的其值都是该属性的默认值
     */
    @Test
    public void generate(){
        String parentName = "top.conanan";
        // 模块名称，其实不是 IDEA 中的 Model
        String moduleName = "shiroweb";
        // 要生成的表
        String[] tableNamesInclude = {
                "sys_user"/*,
                "sys_permission",
                "sys_role",
                "sys_role_permission",
                "sys_user_role"*/
        };
        String projectPath = System.getProperty("user.dir");// 在 main 中相对于 Project，若在 @Test 中，则相对于 Module。推荐 @Test 中执行


        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 1 全局策略配置
        GlobalConfig gc = new GlobalConfig();
        gc.setAuthor("conanan")
                // .setIdType(IdType.ASSIGN_ID)// 主键生成方式。此处选择雪花算法ID，可为 String 或 Long。一般跟随全局中配置
                // .setSwagger2(true)// 实体属性 Swagger2 注解
                .setActiveRecord(true)// AR 模式
                .setBaseResultMap(true)// BaseResultMap
                .setBaseColumnList(true)// BaseColumnList
                // .setEnableCache(true)// XML 中开启二级缓存
                .setOutputDir(projectPath + "/src/main/java")// 若是在 main() 中，需要添加 Project 路径
                .setFileOverride(false)// 覆盖文件，一般不覆盖
                .setOpen(false);// 是否打开输出目录
        mpg.setGlobalConfig(gc);


        // 2 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(parentName)
                .setModuleName(moduleName)// 模块名，可以放在 parent 中，但不严谨
                .setEntity("domain");// 其他如 mapper 改为 dao，与此类似就不配置了
        mpg.setPackageInfo(pc);


        // 3 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/study-shiro?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        // dsc.setDbType(DbType.MYSQL);// 数据库类型，不用设置，会根据 DriverName 自动检测
        dsc.setUsername("root");
        dsc.setPassword("123456");
        // dsc.setSchemaName("public");// 数据库 schema name，例如 PostgreSQL 可设置为 public。不懂？
        ITypeConvert myTypeConvert = new MySqlTypeConvert() {
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                // int 和 bigint 都转为 Long 类型，除了 tinyint(1) 是Boolean
                if (fieldType.toLowerCase().contains("int") && !fieldType.toLowerCase().contains("tinyint(1)")) {
                    return DbColumnType.LONG;
                }
                return super.processTypeConvert(globalConfig, fieldType);
            }
        };
        dsc.setTypeConvert(myTypeConvert);// 数据库类型和 Java 类型转换，不同数据库重写不同的类
        mpg.setDataSource(dsc);


        // 4 数据库表、字段配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(false);// 是否大写命名
        // strategy.setSkipView(false);// 是否跳过视图
        strategy.setNaming(NamingStrategy.underline_to_camel)// 数据库表映射到实体的命名策略
                .setColumnNaming(NamingStrategy.underline_to_camel)// 数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
                .setTablePrefix("tb_")// 表前缀。此处表示生成的实体去掉tb_前缀
                // strategy.setFieldPrefix("f_")// 字段前缀。此处表示生成的字段去掉f_前缀
                .setInclude(tableNamesInclude)// 需要包含的表名，允许正则表达式（与exclude二选一配置）
                // .setExclude()// 需要排除的表名，允许正则表达式
                // .setLikeTable()// 自3.3.0起，模糊匹配表名（与notLikeTable二选一配置）
                // .setNotLikeTable()// 自3.3.0起，自3.3.0起，模糊排除表名

                // .setEntityColumnConstant(false)// 实体类是否生成字段常量。生成的常量键值对和字段一样，没啥用？
                // .setEntityBuilderModel(false);// set后返回当前对象，可链式调用。也可使用 Lombok 的 @Accessors(chain = true)
                .setEntityLombokModel(true)// Lombok的@Data，@EqualsAndHashCode(callSuper = false)，@Accessors(chain = true)
                // .setEntityBooleanColumnRemoveIsPrefix(true)// Boolean类型字段是否移除is前缀。虽然阿里推荐移除，但是SpringMVC中已经可以自动映射。
                .setRestControllerStyle(true)// 生成 @RestController 控制器
                .setControllerMappingHyphenStyle(true)// 驼峰转连字符。stackoverflow 和 github 的 RESTApi 采用该脊椎命名法，不是驼峰或蛇形
                .setEntityTableFieldAnnotationEnable(true)// 是否生成实体时，生成表、字段注解，即@TableName("sys_permission")、 @TableId("id") 或 @TableField("type")
                // .setVersionFieldName("version")// 乐观锁属性名称，暂时不用

                .setLogicDeleteFieldName("is_deleted")// 逻辑删除字段
                .setLogicDeleteFieldName("is_public");

        // .setSuperEntityClass();// 自定义继承的Entity类全称，带包名。你自己的父类实体，没有就不用设置。
        // .setSuperEntityColumns();// 自定义基础的Entity类，公共字段，没有就不用设置。
        // .setSuperMapperClass();// 自定义继承的Mapper类全称，带包名
        // .setSuperServiceClass();// 自定义继承的Service类全称，带包名
        // .setSuperServiceImplClass();// 自定义继承的ServiceImpl类全称，带包名
        // .setSuperControllerClass();// 自定义继承的Controller类全称，带包名

        // strategy.setEnableSqlFilter(true);// 启用sql过滤，3.3.1开始，关闭之后likeTable与notLikeTable将失效，include和exclude将使用内存过滤

        // 填充策略
        List<TableFill> tableFillList = new ArrayList<>();
        tableFillList.add(new TableFill("create_time", FieldFill.INSERT));
        tableFillList.add(new TableFill("update_time", FieldFill.INSERT_UPDATE));
        strategy.setTableFillList(tableFillList);

        mpg.setStrategy(strategy);


        // 5 模版配置
        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        templateConfig.setXml(null);// mapper xml 模板
        mpg.setTemplate(templateConfig);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());


        // 6 注入配置，可注入自定义参数等操作以实现个性化操作
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        cfg.setFileOutConfigList(focList);// 自定义输出文件。配置 FileOutConfig 指定模板文件、输出文件达到自定义文件生成目的
        mpg.setCfg(cfg);


        mpg.execute();
    }
}
