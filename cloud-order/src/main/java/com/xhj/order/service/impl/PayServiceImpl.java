package com.xhj.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.order.entity.Order;
import com.xhj.order.entity.Pay;
import com.xhj.order.entity.req.OrderListPageReq;
import com.xhj.order.entity.vo.OrderListVo;
import com.xhj.order.entity.vo.PayVo;
import com.xhj.order.feign.MemberFeignService;
import com.xhj.order.mapper.PayMapper;
import com.xhj.order.service.OrderAddrService;
import com.xhj.order.service.OrderService;
import com.xhj.order.service.PayService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author xhj
* @description 针对表【oms_pay】的数据库操作Service实现
* @createDate 2023-09-26 14:39:50
*/
@Service
public class PayServiceImpl extends ServiceImpl<PayMapper, Pay> implements PayService {

    @Autowired
    private MemberFeignService memberFeignService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderAddrService orderAddrService;

    /**
     * 保存支付信息
     * @param pay
     */
    @Override
    public void savePay(Pay pay) {
        pay.setBizPayNo(mzregNo());
        pay.setId(null);
        pay.setPayStatus(1L);
        baseMapper.savePay(pay);
    }

    /**
     * 生成支付流水号
     */
    @Override
    public String mzregNo(){
        int count = baseMapper.count();
        int i = 10000 + count +1;
        String s1 = i+"";
        String substring = s1.substring(1);
        String s = "GH"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))+ substring;
        return s;
    }

    /**
     * 修改支付状态
     * @param i
     */
    @Override
    public void updateState(String orderSn, int i) {
        baseMapper.updataState(orderSn,i);
    }

    /**
     * 分页查询
     * @param req
     * @return
     */
    @Override
    public PageUtils<PayVo> getPayPageList(OrderListPageReq req) {
        // 查询所有已支付的订单
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Order::getIsPayed,1);
        List<Order> orders = orderService.list(queryWrapper);
        // 分页查询支付信息
        Page<Pay> page = new Page<>(req.getPageNo(),req.getPageSize());
        LambdaQueryWrapper<Pay> wrapper = Wrappers.lambdaQuery();
        Page<Pay> payPage = page(page, wrapper);
        // 最终操作
        List<PayVo> payVoList = payPage.getRecords().stream().map(pay -> {
            PayVo payVo = new PayVo();
            BeanUtils.copyProperties(pay, payVo);
            // 保存支付方式
            for (Order order : orders) {
                if (order.getOrderSn().equals(pay.getOrderSn())) {
                    payVo.setPayId(order.getIsPayed());
                }
            }
            return payVo;
        }).collect(Collectors.toList());
        // 商品名称不为空
        // 买家不为空
        // 卖家不为空
        // 订单状态不为空
        PageUtils build = PageUtils.build(payPage);
        build.setList(payVoList);
        return build;
    }
}




