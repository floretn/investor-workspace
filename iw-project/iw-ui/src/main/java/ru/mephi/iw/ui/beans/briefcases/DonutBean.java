package ru.mephi.iw.ui.beans.briefcases;

import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import ru.mephi.iw.constants.CurrencyKeys;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.briefcases.collections.BIFUMapper;
import ru.mephi.iw.exceptions.IwRuntimeException;
import ru.mephi.iw.models.briefcases.collections.BriefcaseInfoForUser;
import ru.mephi.iw.ui.beans.hat.HatBean;
import ru.mephi.iw.work_classes.Percentage;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

@ManagedBean(name = "donut")
@ViewScoped
@Data
public class DonutBean implements Serializable {

    /**Названия учитываемых средств*/
    private static final String CURRENCY_NAME = "Валюта";
    private static final String STOCK_NAME = "Акции";
    private static final String GDR_NAME = "GDR";
    private static final String ADR_NAME = "ADR";

    /**Индексы учитываемых средств в DataSet of donutModel*/
    private static final int CURRENCY_ID = 0;
    private static final int STOCK_ID = 1;
    private static final int GDR_ID = 2;
    private static final int ADR_ID = 3;

    private static final String[] COLORS = new String[] {"rgb(255, 99, 132)", "rgb(75, 192, 192)", "rgb(255, 205, 86)"
            , "rgb(54, 235, 117)", "rgb(16, 199, 199)", "rgb(201, 203, 207)", "rgb(69, 61, 61)"};

    private static final Map<Integer, String> MAP_OF_SECURITY_TYPE = new HashMap<>();
    static {
        MAP_OF_SECURITY_TYPE.put(CURRENCY_ID, CURRENCY_NAME);
        MAP_OF_SECURITY_TYPE.put(STOCK_ID, STOCK_NAME);
        MAP_OF_SECURITY_TYPE.put(ADR_ID, ADR_NAME);
        MAP_OF_SECURITY_TYPE.put(GDR_ID, GDR_NAME);
    }

    private DonutChartModel donutModel;

    private DonutChartModel donutModelCommon;
    private DonutChartModel donutModelCur;
    private DonutChartModel donutModelSecurity;

    private List<BriefcaseInfoForUser> briefcases = null;

    @ManagedProperty(value = "#{hat}")
    private HatBean hat;

    @PostConstruct
    public void init() {

        String itemIdS = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .getParameter("id");
        int itemId = -1;

        if (itemIdS != null) {
            try {
                itemId = Integer.parseInt(itemIdS);
            } catch (Exception ex) {
                ex.printStackTrace();
                err();
                return;
            }
        }

        if (briefcases == null) {
            try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
                briefcases = sqlSession.getMapper(BIFUMapper.class).selectAllLastBIFU(hat.getCurrentUserInfo().getId());
            }
        }

        if (itemId == -1) {
            createDonutModelCommon();
            donutModel = donutModelCommon;
            return;
        }

        if (itemId == CURRENCY_ID) {
            createDonutModelCur();
            donutModel = donutModelCur;
            return;
        }

        createDonutModelSecurity(itemId);
        donutModel = donutModelSecurity;
    }

    private void err() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                    .getExternalContext().getRequest();

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
                    .getExternalContext().getResponse();
            response.sendRedirect(request.getContextPath() + "/ru/mephi/iw/briefcases/Donut.xhtml");
        } catch (Exception e) {
            throw new IwRuntimeException("", e);
        }
    }

    public void createDonutModelCommon() {
        donutModelCommon = new DonutChartModel();
        ChartData data = new ChartData();

        double accountCurrency = briefcases.parallelStream().flatMap(b -> b.getAccountAndCurrencies().parallelStream())
                .flatMapToDouble(a -> DoubleStream.of(a.getAccount().getAmount()*a.getCurrency().getCourse())).sum();

        double accountStock = completeStocksAccount(STOCK_NAME);
        double accountGDR = completeStocksAccount(GDR_NAME);
        double accountADR = completeStocksAccount(ADR_NAME);

        double sum = accountCurrency + accountStock + accountGDR + accountADR;
        double sumPercents = 0;

        Percentage percentStock = new Percentage(findPercent(sum, accountStock));
        sumPercents += percentStock.doubleValue();

        Percentage percentGDR = new Percentage(findPercent(sum, accountGDR));
        sumPercents += percentGDR.doubleValue();

        Percentage percentADR = new Percentage(findPercent(sum, accountADR));
        sumPercents += percentADR.doubleValue();

        Percentage percentCurrency = new Percentage(100 - sumPercents);

        DonutChartDataSet dataSet = new DonutChartDataSet();

        List<Number> values = new ArrayList<>();
        values.add(CURRENCY_ID, percentCurrency);
        values.add(STOCK_ID, percentStock);
        values.add(GDR_ID, percentGDR);
        values.add(ADR_ID, percentADR);
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add(CURRENCY_ID, COLORS[0]);
        bgColors.add(STOCK_ID, COLORS[1]);
        bgColors.add(GDR_ID, COLORS[2]);
        bgColors.add(ADR_ID, COLORS[3]);
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add(CURRENCY_ID, CURRENCY_NAME + "(%)");
        labels.add(STOCK_ID, STOCK_NAME + "(%)");
        labels.add(GDR_ID, GDR_NAME + "(%)");
        labels.add(ADR_ID, ADR_NAME + "(%)");
        data.setLabels(labels);

        donutModelCommon.setData(data);
    }

    public void createDonutModelCur() {
        donutModelCur = new DonutChartModel();
        ChartData data = new ChartData();

        double accountRUB = briefcases.parallelStream().flatMap(b -> b.getAccountAndCurrencies().parallelStream())
                .filter(a -> a.getCurrency().getId() == CurrencyKeys.RUBLES_KEY)
                .flatMapToDouble(a -> DoubleStream.of(a.getAccount().getAmount())).sum();

        double accountUSD = briefcases.parallelStream().flatMap(b -> b.getAccountAndCurrencies().parallelStream())
                .filter(a -> a.getCurrency().getId() == CurrencyKeys.USD_KEY)
                .flatMapToDouble(a -> DoubleStream.of(a.getAccount().getAmount() * 75)).sum();

        double sum = accountRUB + accountUSD;
        double sumPercents = 0;

        Percentage percentRUB = new Percentage(findPercent(sum, accountRUB));
        sumPercents += percentRUB.doubleValue();

        Percentage percentUSD = new Percentage(100 - sumPercents);

        DonutChartDataSet dataSet = new DonutChartDataSet();

        List<Number> values = new ArrayList<>();
        values.add(percentRUB);
        values.add(percentUSD);
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add( "rgb(255, 99, 132)");
        bgColors.add("rgb(54, 162, 235)");
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add("RUB(%)");
        labels.add("USD(%)");
        data.setLabels(labels);

        donutModelCur.setData(data);
    }

    public void createDonutModelSecurity(int index) {
        donutModelSecurity = new DonutChartModel();
        ChartData data = new ChartData();

        Map<String, Double> map = new HashMap<>();

        double[] sum = new double[1];
        briefcases.parallelStream().flatMap(b -> b.getStockAndPriceAndSIBs().parallelStream())
                .filter(sps -> sps.getStockInBriefcase().getType().equals(MAP_OF_SECURITY_TYPE.get(index)))
                .forEach(sps -> {
                    if (map.containsKey(sps.getStock().getTicker())) {
                        double account = sps.getStockPrice().getPrice()
                                * sps.getStockInBriefcase().getStocksNumber() * sps.getCurrencyOfPrice().getCourse();
                        sum[0] += account;
                        map.replace(sps.getStock().getTicker(), map.get(sps.getStock().getTicker()) + account);
                    } else {
                        double account = sps.getStockPrice().getPrice() * sps.getStockInBriefcase().getStocksNumber()
                                * sps.getCurrencyOfPrice().getCourse();
                        sum[0] += account;
                        map.put(sps.getStock().getTicker(), account);
                    }
        });

        List<Number> values = new ArrayList<>();
        List<String> bgColors = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        List<String> keys = new ArrayList<>(map.keySet());
        double sumPercents = 0;
        int j = 0;
        for (int i = 0; i < keys.size() - 1; i++) {
            Percentage account = new Percentage(findPercent(sum[0], map.get(keys.get(i))));
            sumPercents += account.doubleValue();
            values.add(account);
            labels.add(keys.get(i) + "(%)");
            j = i % COLORS.length;
            bgColors.add(COLORS[j]);
        }

        values.add(new Percentage(100 - sumPercents));
        labels.add(keys.get(keys.size() - 1) + "(%)");
        if (j == (COLORS.length - 1)) {
            bgColors.add(COLORS[0]);
        } else {
            bgColors.add(COLORS[j + 1]);
        }

        DonutChartDataSet dataSet = new DonutChartDataSet();

        dataSet.setData(values);
        dataSet.setBackgroundColor(bgColors);
        data.addChartDataSet(dataSet);

        data.setLabels(labels);

        donutModelSecurity.setData(data);
    }

    private double completeStocksAccount(String stockName) {
        return briefcases.parallelStream().flatMap(b -> b.getStockAndPriceAndSIBs().parallelStream())
                .filter(sps -> sps.getStockInBriefcase().getType() != null
                        && sps.getStockInBriefcase().getType().equals(stockName))
                .flatMapToDouble(sps ->
                {if (sps.getCurrencyOfPrice().getId() == CurrencyKeys.USD_KEY)
                    return DoubleStream.of(sps.getStockPrice().getPrice() * 75);
                    return DoubleStream.of(sps.getStockPrice().getPrice());}).sum();
    }

    private double findPercent(double sum, double part) {
        return (part / sum) * 100;
    }

    public void itemSelect(ItemSelectEvent event) {
        if (donutModel == donutModelCommon) {
            try {
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                        .getExternalContext().getRequest();

                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(request.getContextPath() + "/ru/mephi/iw/briefcases/Donut.xhtml?id=" + event.getItemIndex());
            } catch (Exception e) {
                throw new IwRuntimeException("", e);
            }
        }
    }
}