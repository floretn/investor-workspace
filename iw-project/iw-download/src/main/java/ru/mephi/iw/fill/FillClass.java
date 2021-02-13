package ru.mephi.iw.fill;

import ru.mephi.iw.exceptions.CanNotDeleteFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class FillClass {

    FillClass() {
    }

    public static void fill() throws IOException, InterruptedException, CanNotDeleteFile {
        DownloadIMOEXPicture.downloadFiles();
        DownloadXlsFile.giveGif();
        File file = new File(System.getProperty("user.home") + "\\Downloads\\file.xlsx");
        boolean check = false;
        while (!check) {
            try {
                ReadIMOEXClass.readXls(new FileInputStream(file));
                check = true;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Файл был считан неправильно. Внесите изменения в файл " +
                        System.getProperty("user.home") + "\\Downloads\\file.xlsx, а затем напечатайте любой символ и нажмите Enter:");
                (new Scanner(System.in)).next();
            }
        }
        WriteIMOEXClass.writeInDB(ReadIMOEXClass.stocks, ReadIMOEXClass.stocksPrices, ReadIMOEXClass.stocksInIndexes);
        if (!file.delete()){
            throw new CanNotDeleteFile("Не могу удалить скачанный файл C:\\Users\\floretn\\Downloads\\file.xlsx");
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException, CanNotDeleteFile {
        fill();
    }
}
