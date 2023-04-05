package com.lyx.common.web.utils;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 黎勇炫
 * @date 2023年04月05日 14:07
 */
public class ResponseUtils {

    public static HttpServletResponse excelResponse(HttpServletResponse response){
        response.setContentType("application/vnd.ms-excel");// 设置文本内省
        response.setCharacterEncoding("utf-8");// 设置字符编码
        response.setHeader("Content-disposition", "attachment;filename=demo.xlsx"); // 设置响应头
        return response;
    }
}
