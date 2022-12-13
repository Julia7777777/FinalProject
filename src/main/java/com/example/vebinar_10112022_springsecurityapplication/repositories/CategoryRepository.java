package com.example.vebinar_10112022_springsecurityapplication.repositories;
//Веб.29.11.22 21:05  созд. Репозиторий под Категории:
import com.example.vebinar_10112022_springsecurityapplication.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Веб.29.11.22 21:05  созд. Репозиторий под Категории:
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>
{
    //Метод, кот.будет возвращать Категорию (из списка Категорий) по наименованию:
    Category findByName(String name);
}
