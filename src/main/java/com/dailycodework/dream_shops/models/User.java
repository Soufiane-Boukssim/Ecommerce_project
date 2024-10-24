package com.dailycodework.dream_shops.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @NaturalId
    private String email;
    private String password;
    @OneToOne(mappedBy="user", cascade=CascadeType.ALL,orphanRemoval = true)
    private Cart cart;
    @OneToMany(mappedBy="user", cascade=CascadeType.ALL,orphanRemoval = true)
    private List<Order> orders;

    //CascadeType.REMOVE est exclu pour sela on a pas utilisé cascade=CascadeType.ALL
    //Because we have many Users shares the same role
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "user_roles", joinColumns= @JoinColumn(name = "user_id", referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles= new HashSet<>();

}
