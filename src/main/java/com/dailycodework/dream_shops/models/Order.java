package com.dailycodework.dream_shops.models;

import com.dailycodework.dream_shops.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "`order`") // Utilisation de backticks pour éviter le conflit avec le mot réservé ORDER (qui est normalement utilisé pour ORDER BY dans SQL).
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)//by default int: EnumType.ORDINAL
    private OrderStatus orderStatus;
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<OrderItem> orderItems=new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
