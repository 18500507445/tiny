package com.tiny.common.generator;

import com.baomidou.mybatisplus.annotation.IdType;
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
     * @see <a href="https://baomidou.com/pages/981406/#%E7%AD%96%E7%95%A5%E9%85%8D%E7%BD%AE-strategyconfig">官方配置文档</a>
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
                        // 指定时间格式
                        .commentDate("yyyy/MM/dd HH:mm")
                        //禁止打开输出目录
                        .disableOpenDir())
                .packageConfig(builder -> builder
                        // 设置父包名
                        .parent(getPackageName())
                        // 设置mapper路径
                        .mapper("repository.mapper")
                        // 设置实体路径
                        .entity("repository.entity")
                        // 设置Controller路径
                        .controller("web.controller")
                        // 设置父包模块名
//                        .moduleName("system")
                        // 设置mapperXml生成路径
                        .pathInfo(Collections.singletonMap(OutputFile.xml, getMapperPath())))
                .strategyConfig(builder -> builder
                        // 设置需要生成的表名
                        .addInclude(getTableName())


                        // 实体类策略
                        .entityBuilder()
                        // 开启lombok
                        .enableLombok()
                        // 开启链式模型
                        .enableChainModel()
                        // 开启生成实体时生成字段注解
                        .enableTableFieldAnnotation()
                        // 关闭序列话
                        .disableSerialVersionUID()
                        // 主键自增
                        .idType(IdType.AUTO)
                        //开启覆盖已有文件
                        .enableFileOverride()

                        //service策略
                        .serviceBuilder()
                        .enableFileOverride()
                        //转换service、impl文件名称
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImpl")

                        // mapper策略
                        .mapperBuilder()
                        // 添加mapper注解
                        .mapperAnnotation(org.apache.ibatis.annotations.Mapper.class)
                        .enableFileOverride()

                        // controller策略
                        .controllerBuilder()
                        // 自定义继承父类
                        .superClass("com.tiny.common.core.result.BaseController")
                        // 开启restController
                        .enableRestStyle()
                        .enableFileOverride()
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
