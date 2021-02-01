package bean;

import dao.iw.methods.SiiMethods;
import dao.iw.methods.SpMethods;
import dao.iw.methods.StockMethods;
import exceptins.CanNotDeleteFile;
import fillIMOEX.FillClass;
import models.Stock;
import models.StocksInIndexes;
import models.StocksPrices;
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
    private StockMethods stockMethods;
    private SpMethods spMethods;
    private SiiMethods siiMethods;
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
            stockMethods = new StockMethods();
            spMethods = new SpMethods();
            siiMethods = new SiiMethods();
            stocks = stockMethods.showAllStocks();
            stocksP = spMethods.showAllSp();
            stocksI = siiMethods.showAllSii();
            helpers = new ArrayList<>(45);
            for (int i = 0; i < 45; i++){
                helpers.add(new Helper());
                helpers.get(i).stock = stocks.get(i);
                helpers.get(i).sii = stocksI.get(i);
                helpers.get(i).sp = stocksP.get(i);
            }
            thread = new Thread(() -> {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(86400);
                        FillClass.fill();
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
