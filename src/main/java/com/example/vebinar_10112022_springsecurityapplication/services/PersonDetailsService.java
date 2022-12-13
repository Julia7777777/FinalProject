package com.example.vebinar_10112022_springsecurityapplication.services;

import com.example.vebinar_10112022_springsecurityapplication.models.Person;
import com.example.vebinar_10112022_springsecurityapplication.repositories.PersonRepository;
import com.example.vebinar_10112022_springsecurityapplication.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//Веб.10.11.22 2ч 28:45 --- сформируем сервис по работе с Репозиторием (этот сервис будет работать в связке с PersonDetails):
@Service
public class PersonDetailsService implements UserDetailsService
{
    private final PersonRepository personRepository;//внедряем зависимость Репозитория.

    @Autowired
    public PersonDetailsService(PersonRepository personRepository1)
    {
        this.personRepository = personRepository1;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Получаем пльзователя из таблицы по логину с формы аутентификации:
        Optional<Person> person = personRepository.findByLogin(username);

        //Если пользователь НЕ был найден,
        if (person.isEmpty())
        {   //выбрасываем исключение, что данный пользователь НЕ найден:
            //Данное исключение будет поймано Spring Security и сообщение будет выведено на станицу:
            throw new UsernameNotFoundException("Пользователь НЕ найден!");
        }
        return new PersonDetails(person.get());
    }
}
