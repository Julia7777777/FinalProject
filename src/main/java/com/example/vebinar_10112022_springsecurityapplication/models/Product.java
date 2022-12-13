package com.example.vebinar_10112022_springsecurityapplication.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


//Веб.22.11.22 2часть 35:40  созд. Модель с продуктами (и потом будем выводить данные из БД по доступным продуктам):
@Entity
public class Product
{
    @Id
//Веб.24.11.22 43:30 – модель Product делаем валидацию, чтоб notnull не было и тд:
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title", nullable = false, columnDefinition = "text", unique = true)
    @NotEmpty(message = "Поле 'НАИМЕНОВАНИЕ ТОВАРА' не может быть пустым.")
    private String title;
    @Column(name = "description", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Поле 'ОПИСАНИЕ ТОВАРА' не может быть пустым.")
    private String description;
    @Column(name = "price", nullable = false)
 // @NotEmpty(message = "Поле 'СТОИМОСТЬ ТОВАРА' не может быть пустым.")
    @Min(value = 1, message = "Стоимость товара должна начинаться от 1.")
    private float price;
    @Column(name = "warehouse", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Поле 'НАИМЕНОВАНИЕ СКЛАДА' не может быть пустым.")
    private String warehouse; //Город продавца (т.к. делаем MarketPlace).//Веб.24.11.22 19:40 - поменяли на склад.
    @Column(name = "seller", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Поле 'ИНФОРМАЦИЯ О ПРОДАВЦЕ' не может быть пустым.")
    private String seller; //Поле продавца.

//Веб.24.11.22 2часть 27:00  идем в Product:
//Создадим поле, отвечающее за ФОТО:
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product") //У одного товара м.быть много ФОТО! Каскадирование - при удалении товара все фото также будут удалены из БД.
    private List<Image> imageList = new ArrayList<>(); //Созд.Лист, кот.будет храниться на Модели. И в этом Листе будут храниться все ФОТО.


//Веб.24.11.22 50:10 – создадим еще поле «ДАТА СОЗДАНИЯ» и Метод, кот. будет его инициализировать:
//Будем заполнять ДАТУ и ВРЕМЯ при создании Объекта класса:
    private LocalDateTime dateTime;


//Веб.29.11.22 08:00 – доб.товара по КАТЕГОРИЯМ.
//Веб.29.11.22 11:20 - Установим связь - к товару будет присваиваться КАТЕГОРИЯ 11:50:
    @ManyToOne(optional = false) //Много товаров может относиться к одной Категории.
    private Category category;


    //Веб.24.11.22 2часть 50:30  пропишем логику по сохранению ФОТО:
    //Метод по добавлению ФОТО в лист к текущему товару:
    public void addImageToProduct(Image image)
    {
        image.setProduct(this);//Обращаемся к Модели Image и указываем, что работаем с текущим товаром.
        imageList.add(image);//В imageList добавляем новый объект.
    }

//Веб.29.11.22 2часть 33:05  также (помимо Person) связь ManyToMany надо указать и в табл product (для Корзины):
    @ManyToMany()
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<Person> personList; //В продукте будет лежать лист пользователей.



//Веб.29.11.22 2ч 1:14: 55 -- Теперь связь надо сделать и на др. стороне (Product), см. Order:
    @OneToMany(mappedBy = "product")
    private List<Order> orderList;



//Веб.24.11.22 50:10 – создадим еще поле «ДАТА СОЗДАНИЯ» и Метод, кот. будет его инициализировать:
//Будем заполнять ДАТУ и ВРЕМЯ при создании Объекта класса: //  private LocalDateTime dateTime;
    @PrePersist
    private void init()
    {
        dateTime = LocalDateTime.now();
    }


    //Геттеры и сеттеры для всех полей:
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    //Конструкторы:
    public Product(String title, String description, float price, String city, String seller) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.warehouse = warehouse;
        this.seller = seller;
    }

//    public Product(String title, String description, float price, String warehouse, String seller, LocalDateTime dateTime) {
//        this.title = title;
//        this.description = description;
//        this.price = price;
//        this.warehouse = warehouse;
//        this.seller = seller;
//        this.dateTime = dateTime; //добавила. Не надо.
//    }

    public Product() {
    }

}
