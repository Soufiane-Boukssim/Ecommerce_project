package com.dailycodework.dream_shops.services.order;

import com.dailycodework.dream_shops.dto.OrderDto;
import com.dailycodework.dream_shops.models.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    //Convert Order object to OrderDto object
    OrderDto convertToDto(Order order);
}
