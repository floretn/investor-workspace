package ru.mephi.iw.download.fillIMOEX;

import ru.mephi.iw.exceptions.IwRuntimeException;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DownloadIMOEXPicture {

    private DownloadIMOEXPicture() {
    }

    static File downloadIndexGif() {
        File fileGif;
        try {
            fileGif = File.createTempFile("Picture_With_IMOEX_Data", ".gif");
            try (InputStream in = (new URL(getURL())).openStream();
                 OutputStream writer = new FileOutputStream(fileGif)) {
                byte[] buffer = new byte[1024];
                int c;
                while ((c = in.read(buffer)) > 0) {
                    writer.write(buffer, 0, c);
                }
                writer.flush();
            }
        } catch (IOException ioe) {
            throw new IwRuntimeException("Внутренняя ошибка", ioe);
        }
        return fileGif;
    }

    private static String getURL(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return "https://informer.moex.com/ru/index/constituents-IMOEX-" +
                (new SimpleDateFormat("yyyyMMdd")).format(calendar.getTime()) +
                ".gif?page=0";
    }
}

