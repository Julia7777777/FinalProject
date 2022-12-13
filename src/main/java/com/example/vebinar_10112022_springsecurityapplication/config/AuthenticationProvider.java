//package com.example.vebinar_10112022_springsecurityapplication.config;
//
//import com.example.vebinar_10112022_springsecurityapplication.services.PersonDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//
////Веб.10.11.22 2ч 23:15 --- созд. Файл Конфигурации, кот. будет отвечать за логику Аутентификации приложения:
//@Component
//public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider
//{
//    //Веб.10.11.22 2ч 35:25 --
//    private final PersonDetailsService personDetailsService; //внедряем через конструктор Объект personDetailsService.
//    @Autowired
//    public AuthenticationProvider(PersonDetailsService personDetailsService) {
//        this.personDetailsService = personDetailsService;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        //Получаем логин с формы аутентификации. За нас Spring Security сам возьмет Объект из формы и передаст его сюда:
//        String login = authentication.getName();
//
//        //Получаем запись найденного пользователя по ЛОГИНУ:
//        UserDetails person = personDetailsService.loadUserByUsername(login); //веб.10.11.22 2ч 36:20.
//
//        //Получаем Объект ПАРОЛЬ:
//        String password = authentication.getCredentials().toString();
//
//        if(!password.equals(person.getPassword())) //Если пароли НЕ совпадают,
//        {//выбрасываем исключение:
//            throw new BadCredentialsException("НЕкорретный пароль!");
//        }
//// Возвращаем Объект аутентификации. В данном Объекте будет лежать Объект Модели, пароль, права доступа -> пока ролей нет:
//        //Даннй Объект будет помещен в сессию (веб.10.11.22 2ч 43:41):
//        return new UsernamePasswordAuthenticationToken(person,password, Collections.emptyList());
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return true; //Данная аутентификация будет работать во всех случаях. Меняем false на true.
//    }
//}
