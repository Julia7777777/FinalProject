package com.example.vebinar_10112022_springsecurityapplication.services;

import com.example.vebinar_10112022_springsecurityapplication.models.Person;
import com.example.vebinar_10112022_springsecurityapplication.models.Product;
import com.example.vebinar_10112022_springsecurityapplication.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//Веб.15.11.22 1:13:05 --- созд. Свой Сервис под РЕГИСТРАЦИЮ:
@Service
public class PersonService
{
    private final PersonRepository personRepository; //внедряем Репозиторий через конструктор.
//    Веб.15.11.22 21:50 - ШИФРОВАНИЕ паролей, идем в PersonService:
    private final PasswordEncoder passwordEncoder; //внедряем passwordEncoder, кот. в SecurityConfig.

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Person findByLogin(Person person)
    {
       Optional<Person> person_db = personRepository.findByLogin(person.getLogin()); //Метод, позволяющий вернуть пользователя по логину.
        return person_db.orElse(null);
    }

    @Transactional
    //Специальный Метод по регистрации (будет сохранять тот Объект, кот. сюда придет):
    public void register(Person person)
    {
        person.setPassword(passwordEncoder.encode(person.getPassword()));//веб.15.11.22 2часть 22:55. Шифруем пароль.

//Веб.15.11.22 2часть 38:00 --- реализуем 2 роли: ADMIN (будет выдаваться только админам) и USER (будет выдаваться всем юзерам по умолчанию):
        person.setRole("ROLE_USER"); //По умолчанию все пользователи будут региться как USER.

        personRepository.save(person); //Пароль уже сохранится не открытый, а зашифрованный!
    }


//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public List<Person> getAllPerson() {
        return personRepository.findAll();
    }

//!!!!!!!!!!!!!!!!!!
//Метод по получению пользователя по ID:
//   public Person getPersonId(int id)
//{
//    Optional<Person> optionalPerson = personRepository.findById(id);
//    return optionalPerson.orElse(null);
//}

    //Метод по редактированию роли пользователя админом:
//    @Transactional
//    public void updatePerson(int id, Person person)
//    {
////        person.getId();
//        person.setId(id);
//        personRepository.save(person);
//    }
//
//    @Transactional
//    public void savePerson(Person person)
//    {
//        personRepository.save(person);
//    }

}
