package com.lyx.member.service;

import com.lyx.member.entity.Area;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyx.member.entity.vo.AreaVo;

import java.util.List;

/**
 * <p>
 * 省市区地区信息 服务类
 * </p>
 *
 * @author xhj
 * @since 2023-03-25 09:42:34
 */
public interface AreaService extends IService<Area> {

    /**
     * 省市区地区信息
     * @return
     */
    List<AreaVo> areaList();
}
