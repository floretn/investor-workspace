package main;

import dao.iw.methods.*;
import models.*;
import org.apache.ibatis.javassist.tools.reflect.CannotInvokeException;
import javax.swing.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

public class TestWorkWithDB {

    public static void main(String[] args) throws IOException {
        CompanyMethods companyMethods = new CompanyMethods();
        System.out.println(companyMethods.selectCompany(1));
        CurrencyMethods currencyMethods = new CurrencyMethods();
        System.out.println(currencyMethods.selectCurrency(1));
        DoicMethods doicMethods = new DoicMethods();
        System.out.println(doicMethods.selectDoic(1));
        IndexMethods indexMethods = new IndexMethods();
        System.out.println(indexMethods.selectIndex(1));
        SectorMethods sectorMethods = new SectorMethods();
        System.out.println(sectorMethods.selectSector(1));
        SiiMethods siiMethods = new SiiMethods();
        System.out.println(siiMethods.selectSii(1));
        SocMethods socMethods = new SocMethods();
        System.out.println(socMethods.selectSoc(1));
        SpMethods spMethods = new SpMethods();
        System.out.println(spMethods.selectSp(5));
        StockMethods stockMethods = new StockMethods();
        System.out.println(stockMethods.selectStock(1));
        System.out.println("*************************************************************************************************");
    }
    /*
        CompanyMethods companyMethods = new CompanyMethods();
        Company company = Company.builder().cmpnName("Тестовая компания").build();
        companyMethods.insertCompany(company);
        System.out.println(companyMethods.showAllCompanies());
        companyMethods.updateCompany(1, Company.builder().cmpnName("Тестовая компания Новая").build());
        System.out.println(companyMethods.selectCompany(1));
        companyMethods.deleteCompany(1);
        System.out.println(companyMethods.showAllCompanies());
        System.out.println("*************************************************************************************************");

        CurrencyMethods currencyMethods = new CurrencyMethods();
        Currency currency = Currency.builder().curName("Тестовая валюта").build();
        currencyMethods.insertCurrency(currency);
        System.out.println(currencyMethods.showAllCurrencies());
        currencyMethods.updateCurrency(1, Currency.builder().curName("Тестовая валюта Новая").build());
        System.out.println(currencyMethods.selectCurrency(1));
        currencyMethods.deleteCurrency(1);
        System.out.println(currencyMethods.showAllCurrencies());
        System.out.println("*************************************************************************************************");

        IndexMethods indexMethods = new IndexMethods();
        Index index = Index.builder().indxName("Тестовый индекс").build();
        indexMethods.insertIndex(index);
        System.out.println(indexMethods.showAllIndexes());
        indexMethods.updateIndex(1, Index.builder().indxName("Тестовый индекс Новая").build());
        System.out.println(indexMethods.selectIndex(1));
        indexMethods.deleteIndex(1);
        System.out.println(indexMethods.showAllIndexes());
        System.out.println("*************************************************************************************************");

        SectorMethods sectorMethods = new SectorMethods();
        Sector sector = Sector.builder().sctrName("Тестовый сектор").build();
        sectorMethods.insertSector(sector);
        System.out.println(sectorMethods.showAllSectors());
        sectorMethods.updateSector(1, Sector.builder().sctrName("Тестовый сектор Новый").build());
        System.out.println(sectorMethods.selectSector(1));
        sectorMethods.deleteSector(1);
        System.out.println(sectorMethods.showAllSectors());
        System.out.println("*************************************************************************************************");

        // Удаление и пересоздание схемы БД
        CompanyMethods companyMethods = new CompanyMethods();
        Company company = Company.builder().cmpnName("Тестовая компания").build();
        companyMethods.insertCompany(company);
        CurrencyMethods currencyMethods = new CurrencyMethods();
        Currency currency = Currency.builder().curName("Тестовая валюта").build();
        currencyMethods.insertCurrency(currency);
        IndexMethods indexMethods = new IndexMethods();
        Index index = Index.builder().indxName("Тестовый индекс").build();
        indexMethods.insertIndex(index);
        SectorMethods sectorMethods = new SectorMethods();
        Sector sector = Sector.builder().sctrName("Тестовый сектор").build();
        sectorMethods.insertSector(sector);
        System.out.println("*************************************************************************************************");

        StockMethods stockMethods = new StockMethods();
        Stock stock = Stock.builder().stckCmpnFK(1).stckName("Тестовая акция").stckTicker("Тестовый тикер").build();
        stockMethods.insertStock(stock);
        System.out.println(stockMethods.showAllStocks());
        company = Company.builder().cmpnName("Тестовая компания 2").build();
        companyMethods.insertCompany(company);
        stockMethods.updateStock(1, Stock.builder().stckCmpnFK(2).stckName("Тестовая акция Новая").stckTicker("Тестовый тикер Новый").build());
        System.out.println(stockMethods.selectStock(1));
        stockMethods.deleteStock(1);
        System.out.println(stockMethods.showAllStocks());
        System.out.println("*************************************************************************************************");

        SocMethods socMethods = new SocMethods();
        SectorsOfCompanies sectorsOfCompanies = SectorsOfCompanies.builder().socCmpnFK(1).socSctrFK(1).build();
        socMethods.insertSoc(sectorsOfCompanies);
        System.out.println(socMethods.showAllSoc());
        SectorMethods sectorMethods = new SectorMethods();
        Sector sector = Sector.builder().sctrName("Тестовый сектор").build();
        sectorMethods.insertSector(sector);
        socMethods.updateSoc(1, SectorsOfCompanies.builder().socCmpnFK(2).socSctrFK(2).build());
        System.out.println(socMethods.selectSoc(1));
        socMethods.deleteSoc(1);
        System.out.println(socMethods.showAllSoc());
        System.out.println("*************************************************************************************************");

        DoicMethods doicMethods = new DoicMethods();
        DateOfIndexesChanges dateOfIndexesChanges = DateOfIndexesChanges.builder().doicIndxFK(1).doicDateChng(new Date(2021, 1, 22)).build();
        doicMethods.insertDoic(dateOfIndexesChanges);
        System.out.println(doicMethods.showAllDoic());
        IndexMethods indexMethods = new IndexMethods();
        Index index = Index.builder().indxName("Тестовый индекс").build();
        indexMethods.insertIndex(index);
        doicMethods.updateDoic(1, DateOfIndexesChanges.builder().doicDateChng(new Date(2020, 12, 31)).doicIndxFK(2).build());
        System.out.println(doicMethods.selectDoic(1));
        doicMethods.deleteDoic(1);
        System.out.println(doicMethods.showAllDoic());
        System.out.println("*************************************************************************************************");

        StockMethods stockMethods = new StockMethods();
        Stock stock = Stock.builder().stckCmpnFK(1).stckName("Тестовая акция").stckTicker("Тестовый тикер").build();
        stockMethods.insertStock(stock);
        SpMethods spMethods = new SpMethods();
        StocksPrices stocksPrices = StocksPrices.builder().spCurFK(1).spStckFK(2).spPrice(123.12).spTimeSet(new Date(1235342)).build();
        spMethods.insertSp(stocksPrices);
        System.out.println(spMethods.showAllSp());
        stock = Stock.builder().stckCmpnFK(1).stckName("Тестовая акция 2").stckTicker("Тестовый тикер 2").build();
        stockMethods.insertStock(stock);
        CurrencyMethods currencyMethods = new CurrencyMethods();
        Currency currency = Currency.builder().curName("Тестовая валюта 2").build();
        currencyMethods.insertCurrency(currency);
        spMethods.updateSp(1, StocksPrices.builder().spStckFK(3).spCurFK(2).spPrice(432.12).spTimeSet(new Date(16745893)).build());
        System.out.println(spMethods.selectSp(1));
        spMethods.deleteSp(1);
        System.out.println(spMethods.showAllSp());
        System.out.println("*************************************************************************************************");

        DoicMethods doicMethods = new DoicMethods();
        DateOfIndexesChanges dateOfIndexesChanges = DateOfIndexesChanges.builder().doicIndxFK(1).doicDateChng(new Date(2021, 1, 22)).build();
        doicMethods.insertDoic(dateOfIndexesChanges);
        dateOfIndexesChanges = DateOfIndexesChanges.builder().doicIndxFK(2).doicDateChng(new Date(2020, 11, 23)).build();
        doicMethods.insertDoic(dateOfIndexesChanges);
        SiiMethods siiMethods = new SiiMethods();
        StocksInIndexes stocksInIndexes = StocksInIndexes.builder().siiDoicFK(2).siiStckFK(2).siiNumStck(122).build();
        siiMethods.insertSii(stocksInIndexes);
        System.out.println(siiMethods.showAllSii());
        siiMethods.updateSii(1, StocksInIndexes.builder().siiDoicFK(3).siiStckFK(4).siiNumStck(1765).build());
        System.out.println(siiMethods.selectSii(1));
        siiMethods.deleteSii(1);
        System.out.println(siiMethods.showAllSii());
        System.out.println("*************************************************************************************************");
    */
}
