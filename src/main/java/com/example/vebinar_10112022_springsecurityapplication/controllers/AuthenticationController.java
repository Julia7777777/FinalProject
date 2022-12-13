package com.example.vebinar_10112022_springsecurityapplication.controllers;

import com.example.vebinar_10112022_springsecurityapplication.models.Person;
import com.example.vebinar_10112022_springsecurityapplication.services.PersonService;
import com.example.vebinar_10112022_springsecurityapplication.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.incrementer.AbstractDataFieldMaxValueIncrementer;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

//Веб.15.11.2022 10:00 --- переопределим форму Sign in аутентификации (под себя, разработаем свою):
//Для этого создадим отдельный Контроллер:
@Controller
@RequestMapping("/authentication")
public class AuthenticationController
{
    private final PersonValidator personValidator; //Веб.15.11.22 1:19:22_внедрим валидатор.
    private final PersonService personService; //и сервис сразу подключим.

    @Autowired
    public AuthenticationController(PersonValidator personValidator, PersonService personService) {
        this.personValidator = personValidator;
        this.personService = personService;
    }
//Веб.15.11.22 2часть 33:50 – АВТОРИЗАЦИЯ с помощью РОЛЕЙ:
//    ROLE_ADMIN
//    ROLE_USER
    @GetMapping("/login")
    public String login()
    {
        return "authentication/login";
    }

//    Веб.15.11.22 58:55 - реализуем РЕГИСТРАЦИЮ (2 способа передачи Объекта):
    //1
//    @GetMapping("/registration")
//    public String registration(Model model)
//    {
//        model.addAttribute("person", new Person());
//    }
    //2
    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") Person person)
    {
        return "authentication/registration";
    }

//    Веб.15.11.22 1:20:05 – обработаем на Контроллере форму Регистрации:
    @PostMapping("/registration")
    public String resultRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult)
    {
       personValidator.validate(person, bindingResult); //Если валидатор возвращает ошибку, помещаем ее в bindingResult.
        if(bindingResult.hasErrors())
        {
            return "authentication/registration";
        }
        personService.register(person);//Если ошибок нет, обращаемся к сервису и передаем Объект.
        return "redirect:/index";
    }
}

//http:localhost:8080/authentication/login   -- по этому адресу будем заходить на тсрицу, кот. будет отображасть собственную форму входа в систему.