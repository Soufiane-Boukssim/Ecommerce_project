package com.dailycodework.dream_shops.repositories;

import com.dailycodework.dream_shops.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findProductByName(String name);

    List<Product> findProductByBrand(String brand);

    List<Product> findProductByCategoryName(String categoryName);

    List<Product> findProductByBrandAndName(String Brand, String Name);

    //Spring Data JPA utilise la nomenclature pour comprendre ce que vous voulez interroger
    List<Product> findProductByCategoryNameAndBrand(String Category, String Brand);

    Long countByBrand(String brand);


}
