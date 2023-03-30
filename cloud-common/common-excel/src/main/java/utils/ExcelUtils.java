package utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
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
        EasyExcel.write(outputStream, clazz)
                .excelType(ExcelTypeEnum.XLSX)
                .autoCloseStream(true)
                .sheet(sheetName)
                .needHead(true)
                .doWrite(data); //用io流来写入数据
    }
}
