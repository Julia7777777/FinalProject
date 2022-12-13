package com.example.vebinar_10112022_springsecurityapplication.models;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

//Веб.10.11.22 2часть 12:45 – сформируем Модель, кот. будет работать с пользователями user1, user2, user3:
@Entity
@Table(name = "Person")
public class Person
{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Поле 'ЛОГИН' не может быть пустым.")
    @Size(min = 4, max = 70, message = "Поле 'ЛОГИН' должно быть от 4 до 70 символов.")
    @Column(name = "login")
    private String login;

    @NotEmpty(message = "Поле 'ПАРОЛЬ' не может быть пустым.")
//  @Size(min = 4, max = 70, message = "Поле 'ПАРОЛЬ' должно быть от 4 до 70 символов.")
    @Column(name = "password")
    @Max(value = 70, message = "Поле 'ПАРОЛЬ' не может быть более 70 символов.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Пароль должен состоять минимум из 8-ми символов, содержать в себе хотя бы 1 цифру, 1 специальный символ, букву в верхнем и нижнем регистрах и не содержать пробел.")
    private String password;

//    Веб.15.11.22 2часть 37:25 --- модифицируем Модель, добавив поле "role":
    @Column(name="role")
    private String role; //поле, отвечающее за РОЛЬ.

//Веб.29.11.22 2часть 30:30 – Объявим, что у польз-ля в корзине м.б. много товаров: product_cart - новая связующая таблица между person и product:
    @ManyToMany()
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "person_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> product;

//Веб.29.11.22 2ч 1:15:35 -- Теперь связь надо сделать и на др. стороне (Person), см. Order и Product:
    @OneToMany(mappedBy = "person")
    private List<Order> orderList;


    //Пустой Конструктор:
    public Person() {
    }

    public Person(String role) {
        this.role = role;
    }

    //Конструктор с параметрами:
    public Person(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    //Геттеры и сеттеры:
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
