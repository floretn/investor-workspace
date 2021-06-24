package ru.mephi.iw.work_classes;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Percentage extends Number {

    private final double amount;

    public Percentage(double amount) {
        this.amount = amount;
    }

    @Override
    public int intValue() {
        return (int) amount;
    }

    @Override
    public long longValue() {
        return intValue();
    }

    @Override
    public float floatValue() {
        return (float) amount;
    }

    @Override
    public double doubleValue() {
        return BigDecimal.valueOf(amount)
                .setScale(2, RoundingMode.DOWN).doubleValue();
    }

    @Override
    public String toString() {
        return BigDecimal.valueOf(amount)
                .setScale(2, RoundingMode.DOWN).toString();
    }
}
