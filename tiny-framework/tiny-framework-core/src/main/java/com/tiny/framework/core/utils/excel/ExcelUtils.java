package com.tiny.framework.core.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author: wzh
 * @description: EasyExcel工具类
 * @date: 2023/12/08 11:32
 * @see <a href="https://blog.csdn.net/weixin_70506521/article/details/132011693">关闭流问题</a>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j(topic = "tiny-framework-core ==> ExcelUtils")
public final class ExcelUtils {

    /**
     * easyExcel导出
     *
     * @param response  http响应
     * @param dataList  数据内容
     * @param fileName  文件名
     * @param sheetName sheet名
     * @param clazz     导出对象类
     */
    public static <T> void write(HttpServletResponse response, List<T> dataList, String fileName, String sheetName, Class<T> clazz) {
        try {
            // 开始导出
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ExcelTypeEnum.XLSX.getValue());
            response.setCharacterEncoding("UTF-8");
            // easyExcel 导出完成自动关闭流，也是可以再手动关一次的（官网这么写的）
            EasyExcel.write(response.getOutputStream(), clazz).excelType(ExcelTypeEnum.XLSX).sheet(sheetName).doWrite(dataList);
        } catch (IOException e) {
            log.error("导出失败异常", e);
        }
    }

    /**
     * 将列表以 Excel 响应给前端
     *
     * @param response  响应
     * @param filename  文件名
     * @param sheetName Excel sheet 名
     * @param head      Excel head 头
     * @param data      数据列表哦
     * @param <T>       泛型，保证 head 和 data 类型的一致性
     */
    public static <T> void write(HttpServletResponse response, String filename, String sheetName,
                                 Class<T> head, List<T> data) throws IOException {
        // 输出 Excel
        EasyExcel.write(response.getOutputStream(), head)
                // 交给工具，自动关闭，有问题在手动吧
                .autoCloseStream(true)
                // 基于 column 长度，自动适配。最大 255 宽度
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet(sheetName).doWrite(data);
        // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
    }

    /**
     * 读取 Excel 文件
     * 官方：// 不要自动关闭，交给 Servlet 自己处理
     *
     * @param file 文件
     * @param head 头
     * @param <T>  泛型
     * @return 列表
     */
    public static <T> List<T> read(MultipartFile file, Class<T> head) throws IOException {
        return EasyExcel.read(file.getInputStream(), head, null)
                .autoCloseStream(true)
                .doReadAllSync();
    }


}
