package com.dailycodework.dream_shops.repositories;

import com.dailycodework.dream_shops.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
