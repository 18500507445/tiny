package com.tiny.example.web.dto;

import com.tiny.common.annotation.ValidGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

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

    //此处标识分组后，controller接口层， @Validated({ValidGroup.All.class}会进行分组校验
    @NotNull(message = "ID不能为null", groups = {ValidGroup.All.class})
    private String id;

    private String name;
}
