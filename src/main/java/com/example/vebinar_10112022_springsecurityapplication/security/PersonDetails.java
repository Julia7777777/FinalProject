package com.example.vebinar_10112022_springsecurityapplication.security;
//Веб.10.11.22 2часть 16:40 – произведем настройку для безопасности:

import com.example.vebinar_10112022_springsecurityapplication.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class PersonDetails implements UserDetails
{//Раз класс наследует интерфейс, мы должны реализовать все  Методы данного интерфейса:
    private final Person person; //Внедрим поле, кот. будет отвечать за Модель (внедрим Модель через Конструктор).
    @Autowired
    public PersonDetails(Person person) {
        this.person = person;
    }

    //Метод, возвращающий РОЛЬ текущего человека (веб.15.11.22 2часть 45:50).
    //Gо возвращенной РОЛИ мы сможем определить, какой функционал доступен человеку в рамках нашей системы!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole())); //Возвращает лист из одного элемента.
    }

    @Override
    public String getPassword() {
        return this.person.getPassword(); //реализовать надо данный Метод. Он срабатывает, когда хотим получить пароль пользователя (Веб.10.11.22 2ч 19:00).
    }

    @Override
    public String getUsername() {
        return this.person.getLogin(); //Метод по получению логина пользователя.
    }
//Аккаунт действителен:
    @Override
    public boolean isAccountNonExpired() {
        return true; //Устанавливаем true (все записи разморожены, разлокированы и т.д.)
    }
    //Аккаунт не заблокирован:
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
//Пароль явл. действительным:
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //Аккаунт активен:
    @Override
    public boolean isEnabled() {
        return true;
    }

//    Веб.10.11.22 2ч 20:40 --- реализуем Метод, кот будет возвращать Объект человека после успешной Аутентификации:
//Получить Объект пользователя:
    public Person getPerson()
    {
        return this.person;
    }

}
