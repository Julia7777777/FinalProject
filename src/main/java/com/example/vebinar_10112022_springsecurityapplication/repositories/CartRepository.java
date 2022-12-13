package com.example.vebinar_10112022_springsecurityapplication.repositories;
//Веб.29.11.22 2часть 38:05  созд.Репозиторий под Корзину:
import com.example.vebinar_10112022_springsecurityapplication.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CartRepository extends JpaRepository<Cart, Integer>
{
// Веб.29.11.22 2часть 39:10  Метод, кот. позволит получить лист корзин по ID пользователя:
    List<Cart> findByPersonId(int id);


    //Метод по удалению (т.к. это не чтение, то @Transactional)
    void deleteCartByProductId(int id);

}
