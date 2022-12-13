package com.example.vebinar_10112022_springsecurityapplication.controllers.admin;

import com.example.vebinar_10112022_springsecurityapplication.models.Image;
import com.example.vebinar_10112022_springsecurityapplication.models.Order;
import com.example.vebinar_10112022_springsecurityapplication.models.Person;
import com.example.vebinar_10112022_springsecurityapplication.models.Product;
import com.example.vebinar_10112022_springsecurityapplication.repositories.CategoryRepository;
import com.example.vebinar_10112022_springsecurityapplication.repositories.OrderRepository;
import com.example.vebinar_10112022_springsecurityapplication.repositories.PersonRepository;

import com.example.vebinar_10112022_springsecurityapplication.security.PersonDetails;
import com.example.vebinar_10112022_springsecurityapplication.services.PersonService;
import com.example.vebinar_10112022_springsecurityapplication.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

//Веб.15.11.22 2часть 48:15 – создадим Controller для админа:
@Controller
@RequestMapping("/admin") //Указываем, что будет работать, если в запросе будет /admin !
//@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')") //Также это можно повесить и на контроллер сюда.
public class AdminController
{

    private final OrderRepository orderRepository;
    private final PersonRepository personRepository;
    private final PersonService personService;

//    private final RoleRepository roleRepository;


//Веб.24.11.22 2часть 42:45  указ.инфо для ФОТО:
    @Value("${upload.path}")
    private String uploadPath; //Подгрузили путь к фото в поле Контроллера.
//----------------------------------------------------------------------------------------------------
// Веб.15.11.22 2часть 56:10, 57^35  есть и 2-ой способ АУТЕНТИФИКАЦИИ по РОЛЯМ:
//2-й способ – на основе аннотаций.
//@PreAuthorize("hasRole('ROLE_ADMIN')")
//@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')") //58:20-Таким образом можно указать, что к данному Методу будет доступ как у админа, так и у обычного юзера.
//@PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')") //Доступ есть И у админа, И у юзера.

//Веб.24.11.22 11:45  в админке реализуем добавление, редактирование, удаление и т.д. товаров:
    private final ProductService productService; //Внедрим ProductService ч/з Конструктор, откуда будем работать с Методами по товарам.
//Веб.29.11.22 22:40 – при добавлении товара у админа будет выпадающий список с КАТЕГОРИЯМИ товаров.
//Поэтому надо их получить в AdminController:
    private final CategoryRepository categoryRepository;
    @Autowired
    public AdminController(OrderRepository orderRepository, PersonRepository personRepository, PersonService personService, ProductService productService, CategoryRepository categoryRepository) {
        this.orderRepository = orderRepository;
        this.personRepository = personRepository;
        this.personService = personService;

        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }


//    @GetMapping("/admin") //При заходе на /admin ..
//    @GetMapping("")
//    public String admin() //..будет срабатывать Метод admin.
//    {
//        return "admin/admin"; //В нем будем возвращать шаблон админа.
//    }
//Веб.24.11.22 53:50 – сделаем, чтоб админ на своей гл странице мог просматривать инфо о товарах:
    @GetMapping("")
    public String admin(Model model)
    {
        model.addAttribute("products", productService.getAllProduct());
        //        !!!!!!!!!!!!!!!!!
        model.addAttribute("person", personService.getAllPerson());

        model.addAttribute("orders", orderRepository.findAll());
        return "admin/admin";
    }
//===============================================================================================
//Веб.24.11.22 13:55  в админке реализуем Методы из ProductService:

  //Метод по отображению страницы с возможностью ДобавлениЯ товара (http://localhost:8080/admin/product/add):
    @GetMapping("/product/add")
    public String addProduct(Model model)
    {
        model.addAttribute("product", new Product()); //Объект формы привязан к Объекту Модели.
        model.addAttribute("category", categoryRepository.findAll()); //Веб.29.11.22 23:10 - доб.Список КАТЕГОРИЙ.
        return "product/addProduct";
    }

//Веб.24.11.22 34:40 – Обработаем форму addProduct на Контроллере admin (созд. Метод):
    //Метод по ДОБАВЛЕНИЮ товара в БД через Сервис->Репозиторий (http://localhost:8080/admin/product/add):
    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("file_one")MultipartFile file_one, @RequestParam("file_two")MultipartFile file_two, @RequestParam("file_three")MultipartFile file_three, @RequestParam("file_four")MultipartFile file_four, @RequestParam("file_five")MultipartFile file_five) throws IOException //Получаем товар с формы. //Веб.24.11.22 2часть 44:30 - доб.код по ФОТО. MultipartFile позволялет получить файл с формы.
    {
////Веб.24.11.22 1:03:00 – добавим валидацию: пока удалили.
//Веб.24.11.22 08:00 --- добавим ВАЛИДАЦИЮ:
        if(bindingResult.hasErrors())//Если есть ошибки,
        {
            return "product/addProduct";//..то идем на этот шаблон.
        }

//Веб.24.11.22 2часть 46:30 - по ФОТО:
        if(file_one != null)
        {
            File uploadDir = new File(uploadPath);//После получения файла преобразуем переменную пути в путь директории.
            if(!uploadDir.exists()) //если такая директория есть,с ней работаем
            {
                uploadDir.mkdir(); //если нет, то ее создаем.
            }
            String uuidFile = UUID.randomUUID().toString();//получаем уникальное значение.
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();//и прибавляем наименование файла.
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));//сохраняем файл по пути uploadPath и к ней добавляем имя.

            Image image = new Image(); //Созд.Объет Модели.
            image.setProduct(product);//обращаемся к модели image и помещаем туда product (связь с продуктами). Задаем продукт, к которому будет привязано ФОТО (50:00).
            image.setFileName(resultFileName); //Указ. наименование ФОТО в папке.
            //Веб.24.11.22 2часть 53:10 - вызываем Метод из класса Product
            product.addImageToProduct(image);
        }

        if(file_two != null)
        {
            File uploadDir = new File(uploadPath);//После получения файла преобразуем переменную пути в путь директории.
            if(!uploadDir.exists()) //если такая директория есть,с ней работаем
            {
                uploadDir.mkdir(); //если нет, то ее создаем.
            }
            String uuidFile = UUID.randomUUID().toString();//получаем уникальное значение.
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();//и прибавляем наименование файла.
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));//сохраняем файл по пути uploadPath и к ней добавляем имя.

            Image image = new Image(); //Созд.Объет Модели.
            image.setProduct(product);//обращаемся к модели image и помещаем туда product (связь с продуктами). Задаем продукт, к которому будет привязано ФОТО (50:00).
            image.setFileName(resultFileName); //Указ. наименование ФОТО в папке.
            //Веб.24.11.22 2часть 53:10 - вызываем Метод из класса Product
            product.addImageToProduct(image);
        }

        if(file_three != null)
        {
            File uploadDir = new File(uploadPath);//После получения файла преобразуем переменную пути в путь директории.
            if(!uploadDir.exists()) //если такая директория есть,с ней работаем
            {
                uploadDir.mkdir(); //если нет, то ее создаем.
            }
            String uuidFile = UUID.randomUUID().toString();//получаем уникальное значение.
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();//и прибавляем наименование файла.
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));//сохраняем файл по пути uploadPath и к ней добавляем имя.

            Image image = new Image(); //Созд.Объет Модели.
            image.setProduct(product);//обращаемся к модели image и помещаем туда product (связь с продуктами). Задаем продукт, к которому будет привязано ФОТО (50:00).
            image.setFileName(resultFileName); //Указ. наименование ФОТО в папке.
            //Веб.24.11.22 2часть 53:10 - вызываем Метод из класса Product
            product.addImageToProduct(image);
        }

        if(file_four != null)
        {
            File uploadDir = new File(uploadPath);//После получения файла преобразуем переменную пути в путь директории.
            if(!uploadDir.exists()) //если такая директория есть,с ней работаем
            {
                uploadDir.mkdir(); //если нет, то ее создаем.
            }
            String uuidFile = UUID.randomUUID().toString();//получаем уникальное значение.
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();//и прибавляем наименование файла.
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));//сохраняем файл по пути uploadPath и к ней добавляем имя.

            Image image = new Image(); //Созд.Объет Модели.
            image.setProduct(product);//обращаемся к модели image и помещаем туда product (связь с продуктами). Задаем продукт, к которому будет привязано ФОТО (50:00).
            image.setFileName(resultFileName); //Указ. наименование ФОТО в папке.
            //Веб.24.11.22 2часть 53:10 - вызываем Метод из класса Product
            product.addImageToProduct(image);
        }

        if(file_five != null)
        {
            File uploadDir = new File(uploadPath);//После получения файла преобразуем переменную пути в путь директории.
            if(!uploadDir.exists()) //если такая директория есть,с ней работаем
            {
                uploadDir.mkdir(); //если нет, то ее создаем.
            }
            String uuidFile = UUID.randomUUID().toString();//получаем уникальное значение.
            String resultFileName = uuidFile + "." + file_five.getOriginalFilename();//и прибавляем наименование файла.
            file_five.transferTo(new File(uploadPath + "/" + resultFileName));//сохраняем файл по пути uploadPath и к ней добавляем имя.

            Image image = new Image(); //Созд.Объет Модели.
            image.setProduct(product);//обращаемся к модели image и помещаем туда product (связь с продуктами). Задаем продукт, к которому будет привязано ФОТО (50:00).
            image.setFileName(resultFileName); //Указ. наименование ФОТО в папке.
            //Веб.24.11.22 2часть 53:10 - вызываем Метод из класса Product
            product.addImageToProduct(image);
        }

        productService.saveProduct(product);
        return "redirect:/admin"; //при сохранении товара редиректимся на /admin
    }
//----------------------------------------------------------------------------------------------------
//Веб.24.11.22 52:20 – продолжим писать Методы админа:
    //Метод по УДАЛЕНИЮ товара:
    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id)
    {
        productService.deleteProduct(id);
        return "redirect:/admin";
    }
//----------------------------------------------------------------------------------------------------
//Веб.24.11.22 2часть 00:02 – реализуем редактирование товара (админ):
    //Метод по отображению страницы с возможностью РедактированиЯ товара:
    @GetMapping("/product/edit/{id}")
    public String editProduct(Model model, @PathVariable("id") int id)
    {
        model.addAttribute("product", productService.getProductId(id)); //Для редактирования надо получить товар по id, отправить его в Модель, с формы обратиться к этому Объекту и привязать к полям (чтобы вставились значения при редактировании).
//Веб.29.11.22 43:50 - чтоб на стр.редактирования работала категория:
        model.addAttribute("category", categoryRepository.findAll()); //В выпадающем списке отобразить перечень всех категорий.
        return "product/editProduct";
    }

//Веб.24.11.22 2часть 03:15 – созд. Метод по обработке editProduct:
    //Метод по РЕДАКТИРОВАНИЮ(обновлению) товара в БД:
    @PostMapping("/product/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") int id) //Получаем товар с формы редактирования.
    {
        if(bindingResult.hasErrors())
        {
            return "product/editProduct";
        }
        productService.updateProduct(id, product);
        return "redirect:/admin";
    }
//-------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------
//Веб. 29.11.22 2часть 1:27:55 – обработаем переход на страницу заказов (как у юзера!!!!!!):
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


    //!!!!!!!!!!!!
//    @PostMapping("/search")
//    public String productSearch(@RequestParam("searchOrdersByNumber") String searchOrdersByNumber, Model model)
//    {
//
//    }



//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// Редактирование|смена роли пользователя админом:
//Метод по отображению страницы с возможностью РедактированиЯ РОЛИ пользователя:
//    @GetMapping("/person/edit/{id}")
//    public String editPerson(Model model, @PathVariable("id") int id)
//    {
//        model.addAttribute("person", personService.getPersonId(id)); //Для редактирования надо получить товар по id, отправить его в Модель, с формы обратиться к этому Объекту и привязать к полям (чтобы вставились значения при редактировании).
////Веб.29.11.22 43:50 - чтоб на стр.редактирования работала категория:
//        model.addAttribute("role", personRepository.findAll()); //В выпадающем списке отобразить перечень всех ролей.
//        return "user/editRole";
//    }

    //Метод по обработке editRole: !!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //Метод по смене РОЛИ пользователю админом:
//    @PostMapping("/person/edit/{id}")
//    public String editPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) //Получаю пользователя с формы редактирования editRole.
//    {
//        if(bindingResult.hasErrors())
//        {
//            return "user/editRole";
//        }
//        personService.updatePerson(id, person);
//        return "redirect:/admin";
//    }
//================================================================

}
