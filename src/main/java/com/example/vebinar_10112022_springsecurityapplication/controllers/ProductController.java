package com.example.vebinar_10112022_springsecurityapplication.controllers;

import com.example.vebinar_10112022_springsecurityapplication.models.Order;
import com.example.vebinar_10112022_springsecurityapplication.repositories.OrderRepository;
import com.example.vebinar_10112022_springsecurityapplication.repositories.ProductRepository;
import com.example.vebinar_10112022_springsecurityapplication.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Веб.22.11.22 2часть 31:30  созд. ProductController:
@Controller
@RequestMapping("/product") //Веб.29.11.22 1:10:35 доб.
public class ProductController
{
//    //Метод по открытию станицы с продуктами:
//    @GetMapping("/product")
//    public String products()
//    {
//        return "product/product";
//    }

//Веб.29.11.22 2часть 06:18 - внедрим ProductRepository:
    private final ProductRepository productRepository;

    // !!!!!!!!!!!!!!
    private final OrderRepository orderRepository;

//Веб.29.11.22 49:50  сделаем так, чтоб ВСЕ товары сразу выводились на гл. странице магазина:
    private final ProductService productService; //Внедрим сервис ч\з Контсруктор (50:55).
    @Autowired
    public ProductController(ProductRepository productRepository, OrderRepository orderRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

//  @GetMapping("/product") //Веб.29.11.22 1:10:35 удалили.
    @GetMapping("")
    public String getAllProduct(Model model)
    {
        model.addAttribute("products", productService.getAllProduct());
        return "/product/product";
    }
    //========================================================================
//Веб.24.11.22 2часть 1:33:00  реализуем, чтоб при нажатии на ссылку о товаре (из /product) подробная инфо о товаре выводилась:
    //Метод по обработке нажатия на ссылку с подробной инфо о товаре (с /product):
//  @GetMapping("/product/info/{id}") //Веб.29.11.22 1:10:35 удалили.
    @GetMapping("/info/{id}") //Веб.29.11.22 1:10:35 удалили.
    public String infoUser(@PathVariable("id") int id, Model model)
    {
        model.addAttribute("product", productService.getProductId(id));
        return "product/infoProduct";
    }

//Веб.29.11.22 1:11:05  продумаем логику поиска:
    //Метод по обработке формы ПОИСКА:
    @PostMapping("/search")
    public String productSearch(@RequestParam("search") String search,
                                @RequestParam("from") String from,
                                @RequestParam("to") String to,
                                @RequestParam(value = "price", required = false, defaultValue = "") String price,
                                @RequestParam(value = "contact", required = false, defaultValue = "") String contact,
                             Model model
    ) //Если с формы что-то пришло, то получаем и помещаем в пемереммную. Если ничего не пришло, то берем пустую строку "".


    {
//Веб.29.11.22 2часть 00:45 ProductController:
        if(!from.isEmpty() & !to.isEmpty()) //Если поля "От" и "До" НЕпустые,
        {
            if(!price.isEmpty()) //и при этом Проверяем, является ли цена (сортировка по возр и уб) пустой
            {
                if(price.equals("sorted_by_ascending_price")) //по возрастанию.
                {
                    if(!contact.isEmpty())
                    {
                        if(contact.equals("toys")) //Если лежат Игрушки,
                        {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(from), Float.parseFloat(to), 1 )); //то в Модель будем добавлять Объект search_product, обращаться к Репозиторию и вызывать Метод
                        }
                        else if(contact.equals("houses")) //Если лежат Домики,
                        {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(from), Float.parseFloat(to), 2 )); //то в Модель будем добавлять Объект search_product, обращаться к Репозиторию и вызывать Метод
                        }
                        else if(contact.equals("bowls")) //Если лежит Посуда,
                        {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(from), Float.parseFloat(to), 3 )); //то в Модель будем добавлять Объект search_product, обращаться к Репозиторию и вызывать Метод
                        }
                    }
                }
                else if (price.equals("sorted_by_descending_price")) //Веб.29.11.22 2часть 10:40  по убыванию.
                {
                    if(!contact.isEmpty())
                    {
                        if(contact.equals("toys")) //Если лежат Игрушки,
                        {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(from), Float.parseFloat(to), 1 )); //то в Модель будем добавлять Объект search_product, обращаться к Репозиторию и вызывать Метод
                        }
                        else if(contact.equals("houses")) //Если лежат Домики,
                        {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(from), Float.parseFloat(to), 2 )); //то в Модель будем добавлять Объект search_product, обращаться к Репозиторию и вызывать Метод
                        }
                        else if(contact.equals("bowls")) //Если лежит Посуда,
                        {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(from), Float.parseFloat(to), 3 )); //то в Модель будем добавлять Объект search_product, обращаться к Репозиторию и вызывать Метод
                        }
                    }
                }
            }
            else
            {
                model.addAttribute("search_product", productRepository.findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(search, Float.parseFloat(from), Float.parseFloat(to))); //Веб.29.11.22 2часть 13:40.
            }
        }
        else
        {
            model.addAttribute("search_product", productRepository.findByTitleContainingIgnoreCase(search)); //Веб.29.11.22 2часть 11:27.
        }

        model.addAttribute("value_search", search); //29/11/22 2ч 15:30
        model.addAttribute("value_price_from", from);
        model.addAttribute("value_price_to", to); //Веб.29.11.22 2часть 16:10 - Эти значения передадим, а на стр. product.html выведем.
        model.addAttribute("products", productService.getAllProduct());//чтобы выводили и доступные товары тоже (после поиска нужного).
        return "/product/product";

    }
//    //!!!!!!!!!!!!
//    public List<Order> findProductByLastFourSymbols(String LastFourSymbols)
//    {
//        return orderRepository.findProductByLastFourSymbols(LastFourSymbols);
//    }
}



