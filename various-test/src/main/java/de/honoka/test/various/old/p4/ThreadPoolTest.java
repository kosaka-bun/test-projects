package de.honoka.test.various.old.p4;

import de.honoka.sdk.util.code.CodeUtils;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolTest {

    private final ThreadPoolExecutor threadPoolExecutor =
            (ThreadPoolExecutor) Executors.newCachedThreadPool();

    @Test
    public void test1() {
        for(int i = 0; i < 10; i++) {
            int finalI = i + 1;
            threadPoolExecutor.submit(() -> {
                System.out.println(finalI + ".start");
                CodeUtils.threadSleep(1000);
                System.out.println(finalI + ".finished");
                System.out.println(threadPoolExecutor.getPoolSize());
            });
            CodeUtils.threadSleep(2000);
        }
        CodeUtils.threadSleep(70 * 1000);
    }
}
