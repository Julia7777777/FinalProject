package com.example.vebinar_10112022_springsecurityapplication.repositories;

import com.example.vebinar_10112022_springsecurityapplication.models.Order;
import com.example.vebinar_10112022_springsecurityapplication.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//Веб.22.11.22 2часть 40:10  созд. ProductRepository
public interface ProductRepository extends JpaRepository<Product, Integer>
{
//Веб.29.11.22 1:15:00 пропишем все Методы по работе с сортировками:
    //Метод поиска товара по части его названия БЕЗ учета регистра (в поисковой строке):
    List<Product> findByTitleContainingIgnoreCase(String name); //Поиск без учета регистра.

//Веб.29.11.22 1:18:10  сделаем и через SQL-запрос (@):
    //Метод, кот. будет учитывать наименование и диапазон цен (от-до):
    @Query(value = "SELECT * FROM product WHERE ((LOWER(title) LIKE %?1%) OR (LOWER(title) LIKE '?1%') OR (LOWER(title) LIKE '%?1')) AND (price >= ?2 AND price <= ?3)", nativeQuery = true) //Веб.29.11.22 1:19:30 - аннотация для использования SQL-запроса.
    List<Product> findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(String title, float from, float to);

//Веб.29.11.22 1:23:25 -
    //Поиск по наименованию, Фильтрация по диапазону цены + Метод сортировки ПО ВОЗРАСТАНИЮ цены:
    @Query(value = "SELECT * FROM product WHERE ((LOWER(title) LIKE %?1%) OR (LOWER(title) LIKE '?1%') OR (LOWER(title) LIKE '%?1')) AND (price >= ?2 AND price <= ?3) ORDER BY price ASC", nativeQuery = true)
    List<Product> findByTitleOrderByPriceAsc(String title, float from, float to);

//Веб.29.11.22 1:25:30 --
//Поиск по наименованию, Фильтрация по диапазону цены + Метод сортировки ПО УБЫВАНИЮ цены:
@Query(value = "SELECT * FROM product WHERE ((LOWER(title) LIKE %?1%) OR (LOWER(title) LIKE '?1%') OR (LOWER(title) LIKE '%?1')) AND (price >= ?2 AND price <= ?3) ORDER BY price DESC", nativeQuery = true)
List<Product> findByTitleOrderByPriceDesc(String title, float from, float to);

//Веб.29.11.22 1:25:55--
//Поиск по наименованию, Фильтрация по диапазону цены + Метод сортировки ПО ВОЗРАСТАНИЮ цены + Метод сортировки по КАТЕГОРИЯМ:
@Query(value = "SELECT * FROM product WHERE category_id=?4 AND ((LOWER(title) LIKE %?1%) OR (LOWER(title) LIKE '?1%') OR (LOWER(title) LIKE '%?1')) AND (price >= ?2 AND price <= ?3) ORDER BY price ASC", nativeQuery = true) //категория должна = тому, что будет передано в Метод.
    List<Product> findByTitleAndCategoryOrderByPriceAsc(String title, float from, float to, int category);

//Веб.29.11.22 1:28:00--
//Поиск по наименованию, Фильтрация по диапазону цены + Метод сортировки ПО УБЫВАНИЮ цены + Метод сортировки по КАТЕГОРИЯМ:
@Query(value = "SELECT * FROM product WHERE category_id=?4 AND ((LOWER(title) LIKE %?1%) OR (LOWER(title) LIKE '?1%') OR (LOWER(title) LIKE '%?1')) AND (price >= ?2 AND price <= ?3) ORDER BY price DESC", nativeQuery = true) //категория должна = тому, что будет передано в Метод.
List<Product> findByTitleAndCategoryOrderByPriceDesc(String title, float from, float to, int category);

//=====!!!!!!!!!!!!!!!!!!!!!!!!!!!!=====
//    @Query(value = "SELECT * FROM orders WHERE RIGHT(number, 4) LIKE '?1'", nativeQuery = true)
//    List<Order> findProductByLastFourSymbols(String LastFourSymbols);

}
