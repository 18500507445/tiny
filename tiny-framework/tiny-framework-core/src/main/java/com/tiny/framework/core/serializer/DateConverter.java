package com.tiny.framework.core.serializer;

import cn.hutool.core.date.DatePattern;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.data.WriteCellData;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 阿里easyExcel使用，将时间格式转化为yyyy-MM-dd HH:mm:ss
 *
 * @author wzh
 */
public class DateConverter implements Converter<Date> {

    @Override
    public Class<Date> supportJavaTypeKey() {
        return Date.class;
    }

    @Override
    public WriteCellData<String> convertToExcelData(WriteConverterContext<Date> context) {
        Date date = context.getValue();
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        return new WriteCellData<>(sdf.format(date));
    }
}