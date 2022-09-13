package de.honoka.test.various.old.p4;

import org.apache.commons.lang3.RandomUtils;

public class RandomTest {

    public static void main(String[] args) {
        int amount = 1000;
        for(int count = 1; amount > 0; count++) {
            double random = RandomUtils.nextDouble(0, 1);
            if(random >= 0.5) {
                amount++;
                System.out.println(count + ". Win! amount=" + amount);
            } else {
                amount--;
                System.out.println(count + ". Lose, amount=" + amount);
            }
            if(amount >= 3000) break;
            //CodeUtils.threadSleep(10);
        }
        System.out.println("amount=" + amount);
    }
}
