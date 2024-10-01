package com.dailycodework.dream_shops.requests;

import com.dailycodework.dream_shops.models.Category;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateProductRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory; //inventory:quantity
    private String description;
    private Category category;
}
