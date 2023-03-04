package de.honoka.test.various.test.movable;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class BigDecimalTest {

    private final BigDecimal[] decimals;

    private final String[] values = {
            "0", "1.2E3", "1.23456E2", "12.34567",
            "0.1333333333333333333333333333333333"
    };

    {
        List<BigDecimal> decimalList = new ArrayList<>();
        for(String value : values) {
            decimalList.add(new BigDecimal(value));
        }
        decimals = decimalList.toArray(new BigDecimal[0]);
    }

    @Test
    public void main() {
        halfUp();
        printAll();
    }

    private void printAll() {
        for(BigDecimal decimal : decimals) {
            System.out.println("normal: " + decimal);
            System.out.println("plain: " + decimal.toPlainString());
            System.out.println("enginnering: " + decimal.toEngineeringString());
            System.out.println();
        }
    }

    private void halfUp() {
        for(BigDecimal decimal : decimals) {
            System.out.println(decimal.setScale(2, RoundingMode.HALF_UP));
        }
        System.out.println();
    }
}
