package com.lyx.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lyx.member.entity.Area;
import com.lyx.member.entity.vo.AreaVo;
import com.lyx.member.mapper.AreaMapper;
import com.lyx.member.service.AreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 省市区地区信息 服务实现类
 * </p>
 *
 * @author xhj
 * @since 2023-03-25 09:42:34
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {

    /**
     * 省市区地区信息
     * @return
     */
    @Override
    @Cacheable(value = {"areaVo"},key = "'AreaVo'", sync = true)
    public List<AreaVo> areaList() {
        // 查出所有信息
        List<Area> areaList = this.list();
        List<AreaVo> areaVos = areaList.stream().map(area -> {
            AreaVo areaVo = new AreaVo();
            BeanUtils.copyProperties(area, areaVo);
            return areaVo;
        }).collect(Collectors.toList());
        List<AreaVo> areaVoList = areaVos.stream().filter(area -> area.getLevel() == 1).map(areaVo -> {
            areaVo.setAreaList(getListLevel(areaVo, areaVos));
            return areaVo;
        }).collect(Collectors.toList());

        return areaVoList;
    }

    private List<AreaVo> getListLevel(AreaVo areaVo, List<AreaVo> areaList) {
        List<AreaVo> list = new ArrayList<>();
        for (AreaVo vo : areaList) {
            if (vo.getParentId().equals(areaVo.getId())){
                list.add(vo);
            }
        }
        if (list!=null){
            return list.stream().map(area -> {
                area.setAreaList(this.getListLevel(area,areaList));
                return area;
            }).collect(Collectors.toList());
        }

        return null;
    }

}
