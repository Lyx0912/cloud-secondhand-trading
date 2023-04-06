package com.lyx.common.mp.utils;

import lombok.Data;
import com.baomidou.mybatisplus.extension.plugins.pagination.*;
import java.util.List;
/**
 * @author： 黎勇炫
 */
@Data
public class PageUtils<T> {
    private Long total = 0L;
    private Long pageNo;
    private Long pageSize;
    private List<T> list;

    public static PageUtils build(Page page){
        PageUtils pageUtils = new PageUtils<>();
        pageUtils.setList(page.getRecords());
        pageUtils.setPageNo(page.getCurrent());
        pageUtils.setTotal(page.getTotal());
        return pageUtils;
    }
}
