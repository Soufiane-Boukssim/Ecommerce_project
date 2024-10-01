package com.dailycodework.dream_shops.controllers;

import com.dailycodework.dream_shops.exceptions.ProductNotFoundException;
import com.dailycodework.dream_shops.models.Product;
import com.dailycodework.dream_shops.requests.AddProductRequest;
import com.dailycodework.dream_shops.requests.UpdateProductRequest;
import com.dailycodework.dream_shops.responses.ApiResponse;
import com.dailycodework.dream_shops.services.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@RestController
public class ProductController {
    private final IProductService productService;

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        //ResponseEntity.ok convertie objet en json pour cela on doit avoir getter dans ProductResponse
        return ResponseEntity.ok(new ApiResponse("Get All Products",productService.getAllProduct()));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProducts(@RequestBody AddProductRequest addProductRequest) {
        return ResponseEntity.ok(new ApiResponse("Product added successfully",productService.addProduct(addProductRequest)));
    }

    @GetMapping("/getById/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Product found",product));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getByName/{productName}")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String productName) {
        try {
            List<Product> products = productService.getProductByName(productName);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found ", null));
            }
            return  ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/getByBrand/{productBrand}")
    public ResponseEntity<ApiResponse> getProductByBrand(@PathVariable String productBrand) {
        try {
            List<Product> products = productService.getProductByBrand(productBrand);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found ", null));
            }
            return  ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/getByCategoryName/{categoryName}")
    public ResponseEntity<ApiResponse> getProductByCategoryName(@PathVariable String categoryName) {
        try {
            List<Product> products = productService.getProductByCategory(categoryName);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found ", null));
            }
            return  ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage()));
        }
    }
    @GetMapping("/getByBrandAndName/{productBrand}+{productName}")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@PathVariable String productBrand, @PathVariable String productName) {
        try {
            List<Product> products = productService.getProductByBrandAndName(productBrand, productName);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found ", null));
            }
            return  ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage()));
        }
    }

    @GetMapping("/getByCategoryAndBrand/{productCategory}+{productBrand}")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@PathVariable String productCategory, @PathVariable String productBrand) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(productCategory, productBrand);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found ", null));
            }
            return  ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage()));
        }
    }




    @DeleteMapping("/deleteById/{productId}")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            productService.deleteProduct(productId);
            return ResponseEntity.ok(new ApiResponse("Product deleted successfully",product));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PutMapping("/updateById/{productId}")
    public ResponseEntity<ApiResponse> updateProductById(@RequestBody UpdateProductRequest updateProductRequest, @PathVariable Long productId) {
        try {
            Product theProduct = productService.updateProduct(updateProductRequest, productId);
            return ResponseEntity.ok(new ApiResponse("Update product success!", theProduct));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/count/byBrand")
    public ResponseEntity<ApiResponse> countProductsByBrand(@RequestParam String brand) {
        try {
            long productCount = productService.countProductsByBrand(brand);
            return ResponseEntity.ok(new ApiResponse("Product count with the brand: "+brand, productCount));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
