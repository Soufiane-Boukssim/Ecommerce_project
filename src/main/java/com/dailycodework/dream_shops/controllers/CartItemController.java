package com.dailycodework.dream_shops.controllers;

import com.dailycodework.dream_shops.exceptions.RessourceNotFoundException;
import com.dailycodework.dream_shops.responses.ApiResponse;
import com.dailycodework.dream_shops.services.cart.ICartItemService;
import com.dailycodework.dream_shops.services.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
@RestController
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCard(@RequestParam(required = false) Long cartId,@RequestParam Long productId,@RequestParam int quantity) {
        try {
            if(cartId==null){
                cartId= cartService.initializeNewCart();
            }
            cartItemService.addItemToCard(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item added successfully",null));
        } catch (RessourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCard(@PathVariable Long cartId,@PathVariable Long itemId) {
        try {
            cartItemService.removeItemFromCard(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Item removed successfully",null));
        } catch (RessourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId, @PathVariable Long itemId, @RequestParam Integer quantity) {
        try {
            cartItemService.updateItemQuantity(cartId, itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item updated successfully",null));
        } catch (RessourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));

        }
    }


}