package com.example.vebinar_10112022_springsecurityapplication.services;
//Веб.22.11.22 2часть 40:55  созд. сервисный слой (продукты) по работе с Репозиторием:

import com.example.vebinar_10112022_springsecurityapplication.models.Product;
import com.example.vebinar_10112022_springsecurityapplication.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
    public class ProductService
    {
        private final ProductRepository productRepository; //Внедряем Репозиторий ч/з Конструктор.

        @Autowired
        public ProductService(ProductRepository productRepository) {
            this.productRepository = productRepository;
        }
//================================================================================================
//Веб.22.11.22 2часть 43:25  реализуем Методы по работе с товарами:

        //Метод по возврату(получению) ВСЕХ товаров:
        public List<Product> getAllProduct()
        {
            return productRepository.findAll();
        }

        //Метод по возврату(получению) товара по id:
        public Product getProductId(int id)
        {
            Optional<Product> optionalProduct = productRepository.findById(id);
            return optionalProduct.orElse(null);
        }

        //Метод по сохранению товара:
        @Transactional
        public void saveProduct(Product product)
        {
            productRepository.save(product);
        }

        //Метод по обновлению(редактированию) данных товара:
        @Transactional
        public void updateProduct(int id, Product product)
        {
            product.setId(id);
            productRepository.save(product); //веб.24.11.22 2часть 06:00 (добавили).
        }

        //Метод по удалению товара по id:
        @Transactional
        public void deleteProduct(int id)
        {
            productRepository.deleteById(id);
        }

    }



