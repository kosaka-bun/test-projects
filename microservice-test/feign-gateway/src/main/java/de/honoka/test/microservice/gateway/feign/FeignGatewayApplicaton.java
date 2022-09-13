package de.honoka.test.microservice.gateway.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FeignGatewayApplicaton {

    public static void main(String[] args) {
        SpringApplication.run(FeignGatewayApplicaton.class, args);
    }
}
