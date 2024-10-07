package com.dailycodework.dream_shops.services.cart;

import com.dailycodework.dream_shops.models.CartItem;

public interface ICartItemService {
    void addItemToCard(Long cartId, Long productId, int quantity);
    void removeItemFromCard(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);
}
