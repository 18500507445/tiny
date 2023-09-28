package com.tiny.example;

import cn.hutool.core.util.StrUtil;
import com.tiny.common.generator.AbstractGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;

/**
 * @author: wzh
 * @description example项目生成器
 * @date: 2023/08/25 17:40
 */
@ActiveProfiles("local")
@SpringBootTest
public class ExampleGeneratorTest extends AbstractGenerator {

    @Override
    protected String getUrl() {
        return "jdbc:mysql://xx.xxx.xxx.xxx:3306/xxx?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
    }

    @Override
    protected String getUserName() {
        return "root";
    }

    @Override
    protected String getPassword() {
        return "root";
    }

    @Override
    protected String[] getTableName() {
        return new String[]{"student", "tb_account", "user"};
    }

    @Override
    protected String getPackageName() {
        return this.getClass().getPackage().getName();
    }

    public String getModelPath() {
        String path = Objects.requireNonNull(this.getClass().getResource("")).getPath();
        List<String> split = StrUtil.split(path, "target");
        return split.get(0);
    }

    @Override
    protected String getPath() {
        return getModelPath() + "src/main/java";
    }

    @Override
    protected String getMapperPath() {
        return getModelPath() + "src/main/resources/mapper";
    }

    /**
     * 生成代码测试类
     */
    @Test
    public void mybatisPlusGeneratorTest() {
        this.mybatisPlus();
    }

}
