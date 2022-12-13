package com.example.vebinar_10112022_springsecurityapplication.repositories;
//Веб.10.11.22 2ч 26:40 --- далее работаем с Репозиторием:

import com.example.vebinar_10112022_springsecurityapplication.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer>
{
    //Метод по нахождению человека по ЛОГИНУ:
    Optional<Person> findByLogin(String login);

//    !!!!!!!!!
//    Optional<Role> findByName(String name);
//    Role findByName(String name);
}
