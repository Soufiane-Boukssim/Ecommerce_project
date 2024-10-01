package com.dailycodework.dream_shops.services.product;

import com.dailycodework.dream_shops.models.Product;
import com.dailycodework.dream_shops.requests.AddProductRequest;
import com.dailycodework.dream_shops.requests.UpdateProductRequest;

import java.util.List;

public interface IProductService {

   List<Product> getAllProduct();

   Product getProductById(Long id);

   List<Product> getProductByName(String name);

   List<Product> getProductByBrand(String brand);

   List<Product> getProductByCategory(String Category);

   List<Product> getProductByBrandAndName(String Brand, String Name);

   List<Product> getProductsByCategoryAndBrand(String category,String brand);


   Product addProduct(AddProductRequest product);

   void deleteProduct(Long id);

   Product updateProduct(UpdateProductRequest product, Long productId);

   Long countProductsByBrand(String brand);

}
