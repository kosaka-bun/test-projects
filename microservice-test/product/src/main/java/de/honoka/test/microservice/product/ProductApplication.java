package de.honoka.test.microservice.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@MapperScan("de.honoka.test.microservice.product.dao")
@SpringBootApplication
public class ProductApplication {

    public static ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ProductApplication.class);
        applicationContext = app.run(args);
    }
}
