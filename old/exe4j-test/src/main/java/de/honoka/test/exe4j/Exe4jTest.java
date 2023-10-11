package de.honoka.test.exe4j;

import de.honoka.sdk.util.system.gui.ConsoleWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Exe4jTest {

    public static void main(String[] args) {
        ConsoleWindow.Builder.of("Test").setOnExit(() -> {
            System.out.println("系统退出");
        }).setScreenZoomScale(1.25).build();
        SpringApplication.run(Exe4jTest.class, args);
    }
}
