package com.xhj.order.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
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
import com.lyx.common.base.entity.dto.*;
import com.lyx.common.base.result.R;
import com.lyx.common.mp.utils.PageUtils;
import com.xhj.order.entity.Order;
import com.xhj.order.entity.OrderAddr;
import com.xhj.order.entity.req.OrderAddrMemberReq;
import com.xhj.order.entity.req.OrderListPageReq;
import com.xhj.order.entity.req.OrderPaymentReq;
import com.xhj.order.entity.req.OrderReq;
import com.xhj.order.entity.vo.*;
import com.xhj.order.feign.GoodsFeignService;
import com.xhj.order.feign.MemberFeignService;
import com.xhj.order.feign.StorageFeignService;
import com.xhj.order.mapper.OrderMapper;
import com.xhj.order.service.OrderAddrService;
import com.xhj.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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

    @Autowired
    private ThreadPoolExecutor executor;

    @Override
    public PageUtils<OrderListVo> getAdminOrderPageList(OrderListPageReq req) {
        Page<OrderListVo> page = new Page<>(req.getPageNo(), req.getPageSize());
        Page<Order> orderPage = new Page<>(req.getPageNo(), req.getPageSize());
        // 查询商品信息
        OrderGoodsPageReqDTO reqDTO = new OrderGoodsPageReqDTO();
        reqDTO.setName(req.getName());
        reqDTO.setSeller(req.getSeller());
        reqDTO.setPageSize(req.getPageSize());
        reqDTO.setPageNo(req.getPageNo());
        List<GoodsDTO> goodsDTOs = goodsFeignService.goodsPageList(reqDTO);
        // 查询的是商品名称和卖家
        AtomicInteger i = new AtomicInteger();
        AtomicInteger a = new AtomicInteger();
        AtomicInteger b = new AtomicInteger();
        if (req.getName()!=null||req.getSeller()!=null){
            List<OrderListVo> orderListVoList = new ArrayList<>();
            // 查询条件
            LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(req.getUid()!=null,Order::getMemberId,req.getUid())
                    .eq(req.getState()!=null,Order::getState,req.getState())
                    .like(req.getBuyer()!=null,Order::getMemberUsername,req.getBuyer());
            List<Order> orderss = this.list(wrapper);
            // 遍历查到的商品信息
            for (GoodsDTO goodsDTO : goodsDTOs) {
                // 过滤掉不符合条件的订单
                for (Order order : orderss) {
                    if (order.getGoodsId()==goodsDTO.getId()){
                        i.getAndAdd(1);
                        OrderListVo orderListVo = new OrderListVo();
                        BeanUtils.copyProperties(order, orderListVo);
                        orderListVo.setBuyer(order.getMemberUsername());
                        orderListVo.setId(order.getId());
                        orderListVo.setSeller(goodsDTO.getSeller());
                        orderListVo.setState(order.getState());
                        orderListVo.setDefaultImg(goodsDTO.getDefaultImg());
                        orderListVo.setCategoryName(goodsDTO.getCategoryName());
                        orderListVo.setGoodsName(goodsDTO.getName());
                        if (req.getPageNo()==1&&i.get()<=req.getPageSize()){
                            orderListVoList.add(orderListVo);
                            a.getAndAdd(1);
                        }
                        if (req.getPageNo()!=1&&i.get()>req.getPageSize()*(req.getPageNo()-1)){
                            orderListVoList.add(orderListVo);
                            b.getAndAdd(1);
                        }
                    }
                }
            }

            page.setRecords(orderListVoList);
            PageUtils build = PageUtils.build(page);
            build.setPageSize(Long.parseLong(orderListVoList.size()+""));
            build.setTotal(Long.parseLong(i.get()+""));
            return build;
        }else{
            // 查询条件
            LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(req.getUid()!=null,Order::getMemberId,req.getUid())
                    .eq(req.getState()!=null,Order::getState,req.getState())
                    .like(req.getBuyer()!=null,Order::getMemberUsername,req.getBuyer());
            List<Order> orders = this.page(orderPage,wrapper).getRecords();
            List<OrderListVo> orderListVos = orders.stream().map(order -> {
                OrderListVo orderListVo = new OrderListVo();
                BeanUtils.copyProperties(order, orderListVo);
                orderListVo.setBuyer(order.getMemberUsername());
                // 远程调用查询卖家 goods_id
                GoodsDTO goodsDTO = new GoodsDTO();
                if (order.getGoodsId()!=null){
                    goodsDTOs.forEach(goodsDTO1 -> {
                        if (goodsDTO1.getId()==order.getGoodsId()){
                            BeanUtils.copyProperties(goodsDTO1,goodsDTO);
                            return;
                        }
                    });
                }
                if (goodsDTO!=null){
                    orderListVo.setId(order.getId());
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
            build.setTotal(i.get()>0?i.get():orderPage.getTotal());
            return build;
        }

    }

    @Override
    public PageUtils<OrderListVo> getOrderPageList(OrderListPageReq req) {
        Page<OrderListVo> page = new Page<>(req.getPageNo(), req.getPageSize());
        Page<Order> orderPage = new Page<>(req.getPageNo(), req.getPageSize());
        // 查询条件
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(req.getUid()!=null,Order::getMemberId,req.getUid());
        List<Order> orders = this.page(orderPage,wrapper).getRecords();
//        List<Order> orders = this.baseMapper.getOrderList();
        List<OrderListVo> orderListVos = orders.stream().map(order -> {
            OrderListVo orderListVo = new OrderListVo();
            BeanUtils.copyProperties(order, orderListVo);
            orderListVo.setBuyer(order.getMemberUsername());
            // 远程调用查询卖家 goods_id
            GoodsDTO goodsDTO = null;
            if (order.getGoodsId()!=null){
                goodsDTO = goodsFeignService.orderInfo(order.getGoodsId());
            }
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
                    OrderAddrMemberReq orderAddrMemberReq = JSON.parseObject(s, OrderAddrMemberReq.class);

                    // 保存收货地址
                    OrderAddr orderAddr = new OrderAddr();
                    BeanUtils.copyProperties(orderAddrMemberReq,orderAddr);
                    orderAddr.setCreateTime(LocalDateTime.now());
                    orderAddr.setArea(orderAddrMemberReq.getAddrsPath()[orderAddrMemberReq.getAddrsPath().length-1]);
                    orderAddr.setProvince(orderAddrMemberReq.getAddrsPath()[0]);
                    orderAddr.setCity(orderAddrMemberReq.getAddrsPath()[1]);
                    orderAddrService.save(orderAddr);

                    // 生成订单
                    Order order = new Order();
                    order.setOrderSn(id+"");
                    order.setMemberUsername(req.getMemberName());
                    order.setGoodsId(req.getGoodsId());
                    order.setMemberId(req.getMemberId());
                    order.setOrderAddrId(orderAddr.getId());
                    order.setPayAmount(req.getPrice());
                    order.setTotalAmount(req.getPrice());
//                    order.setCreateTime(LocalDateTime.now());
                    order.setDeleteStatus(0);
                    order.setState(0);
                    order.setSourceType(0);
                    order.setConfirmStatus(0);
                    order.setIsPayed(0);
                    this.baseMapper.insert(order);
                    orderAddrVo.setMemberAddrDTO(null);

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

    @Override
    public OrderReleaseAddrsCountVo getCount(Long memberId) {
        OrderReleaseAddrsCountVo countVo = new OrderReleaseAddrsCountVo();
        // 查询用户订单数量
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtils.isNotEmpty(memberId.toString()),Order::getMemberId,memberId);
        int orderCount = count(wrapper);
        // 查询用户地址数量
        R count1 = memberFeignService.count(memberId);
        int orderAddrCount = Integer.parseInt(count1.getData().toString());
        // 查询用户发布商品数量 远程调用
        R count = goodsFeignService.count(memberId);
        int goodsCount = Integer.parseInt(count.getData().toString());
        countVo.setOrderCount(orderCount);
        countVo.setAddrsCount(orderAddrCount);
        countVo.setReleaseCount(goodsCount);
        return countVo;
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
            if (order.getState()==0||order.getState()==1){
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

    /**
     * 查询订单详情
     */
    @Override
    public OrderInfoVo getInfo(Long orderId) throws ExecutionException, InterruptedException {
        OrderInfoVo orderInfoVo = new OrderInfoVo();
        CompletableFuture<Order> supplyAsync = CompletableFuture.supplyAsync(() -> {
            // 查询订单信息
            Order order = getById(orderId);
            orderInfoVo.setOrder(order);
            return order;
        },executor);
        CompletableFuture<Void> thenAcceptAsync = supplyAsync.thenAcceptAsync(order -> {
            // 查询商品信息
            OrderGoodsDTO goodsEsDTO = goodsFeignService.orderGoodsInfo(order.getGoodsId());
            orderInfoVo.setOrderGoodsDTO(goodsEsDTO);
        }, executor);
        CompletableFuture<Void> thenAcceptAsync1 = supplyAsync.thenAcceptAsync(order -> {
            // 查询订单地址
            LambdaQueryWrapper<OrderAddr> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(OrderAddr::getId, order.getOrderAddrId());
            OrderAddr orderAddr = orderAddrService.getOne(wrapper);
            orderInfoVo.setOrderAddr(orderAddr);
        }, executor);
        //等到所有任务都完成
        CompletableFuture.allOf(supplyAsync,thenAcceptAsync,thenAcceptAsync1).get();
        return orderInfoVo;
    }

    /**
     * 安全信息获取
     */
    @Override
    public OrderSecureVo secure(Long memberId) throws ExecutionException, InterruptedException {
        OrderSecureVo orderSecureVo = new OrderSecureVo();
        // 查询上传商品信息
        CompletableFuture<List<Long>> supplyAsync = CompletableFuture.supplyAsync(() -> {
            List<Long> goodsById = goodsFeignService.getGoodsById(memberId);
            return goodsById;
        }, executor);
        CompletableFuture<Void> thenAcceptAsync = supplyAsync.thenAcceptAsync(goodsById -> {
            if (goodsById.size()>0){

                // 总销售额
                CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> {
                    BigDecimal salesTotal = baseMapper.getSalesTotal(goodsById);
                    orderSecureVo.setSalesTotal(salesTotal);
                }, executor);
                // 总购买量
                CompletableFuture<Void> runAsync1 = CompletableFuture.runAsync(() -> {
                    int purchasesTotal = baseMapper.getPurchases(goodsById);
                    orderSecureVo.setPurchasesTotal(new BigDecimal(purchasesTotal));
                }, executor);

                // 入账总金额
                CompletableFuture<Void> runAsync2 = CompletableFuture.runAsync(() -> {
                    BigDecimal recordedTotal = baseMapper.getRecordedTotal(goodsById);
                    orderSecureVo.setRecordedTotal(recordedTotal);
                    // 钱包
                    orderSecureVo.setPurse(recordedTotal);
                }, executor);
                // 入账记录
                CompletableFuture<Void> runAsync3 = CompletableFuture.runAsync(() -> {
                    List<OrderRecordedVo> orderRecordedVos = baseMapper.getOrderRecorded(goodsById);
                    orderSecureVo.setRecordedVos(orderRecordedVos);
                }, executor);
                try {
                    CompletableFuture.allOf(runAsync, runAsync1, runAsync2, runAsync3).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        CompletableFuture.allOf(supplyAsync,thenAcceptAsync).get();
        return orderSecureVo;
    }

    /**
     * 确认收货
     * @param orderSn
     */
    @Override
    public void confirm(String orderSn) {
        LambdaUpdateWrapper<Order> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Order::getOrderSn,orderSn)
                        .set(Order::getFinallyTime,LocalDateTime.now())
                                .set(Order::getState,3)
                                        .set(Order::getConfirmStatus,1)
                                                .set(Order::getSettledTime,LocalDateTime.now());
        update(wrapper);
    }
}
