package com.example.vebinar_10112022_springsecurityapplication.repositories;
//Веб.29.11.22 2часть 1:16:25 – созд Репозиторий под заказ:
import com.example.vebinar_10112022_springsecurityapplication.models.Order;
import com.example.vebinar_10112022_springsecurityapplication.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>
{
    //Метод по вытаскиванию листа заказов:
    List<Order> findByPerson(Person person); //По Объекту пользователя вытасикиваем лист заказов, кот. пльзователь делал.


    //=====!!!!!!!!!!!!!!!!!!!!!!!!!!!!=====
    @Query(value = "SELECT * FROM orders WHERE RIGHT(number, 4) LIKE '?1'", nativeQuery = true)
//    List<Order> findProductByLastFourSymbols(String LastFourSymbols);
    List<Order> findProductByLastFourSymbols(Person person);
}
