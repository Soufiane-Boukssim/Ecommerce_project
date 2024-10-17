package com.dailycodework.dream_shops.repositories;

import com.dailycodework.dream_shops.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

}
