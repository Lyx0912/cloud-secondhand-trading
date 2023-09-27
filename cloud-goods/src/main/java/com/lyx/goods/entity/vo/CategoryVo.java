package com.lyx.goods.entity.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @Author: xhj
 * @Date: 2023/09/26/21:37
 * @Description:
 */
@Data
public class CategoryVo {
    private Long id;

    /**
     * 分类名称
     */
    private String name;
}
