package ru.mephi.iw.download.briefcases;

import org.apache.ibatis.session.SqlSession;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.CurrencyMapper;
import ru.mephi.iw.dao.mappers.briefcases.*;
import ru.mephi.iw.dao.mappers.stocks.SpMapper;
import ru.mephi.iw.dao.mappers.stocks.StockMapper;
import ru.mephi.iw.exceptions.IwRuntimeException;
import ru.mephi.iw.models.Currency;
import ru.mephi.iw.models.briefcases.*;
import ru.mephi.iw.models.stocks.Stock;
import ru.mephi.iw.models.stocks.StocksPrices;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class UpsertBriefcase {

    /**Название типа ценных бумаг "Акции" в xml файле*/
    private static final String STOCK_NAME_IN_XML = "Акции";
    /**Название типа ценных бумаг "ADR" в xml файле*/
    private static final String ADR_NAME_IN_XML = "ADR";
    /**Название типа ценных бумаг "GDR" в xml файле*/
    private static final String GDR_NAME_IN_XML = "GDR";

    /**Для поиска остатка средств у инвестора и игнорирования оценки активов и пр.*/
    private static final String REMAINING = "Исходящий остаток (факт)";

    public void addBriefcase(String fileName, File fileXML, Briefcases briefcase) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(fileXML);

        NodeList nodeList = document.getDocumentElement().getElementsByTagName("item");
        IwRuntimeException exception = null;
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            try {
                sqlSession.getMapper(BriefcasesMapper.class).insertBriefcase(briefcase);

                LocalDate dateNow = LocalDate.now();

                workWithDB(fileName, fileXML, briefcase, dateNow, nodeList, sqlSession);
            } catch (Exception e) {
                try {
                    sqlSession.rollback();
                } catch (Exception e1) {
                    e.addSuppressed(e1);
                }
                exception = new IwRuntimeException("Внутренняя ошибка", e);
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    public void uploadBriefcase(String fileName, File fileXML, Briefcases briefcase, LocalDate localDate) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(fileXML);
        NodeList nodeList = document.getDocumentElement().getElementsByTagName("item");
        IwRuntimeException exception = null;
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            if (sqlSession.getMapper(BSMapper.class).selectBSOfBriefcaseByDate(briefcase.getId()
                    , localDate) != null) {
                exception = new IwRuntimeException("На указанную дату уже была загружена информация о портфеле!");
                throw exception;
            }
            try {
                workWithDB(fileName, fileXML, briefcase, localDate, nodeList, sqlSession);
            } catch (Exception e) {
                try {
                    sqlSession.rollback();
                } catch (Exception e1) {
                    e.addSuppressed(e1);
                }
                exception = new IwRuntimeException("Внутренняя ошибка", e);
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    private void workWithDB(String fileName, File fileXML, Briefcases briefcase, LocalDate localDate
            , NodeList nodeList, SqlSession sqlSession) throws IOException {

        Files file = new Files(0, fileName, localDate, java.nio.file.Files
                .readAllBytes(Paths.get(fileXML.getAbsolutePath())));
        sqlSession.getMapper(FilesMapper.class).insertFile(file);

        BriefcaseStates briefcaseState = new BriefcaseStates(0, briefcase.getId(), file.getId(), localDate);
        sqlSession.getMapper(BSMapper.class).insertBS(briefcaseState);

        String assetType;
        String rowName;

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            NamedNodeMap namedNodeMap = node.getAttributes();

            if (namedNodeMap.getNamedItem("asset_type") != null) {
                assetType = namedNodeMap.getNamedItem("asset_type").getNodeValue();
                switch (assetType) {
                    case STOCK_NAME_IN_XML:
                        insertSIB(namedNodeMap, sqlSession, localDate, briefcaseState, STOCK_NAME_IN_XML);
                        break;
                    case ADR_NAME_IN_XML:
                        insertSIB(namedNodeMap, sqlSession, localDate, briefcaseState, ADR_NAME_IN_XML);
                        break;
                    case GDR_NAME_IN_XML:
                        insertSIB(namedNodeMap, sqlSession, localDate, briefcaseState, GDR_NAME_IN_XML);
                        break;

                    default:

                }
            }

            if (namedNodeMap.getNamedItem("row_name") != null) {
                rowName = namedNodeMap.getNamedItem("row_name").getNodeValue();
                Currency currency = findCurrency(namedNodeMap.getNamedItem("currency").getNodeValue(), sqlSession);

                if (rowName.contains(REMAINING)) {
                    sqlSession.getMapper(AccountMapper.class).insertAccount
                            (new Account(0, briefcaseState.getId(), currency.getId()
                                    , Double.parseDouble(namedNodeMap.getNamedItem("value").getNodeValue())));
                }
            }
        }
        sqlSession.commit();
    }

    private void insertSP(int stockId, LocalDate localDate, NamedNodeMap namedNodeMap, SqlSession sqlSession) {

        Currency currency = findCurrency(namedNodeMap.getNamedItem("price_currency").getNodeValue(), sqlSession);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        calendar.add(Calendar.SECOND, 67800);
        StocksPrices stockPrice = new StocksPrices(0, stockId, currency.getId(), new Timestamp(calendar.getTimeInMillis())
                , Double.parseDouble(namedNodeMap.getNamedItem("settlement_price").getNodeValue()));
        sqlSession.getMapper(SpMapper.class).insertSp(stockPrice);
    }

    private Currency findCurrency(String currencyName, SqlSession sqlSession) {

        Currency currency = sqlSession.getMapper(CurrencyMapper.class)
                .selectCurrencyByName(currencyName);

        if (currency == null) {
            currency = new Currency(0, currencyName);
            sqlSession.getMapper(CurrencyMapper.class).insertCurrency(currency);
        }

        return currency;
    }

    private void insertSIB(NamedNodeMap namedNodeMap, SqlSession sqlSession, LocalDate localDate
            , BriefcaseStates briefcaseState, String type) {
        String stockTicker = namedNodeMap.getNamedItem("asset_name").getNodeValue();

        StockMapper stockMapper = sqlSession.getMapper(StockMapper.class);
        Stock stock = stockMapper.selectStockByTicker(stockTicker);

        if (stock == null) {
            stock = new Stock(0, null, null, stockTicker);
            stockMapper.insertStock(stock);
            insertSP(stock.getId(), localDate, namedNodeMap, sqlSession);
        } else {
            StocksPrices stockPrice = sqlSession.getMapper(SpMapper.class)
                    .selectPriceForStockByDate(stock.getId(), localDate);
            if (stockPrice == null){
                insertSP(stock.getId(), localDate, namedNodeMap, sqlSession);
            }
        }

        StocksInBriefcases stockInBriefcase = new StocksInBriefcases(0, briefcaseState.getId(), stock.getId(),
                (long) Double.parseDouble(namedNodeMap.getNamedItem("closing_position_fact")
                        .getNodeValue()), type);
        sqlSession.getMapper(SIBMapper.class).insertSIB(stockInBriefcase);
    }

}
