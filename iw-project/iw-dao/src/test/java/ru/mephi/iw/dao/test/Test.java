package ru.mephi.iw.dao.test;

import ru.mephi.iw.dao.initialization.Initial;

public class Test {
    public static void main(String[] args) {
        System.out.println(Initial.stockMapper.selectAllStocks());
    }
}
