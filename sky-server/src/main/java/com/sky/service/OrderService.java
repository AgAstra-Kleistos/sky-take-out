package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.OrderDetail;
import com.sky.result.PageResult;
import com.sky.vo.*;

public interface OrderService {
    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 条件搜索
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 获取订单统计数据
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    OrderVO details(Long id);

    /**
     * 分页查询用户历史订单
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    PageResult pageQueryUser(int pageNum, int pageSize, Integer status);

    /**
     * 用户取消订单
     * @param id
     */
    void userCancelById(Long id);

    /**
     * 再来一单
     *
     * @param id
     */
    void repetition(Long id);


    /**
     * 订单确认
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);


    /**
     * 拒单
     * @param ordersRejectionDTO
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 取消订单
     * @param ordersCancelDTO
     */
    void cancel(OrdersCancelDTO ordersCancelDTO);

    /**
     * 派送订单
     * @param id
     */
    void delivery(Long id);

    /**
     * 完成订单
     * @param id
     */
    void complete(Long id);
}
