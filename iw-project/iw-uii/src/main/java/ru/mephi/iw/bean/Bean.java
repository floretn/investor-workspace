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
    private StockMapper stockMethods;
    private SpMapper spMethods;
    private SiiMapper siiMethods;
    private List<Stock> stocks;
    private List<StocksPrices> stocksP;
    private List<StocksInIndexes> stocksI;
    private List<Helper> helpers;
    private Thread thread;

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
            stockMethods = Initial.stockMapper;
            spMethods = Initial.spMapper;
            siiMethods = Initial.siiMapper;
            stocks = stockMethods.selectAllStocks();
            stocksP = spMethods.selectAllSp();
            stocksI = siiMethods.selectAllSii();
            helpers = new ArrayList<Helper>(45);
            for (int i = 0; i < 45; i++){
                helpers.add(new Helper());
                helpers.get(i).stock = stocks.get(i);
                helpers.get(i).sii = stocksI.get(i);
                helpers.get(i).sp = stocksP.get(i);
            }
            thread = new Thread(() -> {
                while (true) {
                    try {
                        FillClass.fill();
                        TimeUnit.SECONDS.sleep(86400);
                    } catch (InterruptedException | CanNotDeleteFile | IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } catch (Exception e) {
            msg = "Не удалось подключиться к БД:(";
            return;
        }
        msg = "Work...";
    }
}
