package com.tiny.example.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: wzh
 * @description: 样例请求DTO
 * @date: 2023/11/29 15:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ExampleDTO {

    private String id;

    private String name;
}
