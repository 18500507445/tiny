package com.tiny.framework.core.serializer;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.data.WriteCellData;

/**
 * @author: wzh
 * @description: 阿里easyExcel使用，将1、0转换成是，否
 * @date: 2025/12/30 16:20
 */
public class BoolConverter implements Converter<Integer> {

    @Override
    public WriteCellData<String> convertToExcelData(WriteConverterContext<Integer> context) {
        Integer value = context.getValue();
        return new WriteCellData<>(ObjectUtil.equals(0, value) ? "否" : "是");
    }

}
