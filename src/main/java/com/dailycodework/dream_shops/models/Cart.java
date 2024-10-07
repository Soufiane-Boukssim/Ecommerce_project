package com.dailycodework.dream_shops.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private BigDecimal totalAmount=BigDecimal.ZERO;
    //cascade = CascadeType.ALL : Cette option indique que toutes les opérations (insertion, mise à jour, suppression, etc.) effectuées sur un Cart seront propagées aux CartItem associés.
    //cascade = CascadeType.ALL : when a Cart is deleted => all cartItems related to this cart are deleted
    //orphanRemoval = true : when there is any cartItems not referenced by any Cart it will be deleted
    //Avec orphanRemoval = true : Lorsque vous retirez item1 du Cart, il sera également supprimé de CartItem
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,orphanRemoval = true)
    //=new HashSet<>() pour que @PostMapping("/item/add") addItemToCard() marche

    private Set<CartItem> Items= new HashSet<>();

    public void addItem(CartItem item) {
        this.Items.add(item);
        item.setCart(this);
        updateTotalAmount();
    }

    public void removeItem(CartItem item) {
        this.Items.remove(item);
        item.setCart(null);
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        this.totalAmount = Items.stream()
                .map( item -> {
                    // Étape 1: Récupérer le prix unitaire
                    BigDecimal unitPrice = item.getUnitPrice();
                    // Étape 2: Vérifier si le prix unitaire est nul
                    if (unitPrice == null) {
                        // Si nul, retourner zéro
                        return  BigDecimal.ZERO;
                    }
                    // Étape 3: Calculer le total pour cet item
                    return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                } )
                // Étape 4: Réduire en une somme totale
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
