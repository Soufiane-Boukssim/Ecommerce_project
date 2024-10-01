package com.dailycodework.dream_shops.services.category;

import com.dailycodework.dream_shops.models.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    List<Category> getAllCategories();

    Category addCategory(Category category);

    Category updateCategory(Category category, Long id);

    void deleteCategory(Long id);



}
