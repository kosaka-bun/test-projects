package de.honoka.test.spring.component;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Bean1 implements ApplicationRunner {

    @Resource
    private Bean2 bean2;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("Bean1");
        System.out.println(bean2);
    }

    @Override
    public String toString() {
        return "Bean1: bean2=" + (bean2 == null ? "null" : bean2.hashCode());
    }
}
