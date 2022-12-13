package com.example.vebinar_10112022_springsecurityapplication.models;

import javax.persistence.*;

//Веб.29.11.22 2часть 36:20  созд. Модель и подключимся к табл. product_cart, чтобы с ней м.было взаимодействовать:
@Entity
@Table(name = "product_cart") //Устан. связь с существующей табл.product_cart
public class Cart
{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "person_id") //Устан.связь с колонкой
    private int personId;

    @Column(name = "product_id") //Устан.связь с колонкой
    private int productId;


    //Конструкторы:
    public Cart(int personId, int productId) {
        this.personId = personId;
        this.productId = productId;
    }

    public Cart() {
    }


    //Геттеры и сеттеры:
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
