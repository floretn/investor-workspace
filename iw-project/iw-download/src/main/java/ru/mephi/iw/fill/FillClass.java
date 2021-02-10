package ru.mephi.iw.fill;

import ru.mephi.iw.exceptions.CanNotDeleteFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FillClass {

    FillClass() {
    }

    public static void fill() throws IOException, InterruptedException, CanNotDeleteFile {
        DownloadIMOEXPicture.downloadFiles();
        DownloadXlsFile.giveGif();
        File file = new File(System.getProperty("user.home") + "\\Downloads\\file.xlsx");
        try {
            ReadIMOEXClass.readXls(new FileInputStream(file));
        } catch (Exception ex){
            if (!file.delete()){
                ex.addSuppressed(new CanNotDeleteFile("Не могу удалить скачанный файл" + System.getProperty("user.home")
                        + "\\Downloads\\file.xlsx"));
            }
            throw ex;
        }
        if (!file.delete()){
            throw new CanNotDeleteFile("Не могу удалить скачанный файл C:\\Users\\floretn\\Downloads\\file.xlsx");
        }
        WriteIMOEXClass.writeInDB(ReadIMOEXClass.stocks, ReadIMOEXClass.stocksPrices, ReadIMOEXClass.stocksInIndexes);
    }

    public static void main(String[] args) throws InterruptedException, IOException, CanNotDeleteFile {
        fill();
    }
}
