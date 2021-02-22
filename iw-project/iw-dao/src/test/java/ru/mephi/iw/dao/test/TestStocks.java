package ru.mephi.iw.dao.test;

import ru.mephi.iw.dao.initialization.Initial;

public class TestStocks {

    public static void main(String[] args) {
        System.out.println(Initial.stockMapper.selectAllStocksIMOEX(new int[] {1, 2, 3, 6, 22}));
        System.out.println(Initial.spMapper.selectAllSpLastIMOEX(new int[] {2, 15, 23}));
    }
}
