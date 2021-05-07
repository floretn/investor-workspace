package ru.mephi.iw.download.fillIMOEX;

import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.powermock.core.classloader.annotations.PrepareForTest;
import ru.mephi.iw.dao.work.SelectIndexesFromDB;
import ru.mephi.iw.dao.work.UpsertIndexInDB;
import ru.mephi.iw.download.FillIndexInDB;
import ru.mephi.iw.download.IndexChangesCalculator;
import ru.mephi.iw.models.stocks.associations.PriceStockInIndex;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PrepareForTest(FillIMOEXInDB.class)
public class FillIMOEXInDBTest {

    private final ReadIMOEXFromFile readIMOEXFromFile = new ReadIMOEXFromFile();
    private final DownloadIMOEXPicture downloadIMOEXPicture = new DownloadIMOEXPicture();
    private final DownloadXlsxIMOEXFile downloadXlsxIMOEXFile = new DownloadXlsxIMOEXFile();
    private final IndexChangesCalculator indexChangesCalculator = new IndexChangesCalculator();
    private final UpsertIndexInDB upsertIndexInDB = spy(new UpsertIndexInDB());
    private final SelectIndexesFromDB selectIndexesFromDB = spy(new SelectIndexesFromDB());

    private final FillIndexInDB fillIndexInDB = new FillIMOEXInDB(readIMOEXFromFile, downloadIMOEXPicture, downloadXlsxIMOEXFile,
            indexChangesCalculator, upsertIndexInDB, selectIndexesFromDB);

    private static List<PriceStockInIndex> list;

    @Test
    public void test1() throws Exception {
        when(selectIndexesFromDB.selectStructAfterBefore(1, new Date(0))).
                thenReturn(new SelectIndexesFromDB.IndexStruct(new ArrayList<>(), new ArrayList<>(),
                        null));

        doAnswer(invocation -> {
            list = invocation.getArgument(0);
            return 0;
        }).when(upsertIndexInDB).ifIndexWasChanged(anyList(), anyInt());

        Assert.assertTrue(fillIndexInDB.fill(true, new Date(0),
                new File(getClass().getClassLoader().getResource("test1F.xlsx").getFile())));
        System.out.println();
    }

    @Test
    public void test2() throws Exception {
        when(selectIndexesFromDB.selectStructAfterBefore(1, new Date(86400000))).
                thenReturn(new SelectIndexesFromDB.IndexStruct(list, new ArrayList<>(),
                        null));

        doAnswer(invocation -> {
            list = invocation.getArgument(0);
            return 0;
        }).when(upsertIndexInDB).ifIndexWasChanged(anyList(), anyInt());

        Assert.assertTrue(fillIndexInDB.fill(true, new Date(86400000),
                new File(getClass().getClassLoader().getResource("test2F.xlsx").getFile())));
    }

    @Test
    public void test3() throws Exception {
        when(selectIndexesFromDB.selectStructAfterBefore(1, new Date(86400000))).
                thenReturn(new SelectIndexesFromDB.IndexStruct(list, new ArrayList<>(),
                        null));

        doAnswer(invocation -> {
            list = invocation.getArgument(0);
            return 0;
        }).when(upsertIndexInDB).ifIndexWasChanged(anyList(), anyInt());

        Assert.assertTrue(fillIndexInDB.fill(true, new Date(86400000),
                new File(getClass().getClassLoader().getResource("test3F.xlsx").getFile())));
    }
}