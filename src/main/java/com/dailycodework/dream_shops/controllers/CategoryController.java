package com.dailycodework.dream_shops.controllers;

import com.dailycodework.dream_shops.exceptions.CategoryAlreadyExistsException;
import com.dailycodework.dream_shops.exceptions.CategoryNotFoundException;
import com.dailycodework.dream_shops.exceptions.ProductNotFoundException;
import com.dailycodework.dream_shops.models.Category;
import com.dailycodework.dream_shops.models.Product;
import com.dailycodework.dream_shops.responses.ApiResponse;
import com.dailycodework.dream_shops.services.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RequestMapping("/api/categories")
@RestController
public class CategoryController {
    private final CategoryService categoryService;


    @GetMapping("/getById/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId) {
        try {
            Category category = categoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Category found",category));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getByName/{categoryName}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String categoryName) {
        try {
            Category category = categoryService.getCategoryByName(categoryName);
            return ResponseEntity.ok(new ApiResponse("Category found",category));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("getAll")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Categories found",categories));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/deleteById/{categoryID}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryID) {
        try {
            Category category = categoryService.getCategoryById(categoryID);

            // Vérifier si la catégorie a des produits associés
            if (!category.getProducts().isEmpty()) {
                // Si des produits sont associés, retourner un message d'erreur
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse("Cannot delete the category with the id:"+categoryID+", because it's not empty", null));
            }

            categoryService.deleteCategory(categoryID);
            return ResponseEntity.ok(new ApiResponse("Category deleted",category));
        }
        catch (CategoryNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/addCategory")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
        try {
            Category theCategory =categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Category added",theCategory));
        }
        catch (CategoryAlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/updateCategory/{categoryID}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long categoryID, @RequestBody Category category) {
        try {

            Category theCategory= categoryService.updateCategory(category, categoryID);
            return ResponseEntity.ok(new ApiResponse("Category with the ID:"+categoryID+" is updated",theCategory));
        }
        catch (CategoryNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        catch (CategoryAlreadyExistsException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }

    }


}
