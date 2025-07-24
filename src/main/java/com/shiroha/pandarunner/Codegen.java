package com.shiroha.pandarunner;

import com.alibaba.druid.pool.DruidDataSource;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;

public class Codegen {

    public static void main(String[] args) {
        //配置数据源
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/pandarunner");
        dataSource.setUsername("postgres");
        dataSource.setPassword("1234");

        //创建配置内容，两种风格都可以。
        GlobalConfig globalConfig = createGlobalConfig();

        //通过 datasource 和 globalConfig 创建代码生成器
        Generator generator = new Generator(dataSource, globalConfig);

        //生成代码
        generator.generate();
    }

    public static GlobalConfig createGlobalConfig() {
        //创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();

        //设置根包
        globalConfig.setBasePackage("com.shiroha.pandarunner.domain");

        //设置表前缀和只生成哪些表
        globalConfig.setTablePrefix("t_");
        globalConfig.setGenerateTable("t_user", "t_user_address", "t_merchant",
                "t_merchant_business_hours", "t_product_category", "t_product",
                "t_spec_group", "t_spec_option", "t_order", "t_order_item",
                "t_delivery_rider", "t_delivery_order", "t_payment_record",
                "t_review", "t_review_product", "t_review_reply", "t_admin_user",
                "t_cart", "t_cart_item", "t_announcement");

        //设置生成 entity 并启用 Lombok
        globalConfig.setEntityGenerateEnable(true);
        globalConfig.setEntityWithLombok(true);
        //设置项目的JDK版本，项目的JDK为14及以上时建议设置该项，小于14则可以不设置
        globalConfig.setEntityJdkVersion(21);

        //设置生成 mapper
        globalConfig.setMapperGenerateEnable(true);
        globalConfig.enableMapperXml();
        globalConfig.enableService();
        globalConfig.enableServiceImpl();
        globalConfig.enableController();
        globalConfig.enableTableDef();
        globalConfig.setMapperAnnotation(true);

        return globalConfig;
    }
}
