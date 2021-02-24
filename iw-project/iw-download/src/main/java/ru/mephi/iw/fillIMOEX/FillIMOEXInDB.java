package ru.mephi.iw.fillIMOEX;

import ru.mephi.iw.exceptions.CanNotDeleteFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FillIMOEXInDB {

    FillIMOEXInDB() {
    }

    public static void fill() throws IOException, InterruptedException, CanNotDeleteFile {
        DownloadIMOEXPicture.downloadFiles();
        DownloadXlsIMOEXFile.giveGif();
        File file = new File(System.getProperty("user.home") + "\\Downloads\\file.xlsx");
        boolean check = false;
        System.out.println("Файл с индексом МосБиржи был скачан. Необходимо привести файл " +
                System.getProperty("user.home") + "\\Downloads\\file.xlsx к стандартному виду...");
        TimeUnit.SECONDS.sleep(60);
        while (!check) {
            try {
                ReadIMOEXFromFile.readXls(new FileInputStream(file));
                check = true;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Файл был считан неправильно. Внесите изменения в файл " +
                        System.getProperty("user.home") + "\\Downloads\\file.xlsx...");
                TimeUnit.SECONDS.sleep(60);
            }
        }
        WriteIMOEXInDB.writeInDB();
        System.out.println("Данные IMOEX успешно загружены в базу");
        if (!file.delete()){
            throw new CanNotDeleteFile("Не могу удалить скачанный файл C:\\Users\\floretn\\Downloads\\file.xlsx");
        }
    }
}
