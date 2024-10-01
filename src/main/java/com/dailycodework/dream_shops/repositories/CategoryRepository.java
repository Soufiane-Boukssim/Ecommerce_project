package com.dailycodework.dream_shops.repositories;

import com.dailycodework.dream_shops.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
//    find : indique que tu veux effectuer une recherche.
//    By : signale que tu vas filtrer les résultats par un champ.
//    Name : correspond au nom du champ dans l'entité Category car on a CategoryRepository.

     Category findByName(String name);
}
