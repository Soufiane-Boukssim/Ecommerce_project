package com.dailycodework.dream_shops.services.cart;

import com.dailycodework.dream_shops.models.Cart;
import com.dailycodework.dream_shops.models.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);


    Cart initializeNewCart(User user);

    //new
    Cart getCartByUserId(Long userId);

    void resetCartTotalAmount(Long id);
}
