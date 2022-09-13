package de.honoka.test.various.old.p3;

import de.honoka.sdk.util.system.gui.ConsoleWindow;
import lombok.SneakyThrows;

public class GuiTest {

    public static ConsoleWindow consoleWindow;

    static {
        consoleWindow = new ConsoleWindow("Test");
        consoleWindow.setAutoScroll(true);
        consoleWindow.setScreenZoomScale(1.25);
        consoleWindow.show();
    }

    public static void main(String[] args) {
        test1();
    }

    @SneakyThrows
    public static void test1() {
        System.out.println("上↑下↓左←右→");
        System.out.println("上↑\t下↓\t左←\t右→");
        //consoleWindow.setTextPaneFont("Consolas");
        System.out.println("上↑下↓左←右→");
        System.out.println("上↑\t下↓\t左←\t右→");
        System.out.println("--------------------".repeat(5));
        new Exception().printStackTrace();
    }
}
