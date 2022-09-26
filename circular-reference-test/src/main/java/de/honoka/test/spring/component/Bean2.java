package de.honoka.test.spring.component;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Bean2 implements ApplicationRunner {

    @Lazy
    @Resource
    private Bean1 bean1;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("Bean2");
        System.out.println(bean1);
    }

    @Override
    public String toString() {
        return "Bean2: bean1=" + (bean1 == null ? "null" : bean1.hashCode());
    }
}
