package com.tiny.example;

import cn.hutool.json.JSONUtil;
import com.tiny.example.repository.entity.ExampleEntity;
import com.tiny.example.repository.mapper.ExampleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

/**
 * @author: wzh
 * @description: 样例测试类
 * @date: 2023/08/29 14:22
 */
@ActiveProfiles("local")
@SpringBootTest
public class ExampleTest {

    @Resource
    private ExampleMapper exampleMapper;

    @Test
    public void demoA() {
        ExampleEntity example = exampleMapper.selectById(1911753445593890817L);
        System.out.println("json = " + JSONUtil.toJsonStr(example));
    }

}
