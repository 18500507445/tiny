package com.tiny.common.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @author: wzh
 * @description 代码生成器抽象类
 * @date: 2023/08/29 17:31
 */
public abstract class AbstractGenerator {

    /**
     * 获取数据库连接
     */
    protected abstract String getUrl();

    /**
     * 获取用户名
     */
    protected abstract String getUserName();

    /**
     * 获取密码
     */
    protected abstract String getPassword();

    /**
     * 表名
     */
    protected abstract String[] getTableName();

    /**
     * 包名
     * com.tiny.common.generator
     */
    protected abstract String getPackageName();

    /**
     * 输出目录
     * tiny-common/tiny-common-generator/src/main/java
     */
    protected abstract String getPath();

    /**
     * mapper文件路径
     * tiny-common/tiny-common-generator/src/main/resources
     */
    protected abstract String getMapperPath();

    /**
     * mybatisplus代码生成器
     */
    protected void mybatisPlus() {
        FastAutoGenerator.create(getUrl(), getUserName(), getPassword())
                .globalConfig(builder -> builder
                        // 设置作者
                        .author(System.getProperty("user.name"))
                        // 开启 swagger 模式
//                        .enableSwagger()
                        // 指定输出目录
                        .outputDir(getPath())
                        //禁止打开输出目录
                        .disableOpenDir())
                .packageConfig(builder -> builder
                        // 设置父包名
                        .parent(getPackageName())
                        // 设置父包模块名
//                        .moduleName("system")
                        // 设置mapperXml生成路径
                        .pathInfo(Collections.singletonMap(OutputFile.xml, getMapperPath())))
                .strategyConfig(builder -> builder
                                // 设置需要生成的表名
                                .addInclude(getTableName())
                        // 设置过滤表前缀
//                        .addTablePrefix("t_", "c_")
                )
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    /**
     * 普通的代码生成器，带有基础的增删改查和xml文件
     */
    protected void mybatis() {

    }
}
