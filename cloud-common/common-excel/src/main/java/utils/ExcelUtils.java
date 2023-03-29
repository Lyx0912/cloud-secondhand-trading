package utils;

import com.alibaba.excel.EasyExcel;
import org.apache.poi.ss.formula.functions.T;

import java.io.OutputStream;
import java.util.List;

/**
 * @author 黎勇炫
 * @date 2023年03月28日 14:45
 */
public class ExcelUtils<T> {

    /**
     * 导出excel核心方法
     *
     * @param
     */
    public static void export(OutputStream outputStream, Class clazz, List data,String sheetName){
        //给定导出实体类
        EasyExcel.write(outputStream, clazz).sheet(sheetName).doWrite(data);
    }
}
