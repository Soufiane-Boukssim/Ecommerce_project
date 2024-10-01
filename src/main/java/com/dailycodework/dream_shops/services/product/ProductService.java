package com.dailycodework.dream_shops.services.product;

import com.dailycodework.dream_shops.exceptions.ProductNotFoundException;
import com.dailycodework.dream_shops.models.Category;
import com.dailycodework.dream_shops.models.Product;
import com.dailycodework.dream_shops.repositories.CategoryRepository;
import com.dailycodework.dream_shops.repositories.ProductRepository;
import com.dailycodework.dream_shops.requests.AddProductRequest;
import com.dailycodework.dream_shops.requests.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product addProduct(AddProductRequest request) {
        //1. Recherche ou création de la catégorie
        Category category= Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()-> {
                    Category newCategory = new Category(request.getCategory().getName());
                    //(pour category)
                     return categoryRepository.save(newCategory);
                });
        //2. Affectation de la catégorie à la requête (pour product)
        request.setCategory(category);
        //4. Méthode de Sauvegarde du Produit
        return productRepository.save(createProduct(request));
    }

    //3. Méthode de Création de Produit (et qui va etre afficher dans postman)
    public Product createProduct(AddProductRequest request) {
        return new Product(
            request.getName(),
            request.getBrand(),
            request.getPrice(),
            request.getInventory(),
            request.getDescription(),
            request.getCategory()
        );
    }


    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findProductByName(name);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findProductByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return productRepository.findProductByCategoryName(category);
    }

    @Override
    public List<Product> getProductByBrandAndName(String Brand, String Name) {
        return productRepository.findProductByBrandAndName(Brand, Name);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findProductByCategoryNameAndBrand(category, brand);
    }


    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                () -> {throw new ProductNotFoundException("Product not found!");}
        );
    }

    @Override
    public Product updateProduct(UpdateProductRequest product, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct,product))
                .map(productRepository :: save)
                .orElseThrow(()-> new ProductNotFoundException("Product not found!"));
    }

    @Override
    public Long countProductsByBrand(String brand) {
        long counter= productRepository.countByBrand(brand);
        if (counter==0)
            throw new ProductNotFoundException("Product not found with the brand: "+brand);
        return counter;
    }


    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return  existingProduct;

    }




}
