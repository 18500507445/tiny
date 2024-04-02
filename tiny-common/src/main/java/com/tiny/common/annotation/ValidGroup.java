package com.tiny.common.annotation;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

/**
 * @author: wzh
 * @description: 分组校验-定义分组
 * @date: 2024/02/19 09:57
 */
public final class ValidGroup {

    // 新增使用(配合spring的@Validated功能分组使用)
    public interface Insert extends Default {
    }

    // 更新使用
    public interface Update extends Default {
    }

    // 查询使用
    public interface Select extends Default {
    }

    // 删除使用
    public interface Delete extends Default {
    }

    // 属性必须有这4个分组的才验证
    @GroupSequence({Insert.class, Update.class, Select.class, Delete.class})
    public interface All {

    }
}
