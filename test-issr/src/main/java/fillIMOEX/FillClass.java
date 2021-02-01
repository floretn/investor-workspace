package fillIMOEX;

import exceptins.CanNotDeleteFile;
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
        ReadIMOEXClass.readXls(new FileInputStream(file));
        if (!file.delete()){
            throw new CanNotDeleteFile("Не могу удалить скачанный файл C:\\Users\\floretn\\Downloads\\file.xlsx. ");
        }
        WriteIMOEXClass.writeInDB(ReadIMOEXClass.stocks, ReadIMOEXClass.stocksPrices, ReadIMOEXClass.stocksInIndexes);
    }
}
