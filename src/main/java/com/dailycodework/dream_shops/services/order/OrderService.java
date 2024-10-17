package com.dailycodework.dream_shops.services.order;

import com.dailycodework.dream_shops.dto.OrderDto;
import com.dailycodework.dream_shops.enums.OrderStatus;
import com.dailycodework.dream_shops.exceptions.RessourceNotFoundException;
import com.dailycodework.dream_shops.models.Cart;
import com.dailycodework.dream_shops.models.Order;
import com.dailycodework.dream_shops.models.OrderItem;
import com.dailycodework.dream_shops.models.Product;
import com.dailycodework.dream_shops.repositories.OrderRepository;
import com.dailycodework.dream_shops.repositories.ProductRepository;
import com.dailycodework.dream_shops.services.cart.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper; // used in convertToDto()

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(()->new RessourceNotFoundException("Order not found with id " + orderId));
    }


    //Helper Method
    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList
                .stream()
                .map(item-> item.getPrice()
                .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //Helper Method
    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory()-cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice()
            );
        }).toList();
    }

    //Helper Method
    private Order createOrder(Cart cart){
        Order order = new Order();
        // set the user
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order saveOrder= orderRepository.save(order);

        // Réinitialiser le montant total du panier à 0.00
        cartService.resetCartTotalAmount(cart.getId());

        // Vider le panier après la commande
        cartService.clearCart(cart.getId());
        return saveOrder;
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream().map(this::convertToDto).toList();
    }

    //Convert Order object to OrderDto object
    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

}
