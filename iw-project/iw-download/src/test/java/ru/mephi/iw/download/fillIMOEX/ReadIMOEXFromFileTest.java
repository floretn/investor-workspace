package ru.mephi.iw.download.fillIMOEX;

import org.junit.*;
import ru.mephi.iw.constants.CurrencyKeys;
import ru.mephi.iw.models.stocks.Stock;
import ru.mephi.iw.models.stocks.StocksInIndexes;
import ru.mephi.iw.models.stocks.StocksPrices;
import ru.mephi.iw.models.stocks.associations.PriceStockInIndex;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ReadIMOEXFromFileTest {

    Timestamp timestamp = new Timestamp(new Date().getTime());

    PriceStockInIndex priceStockInIndex1 = new PriceStockInIndex(0, null,
            StocksInIndexes.builder().numberOfStocksInIndex(1).build(),
            Stock.builder().companyId(null).ticker("ticker1").build(), StocksPrices.builder().currencyId(CurrencyKeys.RUBLES_KEY).
            settingTime(timestamp).price(1).build());

    PriceStockInIndex priceStockInIndex2 = new PriceStockInIndex(0, null,
            StocksInIndexes.builder().numberOfStocksInIndex(2).build(),
            Stock.builder().companyId(null).ticker("ticker2").build(), StocksPrices.builder().currencyId(CurrencyKeys.RUBLES_KEY).
            settingTime(timestamp).price(2).build());

    PriceStockInIndex priceStockInIndex3 = new PriceStockInIndex(0, null,
            StocksInIndexes.builder().numberOfStocksInIndex(3).build(),
            Stock.builder().companyId(null).ticker("ticker3").build(), StocksPrices.builder().currencyId(CurrencyKeys.RUBLES_KEY).
            settingTime(timestamp).price(3).build());

    @Test
    public void test1() throws IOException {
        File xlsxForTest = new File(getClass().getClassLoader().getResource("test1.xlsx").getFile());
        List<PriceStockInIndex> priceStockInIndices = new ArrayList<>(Arrays.asList(priceStockInIndex1));
        Assert.assertEquals(priceStockInIndices, new ReadIMOEXFromFile().readXlsx(xlsxForTest, timestamp));
    }

    @Test
    public void test2() throws IOException {
        File xlsxForTest = new File(getClass().getClassLoader().getResource("test2.xlsx").getFile());
        List<PriceStockInIndex> priceStockInIndices = new ArrayList<>(Arrays.asList(priceStockInIndex1, priceStockInIndex2));
        Assert.assertEquals(priceStockInIndices, new ReadIMOEXFromFile().readXlsx(xlsxForTest, timestamp));
    }

    @Test
    public void test3() throws IOException {
        File xlsxForTest = new File(getClass().getClassLoader().getResource("test3.xlsx").getFile());
        List<PriceStockInIndex> priceStockInIndices = new ArrayList<>(Arrays.asList(priceStockInIndex1, priceStockInIndex1,
                priceStockInIndex2, priceStockInIndex3));
        Assert.assertEquals(priceStockInIndices, new ReadIMOEXFromFile().readXlsx(xlsxForTest, timestamp));
    }

    @Test
    public void test4() throws IOException {
        File xlsxForTest = new File(getClass().getClassLoader().getResource("test4.xlsx").getFile());
        List<PriceStockInIndex> priceStockInIndices = new ArrayList<>(Arrays.asList(priceStockInIndex1,
                priceStockInIndex2, priceStockInIndex3, priceStockInIndex3));
        Assert.assertEquals(priceStockInIndices, new ReadIMOEXFromFile().readXlsx(xlsxForTest, timestamp));
    }

    @Test
    public void test5() throws IOException {
        File xlsxForTest = new File(getClass().getClassLoader().getResource("test5.xlsx").getFile());
        List<PriceStockInIndex> priceStockInIndices = new ArrayList<>(Arrays.asList(priceStockInIndex1,
                priceStockInIndex2, priceStockInIndex2, priceStockInIndex3));
        Assert.assertEquals(priceStockInIndices, new ReadIMOEXFromFile().readXlsx(xlsxForTest, timestamp));
    }
}