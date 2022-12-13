package com.example.vebinar_10112022_springsecurityapplication.models;

import com.example.vebinar_10112022_springsecurityapplication.enumm.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

//Веб.29.11.22 2часть 1:11:00 – созд. Модель orders:
@Entity
@Table(name="orders")
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String number;

    @ManyToOne(optional = false) //1 продукт может относиться ко многим заказам, и во многих заказах м.быть 1 продукт.
    private Product product;

    @ManyToOne(optional = false)
    private Person person;

    private int count;
    private float price;

    private LocalDateTime dateTime; //Дата и время оформления заказа.

    private Status status; //Статус заказа.

    //Заполнение даты и времени при создании Объекта класса:
    @PrePersist
    private void init()
    {
        dateTime = LocalDateTime.now();
    }

    //Геттеры и сеттеры (кроме id и date):
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    //Конструктор пустой:
    public Order() {
    }

    //Конструктор (без id и date) - веб.29.11.22 2ч 1:25:35:
    public Order(String number, Product product, Person person, int count, float price, Status status) {
        this.number = number;
        this.product = product;
        this.person = person;
        this.count = count;
        this.price = price;
        this.status = status;
    }
}
