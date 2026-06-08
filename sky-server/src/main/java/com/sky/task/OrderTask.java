package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 * * * * ?")
    //@Scheduled(cron = "0/5 * * * * ?")
    public void processTimeOutOrder(){
        log.info("定时处理超时订单， {}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);  //当前时间+(-15)min
        //处于待付款状态的订单
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now());

        //遍历，处理订单
        for(Orders orders : ordersList){
            log.info("处理超时订单，订单号：{}", orders.getNumber());
            orders.setStatus(Orders.CANCELLED);
            orders.setCancelReason("订单超时，自动取消");
            orders.setCancelTime(LocalDateTime.now());
            orderMapper.update(orders);
        }
    }

    /**
     * 处理一种处于派送订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    //@Scheduled(cron = "2/5 * * * * ?")
    public void processDeliveryOrder(){

        log.info("定时处理派送中订单， {}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);

        for(Orders orders : ordersList){
            log.info("处理订单：{}", orders.getId());
            orders.setStatus(Orders.COMPLETED);
            orderMapper.update(orders);
        }
    }
}
