package com.lyx.member.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lyx.member.entity.Area;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/09/18/19:25
 * @Description:
 */
@Data
public class AreaVo {
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 创建时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
//    private LocalDateTime createTime;

    /**
     * 更新时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
//    private LocalDateTime updateTime;

    /**
     * 地址
     */
    private String areaName;

    /**
     * 上级地址
     */
    private Long parentId;

    /**
     * 等级（从1开始）
     */
    private Integer level;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<AreaVo> areaList;

    public AreaVo(){}
}
