package com.example.vebinar_10112022_springsecurityapplication.util;
//Веб.15.11.22 1:07:26 - про кастомную валидацию:

import com.example.vebinar_10112022_springsecurityapplication.models.Person;
import com.example.vebinar_10112022_springsecurityapplication.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator
{
//    Веб.15.11.22 1:16:00 – далее валидатор дописываем:
    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    //В данном Методе указываем, для какой Модели предназначен данный валидатор:
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }
//В данном Методе прописываем правила валидации:
    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person)target;
        if(personService.findByLogin(person) != null)
        {
            errors.rejectValue("login", "", "Данный логин уже занят.");
        }
    }
}
