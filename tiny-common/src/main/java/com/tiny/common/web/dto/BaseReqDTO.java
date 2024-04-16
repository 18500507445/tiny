package com.tiny.common.web.dto;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: wzh
 * @description: 请求DTO基类
 * @date: 2023/12/04 19:22
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BaseReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码，默认第一页
     */
    @Builder.Default
    private Integer pageIndex = 1;

    /**
     * 每页显示大小，默认20条
     */
    @Builder.Default
    private Integer pageSize = 20;

    // 特殊处理字段
    private String params = "";

    // 分页关键字 特殊处理
    private String keyword;

    // 获取authorization参数
    @JsonProperty("Authorization")
    @JSONField(name = "Authorization")
    private String authorization;

    // 起始时间，页面传字符串后端转Date
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    // 结束时间，页面传字符串后端转Date
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    // url解码处理
    public void setParams(String params) {
        this.params = URLUtil.decode(params);
    }

    public Integer getPageIndex() {
        Integer pageIndex = processInteger("pageIndex");
        return null == pageIndex ? this.pageIndex : pageIndex;
    }

    public Integer getPageSize() {
        Integer pageSize = processInteger("pageSize");
        return null == pageSize ? this.pageSize : pageSize;
    }

    public String getKeyword() {
        String keyword = processString("keyword");
        return StrUtil.isEmpty(keyword) ? this.keyword : keyword;
    }

    // 处理Integer类型
    private Integer processInteger(String key) {
        if (StrUtil.isNotBlank(this.params)) {
            String str = StrUtil.subBefore(this.params, "&", false);
            JSONObject jsonObject = JSONObject.parseObject(str);
            return jsonObject.getInteger(key);
        }
        return null;
    }

    // 处理String类型
    private String processString(String key) {
        if (StrUtil.isNotBlank(this.params)) {
            String str = StrUtil.subBefore(this.params, "&", false);
            JSONObject jsonObject = JSONObject.parseObject(str);
            return jsonObject.getString(key);
        }
        return null;
    }
}
