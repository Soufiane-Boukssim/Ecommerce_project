package com.dailycodework.dream_shops.dto;

import com.dailycodework.dream_shops.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDto {
    private Long Id;
    private Long userId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private OrderStatus Status;
    private List<OrderItemDto> Items;
}
