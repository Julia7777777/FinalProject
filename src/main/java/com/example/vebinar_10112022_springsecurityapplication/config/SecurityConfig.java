package com.example.vebinar_10112022_springsecurityapplication.config;

import com.example.vebinar_10112022_springsecurityapplication.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//Веб.10.11.22 2ч 20:40 --- созд. Файл Конфигурации (в данном файле прописываем всю инфо по безопасности нашего приложения):
@EnableWebSecurity
//Веб.15.11.22 2часть 56:10  есть и 2-ой способ АУТЕНТИФИКАЦИИ по РОЛЯМ:
//2-й способ – на основе аннотаций:
//@EnableGlobalMethodSecurity(prePostEnabled = true) //позволяет подключить разграничение прав на опред.методу контроллера на основе аннотации. Аннотацию по разграничению ролей можно повесить как на Метод контроллера, но лучше вешать в спец сервис на админа (мы сервис не будем создавать).

//public class SecurityConfig extends WebSecurityConfiguration
//    Веб.15.11.22 27:00 – в конф. Файле SecurityConfig надо прописать, каким образом форма login будет открываться:
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
//    //Веб. 10.11.22 2ч 41:00 – реализуем SecurityConfig
//    private final AuthenticationProvider authenticationProvider;
//
//    public SecurityConfig(AuthenticationProvider authenticationProvider) {
//        this.authenticationProvider = authenticationProvider;
//    }

    //Веб.10.11.2022 2часть 59:05 – реализуем автоматич. Аутентификацию пользователя:
    //Класс AutenticationProvider закомментим.
    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }


    @Override
    public void configure(WebSecurity web)
    {
        web.ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/icon/**");
    }

//"/css/**", "/js/**", "/resources/**", "/static/**"

//    Веб.15.11.22 27:00 – в конф. Файле SecurityConfig надо прописать, каким образом форма login будет открываться:
    //Конфигурация Spring Security:
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        //Указываем, на какой URL-адрес фильтр Spring Security будет отправлять неаутентифицированного пользователя при заходе на защищенную страницу:
//Веб.15.11.22 45:00 – надо произвести еще настройку в конф. Файле SecurityConfig:
//        httpSecurity.csrf().disable() //Отключаем защиту от межсайтовой подделки запросов. Убираем-веб.15.11.22 2часть 1:10:35.

//Веб.15.11.22 2часть 1:03:17  поговорим про CSRF (Cross-Site Request Forgery):
//Это межсайтовая подделка запросов. Веб. 15.11.22 1:10:20  внедрим CSRF-токен в форму Аутентификации:
        httpSecurity

                //Указываем, что все страницы будут защищены процессом аутентификации:
                . authorizeRequests()

                //Указываем, что данные страницы доступны всем пользователям:
                .antMatchers("/authentication/login", "/authentication/registration", "/error", "/product", "/img/**", "/product/info/{id}", "/product/search", "/resources/**", "/static/**", "/css/**", "/js/**", "/icon/**").permitAll()

                //Веб.15.11.22 2часть 50:20 - внедрим работу с РОЛЯМИ (делаем разграничение по ролям):
                .antMatchers("/admin").hasRole("ADMIN") //Указываем, что страница /admin доступна пользователю с ролью ADMIN.

                .anyRequest().hasAnyRole("USER", "ADMIN") //Указываем, что все остальные страницы доступны пользователям с ролью USER и ADMIN.

//                //Указываем, что для всех остальных страниц необходимо вызывать Метод authenticated(), который открывает форму аутентификации: //Веб.15.11.22 2часть 49:50 - это удаляем уже, т.к. переход на страницы будет происходить уже с учетом РОЛЕЙ.
//                .anyRequest().authenticated()

    //Переходим к след.блоку и указываем and:

                .and()
        .formLogin().loginPage("/authentication/login")
//        httpSecurity.formLogin().loginPage("/authentication/login")
        //Указываем, на какой URL-фдрес будут отпарвляться данные с  формы аутентификации:
                .loginProcessingUrl("/process_login")
        //Указываем, на какой URL-адрес необходимо направить пользователя после успешной аутентификации:
                .defaultSuccessUrl("/index", true)
        //Указываем, куда необходимо перейти при неверной аутентификации:
                .failureUrl("/authentication/login?error")

//Веб.15.11.22 2часть - реализуем LogOUT (позволяет выйти из л.к.):
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/authentication/login");
    }


    //Метод, кот. будет являться настройкой Аутентификации:
    //Данный Метод позволяет настроить Аутентификацию:
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception
    {
//        authenticationManagerBuilder.authenticationProvider(authenticationProvider); //для кастомной аутентификации
//        authenticationManagerBuilder.userDetailsService(personDetailsService);//Логику уже НЕ сами прописываем (как делали в AutenticationProvider), а делегируем сервису personDetailsService.

//Веб.15.11.22 2часть 24:00 – при Аутентификации также используем ШИФРОВАНИЕ пароля:
        authenticationManagerBuilder.userDetailsService(personDetailsService).passwordEncoder(getPasswordEncoder());
    }

//    Веб.10.11.22 2часть 1:03:20 --  Пока отключим шифрование паролей:
//    @Bean
//    public PasswordEncoder getPasswordEncoder()
//    {
//        return NoOpPasswordEncoder.getInstance();
//    }

//Веб.15.11.22 2часть  09:00, 19:30 - ШИФРОВАНИЕ паролей:
    @Bean
    public PasswordEncoder getPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

}
