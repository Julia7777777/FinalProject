package com.example.vebinar_10112022_springsecurityapplication.models;

import javax.persistence.*;
//Веб. 24.11.22 2часть 21:35  работа с ФОТО:
//Нужна отд Модель, с кот. будем устанавливать связь (делаем табл. с фото):
@Entity
public class Image
{
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fileName;

    @ManyToOne(fetch = FetchType.EAGER, optional = false) //Много ФОТО может относиться к одному товару! Режим EAGER позволяет подгрузить фото. А optional - данная табл.не будет отвечать за данную связь.
    private Product product; //Устанавливаем СВЯЗЬ с табл. product.

    //Конструктор с параметрами:
    public Image(int id, String fileName, Product product) {
        this.id = id;
        this.fileName = fileName;
        this.product = product;
    }
    //Пустой Конструктор:
    public Image() {
    }

    //Геттеры и сеттеры для всех полей:
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
