package ru.mephi.iw.download;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mephi.iw.dao.work.SelectIndexesFromDB;
import ru.mephi.iw.exceptions.IwRuntimeException;
import ru.mephi.iw.models.stocks.DateOfIndexesChanges;
import ru.mephi.iw.models.stocks.associations.PriceStockInIndex;
import java.util.Date;
import java.util.List;

@Component
public class IndexChangesCalculator {

    @AllArgsConstructor
    public static class NewIndexStruct {

        private final List<PriceStockInIndex> priceStockInIndicesNew;
        private final DateOfIndexesChanges doicChange;
        private final boolean indexWasChanges;

        public List<PriceStockInIndex> getPriceStockInIndicesNew() {
            return priceStockInIndicesNew;
        }

        public DateOfIndexesChanges getDoicChange() {
            return doicChange;
        }

        public boolean isIndexWasChanges() {
            return indexWasChanges;
        }
    }

    public NewIndexStruct checkIndexData(List<PriceStockInIndex> priceStockInIndices,
                                                SelectIndexesFromDB.IndexStruct indexStruct, boolean trustInfo) {

        List<PriceStockInIndex> priceStockInIndicesOld = indexStruct.getPriceStockInIndicesBefore();
        DateOfIndexesChanges doicChange = null;

        boolean check = checkWasReBalance(priceStockInIndices, priceStockInIndicesOld, trustInfo);
        if (check) {
            priceStockInIndicesOld = indexStruct.getPriceStockInIndicesAfter();
            check = checkWasReBalance(priceStockInIndices, priceStockInIndicesOld, trustInfo);
            if (!check) {
                doicChange = priceStockInIndicesOld.get(0).getDateOfIndexChange();
                doicChange.setDateOfChange(priceStockInIndices.get(0).getStockPrice().getSettingTime().toLocalDateTime().toLocalDate());
                for (PriceStockInIndex priceStockInIndex : priceStockInIndices) {
                    priceStockInIndex.setDateOfIndexChange(doicChange);
                }
            }
        }

        if (priceStockInIndicesOld.size() != 0) {
            if (!check && (priceStockInIndices.size() == priceStockInIndicesOld.size())) {
                if (!trustInfo) {
                    checkPriceAndCap(priceStockInIndices, priceStockInIndicesOld);
                }
                return new NewIndexStruct(priceStockInIndices, doicChange, false);
            }
        }
        return new NewIndexStruct(priceStockInIndices, doicChange, true);
    }

    private void checkPriceAndCap(List<PriceStockInIndex> priceStockInIndices,
                                         List<PriceStockInIndex> priceStockInIndicesOld) {
        for (PriceStockInIndex stockInIndex : priceStockInIndices) {

            PriceStockInIndex priceStockInIndex = findPriceStockInIndexOld(stockInIndex.getId(),
                    priceStockInIndicesOld);

            if ((priceStockInIndex.getStockInIndex().getNumberOfStocksInIndex() != stockInIndex.
                    getStockInIndex().getNumberOfStocksInIndex())) {
                throw new IwRuntimeException("Старое и новое количество бумаг с тикером " +
                        stockInIndex.getStock().getTicker() + " в индексе не равны. " +
                        "Необходимо проверить правильность считанных данных.");
            }

            if ((stockInIndex.getStockPrice().getPrice() / priceStockInIndex.getStockPrice().getPrice()) > 5) {
                throw new IwRuntimeException("Разница между старой и новой ценой слишком большая. " +
                        "Необходимо проверить правильность считанных данных.");
            }
        }
    }

    private boolean checkWasReBalance(List<PriceStockInIndex> priceStockInIndices,
                                                List<PriceStockInIndex> priceStockInIndicesOld, boolean trustInfo) {
        if (priceStockInIndices.size() != priceStockInIndicesOld.size()) {
            if (!trustInfo && priceStockInIndicesOld.size() != 0) {
                throw new IwRuntimeException("Количество акций отличается от имеющихся в базе по индексу. " +
                        "Нужна проверка.");
            }
            return true;
        }
        boolean check = false;
        boolean checkDop = false;
        for (int i = 0; i < priceStockInIndices.size(); i++) {

            for (int j = 0; j < priceStockInIndices.size(); j++) {
                if (priceStockInIndices.get(i).getStock().getTicker().equals(
                        priceStockInIndicesOld.get(j).getStock().getTicker())) {

                    priceStockInIndices.get(i).setId(priceStockInIndicesOld.get(j).getId());
                    priceStockInIndices.get(i).getStock().setId(priceStockInIndicesOld.get(i).getStock().getId());
                    priceStockInIndices.get(i).getStockPrice().setStockId(priceStockInIndicesOld.get(j).getId());

                    check = false;
                    break;
                } else {
                    check = true;
                }
            }

            if (check) {
                if (!trustInfo) {
                    throw new IwRuntimeException("Некоторые тикеры акций отличаются от имеющихся в базе по индексу. " +
                            "Нужна проверка.");
                }
                checkDop = true;
                break;
            }
        }
        return checkDop;
    }

    private PriceStockInIndex findPriceStockInIndexOld(int id, List<PriceStockInIndex> PriceStockInIndexOld) {
        for (PriceStockInIndex priceStockInIndex : PriceStockInIndexOld) {
            if (priceStockInIndex.getId() == id) {
                return priceStockInIndex;
            }
        }
        return null;
    }
}