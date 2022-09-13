package de.honoka.test.microservice.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@MapperScan("de.honoka.test.microservice.order.dao")
@SpringBootApplication
public class OrderApplication {

    public static ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(OrderApplication.class);
        applicationContext = app.run(args);
    }
}
