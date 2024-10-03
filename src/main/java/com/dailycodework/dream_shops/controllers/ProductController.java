package com.dailycodework.dream_shops.controllers;

import com.dailycodework.dream_shops.dto.ProductDto;
import com.dailycodework.dream_shops.exceptions.ProductNotFoundException;
import com.dailycodework.dream_shops.models.Product;
import com.dailycodework.dream_shops.requests.AddProductRequest;
import com.dailycodework.dream_shops.requests.UpdateProductRequest;
import com.dailycodework.dream_shops.responses.ApiResponse;
import com.dailycodework.dream_shops.services.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@RequestMapping("/api/products")
@RequiredArgsConstructor
@RestController
public class ProductController {
    private final IProductService productService;

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        //ResponseEntity.ok convertie objet en json pour cela on doit avoir getter dans ProductResponse
        List<Product> products= productService.getAllProduct();
        List<ProductDto> convertedProducts= productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Get All Products",convertedProducts));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product theProduct = productService.addProduct(product);
            ProductDto productDto = productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("Add product success!", productDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getById/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            ProductDto productDto=productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Product found",productDto));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getByName/{productName}")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String productName) {
        try {
            Product product = productService.getProductByName(productName);
            ProductDto productDto = productService.convertToDto(product);
            return  ResponseEntity.ok(new ApiResponse("success", productDto));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getByBrand/{productBrand}")
    public ResponseEntity<ApiResponse> getProductsByBrand(@PathVariable String productBrand) {
        try {
            List<Product> products = productService.getProductsByBrand(productBrand);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return  ResponseEntity.ok(new ApiResponse("success", convertedProducts));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/getByCategoryName/{categoryName}")
    public ResponseEntity<ApiResponse> getProductByCategoryName(@PathVariable String categoryName) {
        try {
            List<Product> products = productService.getProductByCategory(categoryName);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return  ResponseEntity.ok(new ApiResponse("success", convertedProducts));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
    @GetMapping("/getByBrandAndName/{productBrand}+{productName}")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@PathVariable String productBrand, @PathVariable String productName) {
        try {
            List<Product> products = productService.getProductByBrandAndName(productBrand, productName);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return  ResponseEntity.ok(new ApiResponse("success", convertedProducts));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getByCategoryAndBrand/{productCategory}+{productBrand}")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@PathVariable String productCategory, @PathVariable String productBrand) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(productCategory, productBrand);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            return  ResponseEntity.ok(new ApiResponse("success", convertedProducts));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }




    @DeleteMapping("/deleteById/{productId}")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            productService.deleteProduct(productId);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Product deleted successfully",productDto));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PutMapping("/updateById/{productId}")
    public ResponseEntity<ApiResponse> updateProductById(@RequestBody UpdateProductRequest updateProductRequest, @PathVariable Long productId) {
        try {
            Product theProduct = productService.updateProduct(updateProductRequest, productId);
            ProductDto productDto = productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("Update product success!", productDto));
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
