package com.tiny.framework.core;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.ContentType;

/**
 * @author: wzh
 * @description: 通用常量类
 * @date: 2023/08/29 11:02
 */
public class Constants {

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    public static final String SLASH = "/";

    public static final String COMMA = ",";

    public static final String SEMICOLON = ";";

    public static final String COLON = ":";

    public static final String AT = "@";

    public static final String ZERO = "0";

    public static final String SUCCESS = "SUCCESS";

    public static final String FAIL = "FAIL";

    public static final String JSON = ContentType.build(ContentType.JSON, CharsetUtil.CHARSET_UTF_8);

    public static final boolean IS_OPEN = true;

    public static final String AUTHORIZATION = "Authorization";

    public static final String USER_CONTEXT = "userContext";

    public static final String START_TIME = "startTime";


}
