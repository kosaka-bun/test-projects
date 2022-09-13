package de.honoka.test.various.test.movable.lambda;

import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.SneakyThrows;

import java.lang.invoke.SerializedLambda;
import java.util.Arrays;

@SuppressWarnings({ "ConstantConditions", "RedundantSuppression" })
public class LambdaReceiver<T> {

    @SneakyThrows
    public void receive(SFunction<T, ?> lambda) {
        System.out.println(lambda.getClass().getName());
        System.out.println(Arrays.toString(lambda.getClass().getDeclaredMethods()));
        SerializedLambda sLambda = (SerializedLambda) ReflectionKit.setAccessible(
                lambda.getClass().getDeclaredMethod("writeReplace"))
                .invoke(lambda);
        //SerializedLambda sLambda = (SerializedLambda) (Object) lambda;
        System.out.println(sLambda.getImplMethodName());
    }
}
