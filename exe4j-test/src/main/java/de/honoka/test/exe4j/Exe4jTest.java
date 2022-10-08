package de.honoka.test.exe4j;

import de.honoka.sdk.util.system.gui.ConsoleWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Exe4jTest {

    public static void main(String[] args) {
        ConsoleWindow window = new ConsoleWindow("Test",
                null, () -> {});
        window.setAutoScroll(true);
        window.setScreenZoomScale(1.25);
        window.show();
        SpringApplication.run(Exe4jTest.class, args);
    }
}
