package ru.mephi.iw.dao.upsertIndexes;

import ru.mephi.iw.dao.mappers.stocks.DoicMapper;
import ru.mephi.iw.dao.mappers.stocks.SiiMapper;
import ru.mephi.iw.dao.mappers.stocks.SpMapper;
import ru.mephi.iw.dao.mappers.stocks.StockMapper;
import ru.mephi.iw.exceptions.IwRuntimeException;
import ru.mephi.iw.models.associations.PriceStockInIndex;
import ru.mephi.iw.models.stocks.DateOfIndexesChanges;
import ru.mephi.iw.models.stocks.Stock;
import java.sql.Date;
import java.util.List;


public class WriteIMOEXInDB {

    private WriteIMOEXInDB() {
    }

    public static void insertIndexData(List<PriceStockInIndex> priceStockInIndices,
                                       List<PriceStockInIndex> priceStockInIndicesOld, DoicMapper doicMapper,
                                       SiiMapper siiMapper, StockMapper stockMapper, SpMapper spMapper,
                                       boolean needCheckPrice) {

        if (priceStockInIndicesOld.size() != 0) {

            boolean check = true;
            boolean checkDop = true;
            for (int i = 0; i < priceStockInIndices.size(); i++) {
                for (int j = 0; j < priceStockInIndices.size(); j++) {
                    if (priceStockInIndices.get(i).getStock().getTicker().equals(
                            priceStockInIndicesOld.get(j).getStock().getTicker())) {
                        priceStockInIndices.get(i).getStock().setId(priceStockInIndicesOld.get(i).getStock().getId());
                        priceStockInIndices.get(i).setId(priceStockInIndicesOld.get(j).getId());
                        check = true;
                        priceStockInIndices.get(i).setId(priceStockInIndicesOld.get(j).getId());
                        break;
                    } else {
                        check = false;
                    }
                }

                if (!check) {
                    checkDop = false;
                    break;
                }
            }

            PriceStockInIndex priceStockInIndexOld;
            if (checkDop && (priceStockInIndices.size() == priceStockInIndicesOld.size())) {
                for (int i = 0; i < priceStockInIndices.size(); i++) {

                    priceStockInIndexOld = findPriceStockInIndexOld(priceStockInIndices.get(i).getId(),
                            priceStockInIndicesOld);

                    if ((priceStockInIndexOld.getStockInIndex().getNumberOfStocksInIndex() != priceStockInIndices.get(i).
                            getStockInIndex().getNumberOfStocksInIndex()) && needCheckPrice) {
                        throw new IwRuntimeException("Старое и новое количество бумаг с тикером " +
                                priceStockInIndices.get(i).getStock().getTicker() +" в индексе не равны. " +
                                "Необходимо проверить правильность считанных данных.");
                    }

                    if ((priceStockInIndices.get(i).getStockPrice().getPrice() / priceStockInIndexOld.getStockPrice().
                            getPrice()) > 5 && needCheckPrice) {
                        throw new IwRuntimeException("Разница между старой и новой ценой слишком большая. " +
                                "Необходимо проверить правильность считанных данных.");
                    }
                }
                for (int i = 0; i < priceStockInIndices.size(); i++) {
                    priceStockInIndices.get(i).getStockPrice().setStockId(priceStockInIndices.get(i).getStock().getId());
                    spMapper.insertSp(priceStockInIndices.get(i).getStockPrice());
                }
            } else {
                ifIMOEXWasChanged(priceStockInIndices, priceStockInIndicesOld, doicMapper, siiMapper,  stockMapper, spMapper);
            }
        } else {
            for (int i = 0; i < priceStockInIndices.size(); i++) {
                DateOfIndexesChanges doicNew = new DateOfIndexesChanges(0, 1, new Date((new java.util.Date()).getTime()));
                doicMapper.insertDoic(doicNew);
                stockMapper.insertStock(priceStockInIndices.get(i).getStock());
                priceStockInIndices.get(i).getStockPrice().setStockId(priceStockInIndices.get(i).getStock().getId());
                spMapper.insertSp(priceStockInIndices.get(i).getStockPrice());
                priceStockInIndices.get(i).getStockInIndex().setStockId(priceStockInIndices.get(i).getStock().getId());
                priceStockInIndices.get(i).getStockInIndex().setDateOfIndexesChangesId(doicNew.getIndexId());
                siiMapper.insertSii(priceStockInIndices.get(i).getStockInIndex());
            }
        }
    }

    private static void ifIMOEXWasChanged (List<PriceStockInIndex> priceStockInIndices,
                                           List<PriceStockInIndex> priceStockInIndicesOld, DoicMapper doicMapper,
                                           SiiMapper siiMapper, StockMapper stockMapper, SpMapper spMapper) {
        DateOfIndexesChanges doic = DateOfIndexesChanges.builder().indexId(1).
                dateOfChange(new Date((new java.util.Date()).getTime())).build();
        doicMapper.insertDoic(doic);

        Stock stock;
        for (int i = 0; i < priceStockInIndices.size(); i++) {
            stock = stockMapper.selectStockByTicker(priceStockInIndices.get(i).getStock().getTicker());
            if (stock == null) {
                stockMapper.insertStock(priceStockInIndices.get(i).getStock());
            } else {
                priceStockInIndices.get(i).setStock(stock);
            }

            priceStockInIndices.get(i).getStockInIndex().setStockId(priceStockInIndices.get(i).getStock().getId());
            priceStockInIndices.get(i).getStockInIndex().setDateOfIndexesChangesId(doic.getId());
            siiMapper.insertSii(priceStockInIndices.get(i).getStockInIndex());

            priceStockInIndices.get(i).getStockPrice().setStockId(priceStockInIndices.get(i).getStock().getId());
            spMapper.insertSp(priceStockInIndices.get(i).getStockPrice());
        }
    }

    private static PriceStockInIndex findPriceStockInIndexOld(int id, List<PriceStockInIndex> PriceStockInIndexOld) {
        for (PriceStockInIndex priceStockInIndex : PriceStockInIndexOld) {
            if (priceStockInIndex.getId() == id) {
                return priceStockInIndex;
            }
        }
        return null;
    }
}