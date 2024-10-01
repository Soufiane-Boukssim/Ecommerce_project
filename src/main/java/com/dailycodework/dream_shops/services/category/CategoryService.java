package com.dailycodework.dream_shops.services.category;

import com.dailycodework.dream_shops.exceptions.CategoryAlreadyExistsException;
import com.dailycodework.dream_shops.exceptions.CategoryNotFoundException;
import com.dailycodework.dream_shops.models.Category;
import com.dailycodework.dream_shops.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

/*findById() retourne un Optional<Category>.
1.Si une catégorie est trouvée :
    Le Category est retourné par la méthode et le programme continue normalement.
2.Si aucune catégorie n'est trouvée :
    L'exception CategoryNotFoundException est levée.
    La méthode ne retourne pas de Category dans ce cas, mais comme une exception a été levée, le programme passe dans un flux d'erreur.
    Cela signifie que le type de retour n'est plus pertinent à ce stade, car une exception prend le relais et est envoyée aux couches supérieures (par exemple, le contrôleur dans ton cas).*/
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()->new CategoryNotFoundException("Category not found with id: " + id));
    }

    @Override
    public Category getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name);
        if (category == null) {
            throw new CategoryNotFoundException("Category not found with name: " + name);
        }
        return category;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new CategoryNotFoundException("Table category is empty");
        }
        return categories;
    }

    @Override
    public Category addCategory(Category category) {
        Category cat= categoryRepository.findByName(category.getName());
        if (cat != null) {
            throw new CategoryAlreadyExistsException("Category: "+category.getName()+" already exists");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        Optional<Category> cat= categoryRepository.findById(id);
        if (cat.isPresent()) {
            List<Category> categories = categoryRepository.findAll();
            for (Category c : categories) {
                if (c.getName().equals(category.getName())) {
                    throw new CategoryAlreadyExistsException("Category name: "+category.getName()+" already exists");
                }
            }
            cat.get().setName(category.getName());
            return categoryRepository.save(cat.get());
        }
        throw new CategoryNotFoundException("Category not found with id: " + id);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete,()->{
            throw new CategoryNotFoundException("Category not found with id: " + id);
        });
    }
}
