package ru.mephi.iw.bean;

import ru.mephi.iw.dao.mappers.SiiMapper;
import ru.mephi.iw.dao.mappers.SpMapper;
import ru.mephi.iw.dao.mappers.StockMapper;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.exceptions.CanNotDeleteFile;
import ru.mephi.iw.fill.FillClass;
import ru.mephi.iw.models.Stock;
import ru.mephi.iw.models.StocksInIndexes;
import ru.mephi.iw.models.StocksPrices;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ManagedBean(name="bean")
@ViewScoped
public class Bean implements Serializable {

    private String msg;
    private static StockMapper stockMethods;
    private static SpMapper spMethods;
    private static SiiMapper siiMethods;
    private static List<Stock> stocks;
    private static List<StocksPrices> stocksP;
    private static List<StocksInIndexes> stocksI;
    private static List<Helper> helpers;
    private static Thread thread;

    public List<Helper> getHelpers() {
        return helpers;
    }

    public void setHelpers(List<Helper> helpers) {
        this.helpers = helpers;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @PostConstruct
    private void init() {
        try {
            //FillClass.fill();
            if (helpers == null) {
                stockMethods = Initial.stockMapper;
                spMethods = Initial.spMapper;
                siiMethods = Initial.siiMapper;
                stocksI = siiMethods.selectAllSiiIMOEX(Initial.doicMapper.selectDoicIMOEXLast().getId());
                int[] ids = new int[stocksI.size()];
                for (int i = 0; i < ids.length; i++) {
                    ids[i] = stocksI.get(i).getStockId();
                }
                stocks = stockMethods.selectAllStocksIMOEX(ids);
                stocksP = spMethods.selectAllSpLastIMOEX(ids);

                helpers = new ArrayList<Helper>(ids.length);
                for (int i = 0; i < ids.length; i++) {
                    helpers.add(new Helper());
                    helpers.get(i).stock = stocks.get(i);
                    helpers.get(i).sii = stocksI.get(i);
                    helpers.get(i).sp = stocksP.get(i);
                }
                thread = new Thread(() -> {
                    while (true) {
                        long t1;
                        long t2;
                        t1 = System.currentTimeMillis();
                        try {
                            FillClass.fill();
                        } catch (CanNotDeleteFile | IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        t2 = System.currentTimeMillis();
                        try {
                            TimeUnit.MILLISECONDS.sleep(86400000 - t2 + t1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "Не удалось подключиться к БД:(";
            return;
        }
        msg = "Work...";
    }
}