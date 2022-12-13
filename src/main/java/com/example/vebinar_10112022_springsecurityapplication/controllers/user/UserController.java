package com.example.vebinar_10112022_springsecurityapplication.controllers.user;

import com.example.vebinar_10112022_springsecurityapplication.enumm.Status;
import com.example.vebinar_10112022_springsecurityapplication.models.Cart;
import com.example.vebinar_10112022_springsecurityapplication.models.Order;
import com.example.vebinar_10112022_springsecurityapplication.models.Product;
import com.example.vebinar_10112022_springsecurityapplication.repositories.CartRepository;
import com.example.vebinar_10112022_springsecurityapplication.repositories.OrderRepository;
import com.example.vebinar_10112022_springsecurityapplication.security.PersonDetails;
import com.example.vebinar_10112022_springsecurityapplication.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Веб.10.11.2022 2часть 01:50 – реализуем компоненты приложения:
@Controller
public class UserController
{
        private final OrderRepository orderRepository; //Веб.29.11.22 2часть 1:25:55.
        private final CartRepository cartRepository; //Веб.29.11.22 2часть 43:35. Внедряем Репозиторий Cart.
        private final ProductService productService; //Внедряем ProductService ч/з Конструктор.
    @Autowired
    public UserController(OrderRepository orderRepository, CartRepository cartRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    @GetMapping("/index")
        public String index(Model model) //Веб.29.11.22 51:15  логика по выводу товаров в UserController: Добавим Модель и к этой модели обратимся на шаблоне:

    {
//Веб.10.11.22 2часть 52:35 --- поработаем с MainКонтроллером:
            //Получаем Объект Аутентификации -> С помощью Spring SecurityContextHolder обращаемся к контексту и на нем вызываем Метод Аутентификации:
            //Из потока для текущего пользователя получаем Объект, кот. был положен в сессию после Аутентификации:
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
//            System.out.println("ID пользователя: " + personDetails.getPerson().getId());
//            System.out.println("Логин пользователя: " + personDetails.getPerson().getLogin());
//            System.out.println("Пароль пользователя: " + personDetails.getPerson().getPassword());
//Веб.24.11.22 08:40 - получали Объект из сесии. Сейчас извлекем из нее роль:
            String role = personDetails.getPerson().getRole(); //Получили роль пользователя!
            if(role.equals("ROLE_ADMIN")) //Если роль = admin`у,
            {
                return "redirect:/admin";//то редирект на /admin.
            }
            model.addAttribute("products", productService.getAllProduct()); //29.11.22 51:51 - все товары будем класть в Модель и к этой Модели обращаться в рамках шаблона.
            return "user/index"; //Если роль - НЕ админ, то польз-ля перекидываем на гл.страницу л/к.
        }
//=========================================================================================================
//Веб.29.11.22 2часть 39:10  обработаем ссылку нажатия добавления товара в Корзину:
    @GetMapping("/cart/add/{id}")
    public String addProductInCart(@PathVariable("id") int id, Model model)
    {
        Product product = productService.getProductId(id); //Найти продукт, кот. хотим доб. в Корзину.

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        int id_person = personDetails.getPerson().getId(); //Извлекаем польз-ля (из сессии), кот. хочет доб. товар в Корзину.

        Cart cart = new Cart(id_person, product.getId()); //Созд. Объект Корзины.
        cartRepository.save(cart);
        return "redirect:/index"; //Я исправила  return "redirect:/cart"; на /index
    }

//Веб.29.11.22 2часть 45:00 - Обработаем Корзину (вывод товаров на страницу):
    @GetMapping("/cart")
    public String cart(Model model)
    {
        //Получаем ID пользователя:
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        int id_person = personDetails.getPerson().getId();
        //Получаем Корзину этого пользователя:
//Веб.29.11.22 2часть 47:25 – созд лист корзин:
        List<Cart> cartList = cartRepository.findByPersonId(id_person); //Извлекли все Корзины по ID пользователя.
        List<Product> productList = new ArrayList<>(); //47:40 - извлекаем все товары, чтобы вывести на страницу.
        for (Cart cart: cartList) //Перебираем все Корзины
        {   //доб. в Массив товаров:
            productList.add(productService.getProductId(cart.getProductId())); //Обращаемся к текущему элементы Корзины и вызывем геттер, кот. получает ID продукта.
        }
//Веб.29.11.22 2часть 1:03:50  сделаем подсчет суммы заказа:
        float price = 0;
        for (Product product : productList)
        {
            price += product.getPrice();
        }
        model.addAttribute("price", price);
        model.addAttribute("cart_product", productList); //доб. в Модель лист.
        return "user/cart"; //Возвращаем страницу с корзиной.
    }
//-----------------------------------------------
//Веб.29.11.22 2часть 52:00  обработаем удаление из Корзины:
    @GetMapping("/cart/delete/{id}")
    public String deleteProductCart(Model model, @PathVariable("id") int id)
    {
        //Получаем Объект пользователя, кот. хочет удалить товар:
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        int id_person = personDetails.getPerson().getId();

//        List<Cart> cartList = cartRepository.findByPersonId(id_person); //Получим Корзины, кот. доступны данному пользователю.
        cartRepository.deleteCartByProductId(id);
        return "redirect:/cart"; //после удаления товара из Корзины будем переходить на эту стр.
    }
//============================================================================
//Веб.29.11.22 2часть 1:18:30, 1:20:20 --- на Контроллере обработаем функционал по оформлению заказа:
    //Метод по
    @GetMapping("/order/create")
    public String order()
    {
        //Из сессии получаем ID пользователя (чтоб понимать, какой польз-ль делает заказ)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        int id_person = personDetails.getPerson().getId();

        //Подгружаем всю Корзину
        List<Cart> cartList = cartRepository.findByPersonId(id_person); //Вытягиваем всю корзину пользователя по его ID
        List<Product> productList = new ArrayList<>();
        //Получаем товары из Корзины по ID:
        for (Cart cart: cartList)
        {
            productList.add(productService.getProductId(cart.getProductId()));
        }
        //Считаем итоговую цену заказа, перебирая продукты:
        float price = 0;
        for (Product product : productList)
        {
            price += product.getPrice();
        }

        //Работа с номером заказа (1:23:00):
        String uuid = UUID.randomUUID().toString(); //Уник знач-е используем в качестве номера заказа.
        for (Product product : productList)
        {
            Order newOrder = new Order(uuid, product, personDetails.getPerson(), 1, product.getPrice(), Status.Получен); //При каждом новом заказе создаем Объект заказа.
            orderRepository.save(newOrder); //сохр созданный Объект.

            //Веб.29.11.22 2часть - раз продукт попал в заказ, то из корзины его надо удалить уже:
            cartRepository.deleteCartByProductId(product.getId());
        }
        return "redirect:/orders";
    }

//Веб. 29.11.22 2часть 1:27:55 – обработаем переход на страницу заказов:
    @GetMapping("/orders")
    public String orderUser(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal(); //Подтянули Объект пользовтеля.

        //Получить все заказы пользователя:
        List<Order> orderList = orderRepository.findByPerson(personDetails.getPerson()); //Создали лист с заказами пользователя.
        model.addAttribute("orders", orderList); //Лист заказов помещаем в Модель.
        return "/user/orders"; //Возвращаем шаблон orders. УБРАЛА / перед: "/user/orders" !!!!!!!!!!!!!
    }



//====================================================================================================
//Веб.22.11.22 2часть 43:25  на странице пользователя UserController создадим Метод, кот. позволит нам получить все товары:
//    @GetMapping("/product")
//    public String getAllProduct(Model model)
//    {
//        model.addAttribute("products", productService.getAllProduct());
//        return "/product/product";
//    }
////========================================================================
////Веб.24.11.22 2часть 1:33:00  реализуем, чтоб при нажатии на ссылку о товаре (из /product) подробная инфо о товаре выводилась:
//    //Метод по обработке нажатия на ссылку с подробной инфо о товаре (с /product):
//    @GetMapping("/product/info/{id}")
//    public String infoUser(@PathVariable("id") int id, Model model)
//    {
//        model.addAttribute("product", productService.getProductId(id));
//        return "product/infoProduct";
//    }
//Веб.29.11.22 50:35 -Это перенесем в ProductController.
}
