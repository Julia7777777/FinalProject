package com.example.vebinar_10112022_springsecurityapplication.models;

import javax.persistence.*;
import java.util.List;

//Веб.29.11.22 08:00 – доб.товара по КАТЕГОРИЯМ. //09:40 - Созд.Модель Категории.
@Entity
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name; //Наименование Категории.

    //Установим связь - к товару будет привязываться КАТЕГОРИЯ (см.поле в Product):
    @OneToMany(mappedBy = "category") //Одна Категория может относиться ко  многим товарам. Связь будет с полем "category".  , fetch = FetchType.EAGER не стали писать после "category"
    private List<Product> products;


    //Геттеры и сеттеры:
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    //Конструкторы Я:

    public Category(int id, String name, List<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public Category() {
    }
}
