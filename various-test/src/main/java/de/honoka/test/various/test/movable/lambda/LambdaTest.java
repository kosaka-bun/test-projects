package de.honoka.test.various.test.movable.lambda;

import org.junit.Test;

public class LambdaTest {

    @Test
    public void test1() {
        LambdaReceiver<JavaBean> jReceiver = new LambdaReceiver<>();
        jReceiver.receive(JavaBean::getProp);
    }
}
