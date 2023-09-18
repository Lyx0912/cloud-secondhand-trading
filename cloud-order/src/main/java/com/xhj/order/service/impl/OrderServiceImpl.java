package com.xhj.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyx.common.base.entity.dto.GoodsDTO;
import com.lyx.common.base.entity.dto.GoodsEsDTO;
import com.lyx.common.base.entity.dto.MemberAddrDTO;
import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.order.entity.Order;
import com.xhj.order.entity.OrderAddr;
import com.xhj.order.entity.req.OrderListPageReq;
import com.xhj.order.entity.req.OrderPaymentReq;
import com.xhj.order.entity.req.OrderReq;
import com.xhj.order.entity.vo.OrderAddrVo;
import com.xhj.order.entity.vo.OrderListVo;
import com.xhj.order.entity.vo.OrderVo;
import com.xhj.order.feign.GoodsFeignService;
import com.xhj.order.feign.MemberFeignService;
import com.xhj.order.feign.StorageFeignService;
import com.xhj.order.mapper.OrderMapper;
import com.xhj.order.service.OrderAddrService;
import com.xhj.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: xhj
 * @Date: 2023/04/24/13:51
 * @Description:
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Order> implements OrderService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderAddrService orderAddrService;

    @Autowired
    private GoodsFeignService goodsFeignService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private MemberFeignService memberFeignService;
    @Autowired
    private StorageFeignService storageFeignService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public PageUtils<OrderListVo> getOrderPageList(OrderListPageReq req) {
        Page<OrderListVo> page = new Page<>(req.getPageNo(), req.getPageSize());
        Page<Order> orderPage = new Page<>(req.getPageNo(), req.getPageSize());
        // TODO 完善查询条件
        List<Order> orders = this.page(orderPage).getRecords();
//        List<Order> orders = this.baseMapper.getOrderList();
        List<OrderListVo> orderListVos = orders.stream().map(order -> {
            OrderListVo orderListVo = new OrderListVo();
            BeanUtils.copyProperties(order, orderListVo);
            orderListVo.setBuyer(order.getMemberUsername());
            // 远程调用查询卖家 goods_id
            GoodsDTO goodsDTO = goodsFeignService.orderInfo(order.getGoodsId());
            if (goodsDTO!=null){
                orderListVo.setId(order.getGoodsId());
                orderListVo.setSeller(goodsDTO.getSeller());
                orderListVo.setState(order.getState());
                orderListVo.setDefaultImg(goodsDTO.getDefaultImg());
                orderListVo.setCategoryName(goodsDTO.getCategoryName());
                orderListVo.setGoodsName(goodsDTO.getName());
            }
            return orderListVo;
        }).collect(Collectors.toList());
        page.setRecords(orderListVos);
        PageUtils build = PageUtils.build(page);
        build.setPageSize(orderPage.getSize());
        build.setTotal(orderPage.getTotal());
        return build;
    }

    @Transactional
    @Override
    public OrderAddrVo getOrder(OrderReq req) {
        // 生成订单号
        long id = IdWorker.getId();
        log.info("id{}",id);

        // 扣除库存 获取信号量
        RSemaphore semaphore = redissonClient.getSemaphore("goods:"+req.getGoodsId());
        boolean b = semaphore.tryAcquire();
        if (b){
            try{
                // 扣除库存
                storageFeignService.updateStorage(req.getGoodsId());
                R byId = memberFeignService.getById(req.getAddrId());
                OrderAddrVo orderAddrVo = new OrderAddrVo();
                if (byId.getData()!=null){
                    String s = JSONObject.toJSONString(byId.getData());
                    MemberAddrDTO memberAddrDTO = JSON.parseObject(s, MemberAddrDTO.class);

                    // 保存收货地址
                    OrderAddr orderAddr = new OrderAddr();
                    BeanUtils.copyProperties(memberAddrDTO,orderAddr);
                    orderAddr.setCreateTime(LocalDateTime.now());
                    orderAddrService.save(orderAddr);

                    // 生成订单
                    Order order = new Order();
                    order.setOrderSn(id+"");
                    order.setGoodsId(req.getGoodsId());
                    order.setMemberId(req.getMemberId());
                    order.setOrderAddrId(orderAddr.getId());
                    order.setPayAmount(req.getPrice());
                    order.setTotalAmount(req.getPrice());
                    order.setCreateTime(LocalDateTime.now());
                    order.setDeleteStatus(0);
                    order.setState(0);
                    order.setSourceType(0);
                    order.setConfirmStatus(0);
                    order.setIsPayed(0);
                    this.baseMapper.insert(order);
                    orderAddrVo.setMemberAddrDTO(memberAddrDTO);

                    // 把商品id和地址id保存到redis
                    // 保存订单号到redis 30分钟
                    String name = "order:"+req.getMemberId()+":"+id;
                    redisTemplate.opsForValue()
                            .set(name, req.getGoodsId()+"."+orderAddr.getId(),30L, TimeUnit.MINUTES);
                }
                orderAddrVo.setId(id+"");
                orderAddrVo.setGoodsId(req.getGoodsId());
                // 订单创建成功 发送消息给rabbitMq
                rabbitTemplate.convertAndSend("secondhandOrder-event-exchange","secondhandOrder-create-order",orderAddrVo);
                return orderAddrVo;
            }catch (Exception e){
                semaphore.release();
            }
        }
        return null;
    }

    /**
     * 查询订单
     * @param orderSn
     * @return
     */
    @Override
    public OrderVo getOrderSn(Long orderSn,Long memberId) {
        // 查询是否有该订单
        String name = "order:"+memberId+":"+orderSn;
        String s = redisTemplate.opsForValue().get(name);
        if (s!=null){
            String replace = s.replace("\"", "");
            String[] strings = replace.split("\\.");
            Long goodsId = Long.parseLong(strings[0]);
            Long orderAddrId = Long.parseLong(strings[1]);
            GoodsDTO goodsDTO = goodsFeignService.orderInfo(goodsId);
            OrderAddr orderAddr = orderAddrService.getById(orderAddrId);
            OrderVo orderVo = new OrderVo();
            orderVo.setOrderAddr(Arrays.asList(orderAddr));
            orderVo.setGoodsDTO(goodsDTO);
            return orderVo;
        }
        return null;
    }

    /**
     * rabbitmq取消订单
     */
    @Transactional
    @Override
    public void rabbitMqDeleteOrderByOrderId(OrderPaymentReq req) {
        // 先判断是否已经付款或取消
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Order::getOrderSn,req.getOrderSn());
        Order order = this.getOne(queryWrapper);
        if (order!=null){
            // 如果是待付款状态则取消
            if (order.getState()==0||order.getState()==null){
                deleteOrder(order,req);
            }
        }
    }
    /**
     * 取消订单
     */
    @Transactional
    @Override
    public void deleteOrderByOrderId(OrderPaymentReq req) {
        // 先判断是否已经付款或取消
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Order::getOrderSn,req.getOrderSn());
        Order order = this.getOne(queryWrapper);
        if (order!=null){
            // 如果是待付款状态则取消
            if (order.getState()==0&&order.getState()==1){
                deleteOrder(order,req);
            }
        }
    }
    public void deleteOrder(Order order,OrderPaymentReq req){
        RSemaphore semaphore = redissonClient.getSemaphore("goods:"+req.getGoodsId());
        try{
            LambdaUpdateWrapper<Order> wrapper = Wrappers.lambdaUpdate();
            wrapper.set(Order::getState,4).set(Order::getFinallyTime,LocalDateTime.now())
                    .set(Order::getCloseType,4)
                    .eq(Order::getOrderSn,req.getOrderSn());
            this.update(wrapper);
            // 删除redis里面的订单信息
            String name = "order:"+req.getMemberId()+":"+req.getOrderSn();
            redisTemplate.opsForValue().getOperations().delete(name);
            // 回退库存
            storageFeignService.addStorageTotal(req.getGoodsId());
            semaphore.release();
        }catch (Exception e){
            semaphore.tryAcquire();
        }
    }

    /**
     * 付款
     * @param req
     */
    @Override
    public String payment(OrderPaymentReq req) {
        // 先判断是否存在
        OrderVo orderSn = getOrderSn(req.getOrderSn(), req.getMemberId());
        if (orderSn==null){
            return "0";
        }
        LambdaUpdateWrapper<Order> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Order::getOrderSn,req.getOrderSn())
                .set(Order::getPayType,req.getPayType())
                .set(Order::getState,1)
                .set(Order::getPayTime,LocalDateTime.now())
                .set(Order::getIsPayed,1);
        this.update(wrapper);
        // 删除redis里面的订单信息
        String name = "order:"+req.getMemberId()+":"+req.getOrderSn();
        Boolean aBoolean = redisTemplate.opsForValue().getOperations().delete(name);
        if (!aBoolean){
            return "0";
        }
        return "1";
    }
}
