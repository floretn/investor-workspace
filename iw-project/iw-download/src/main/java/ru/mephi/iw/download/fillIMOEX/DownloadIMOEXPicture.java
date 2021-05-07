package ru.mephi.iw.download.fillIMOEX;

import org.springframework.stereotype.Component;
import ru.mephi.iw.exceptions.IwRuntimeException;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
class DownloadIMOEXPicture {

    public File downloadIndexGif(Date date) {
        File fileGif;
        try {
            fileGif = File.createTempFile("Picture_With_IMOEX_Data", ".gif");
            try (InputStream in = (new URL(getURL(date))).openStream();
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

    private String getURL(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return "https://informer.moex.com/ru/index/constituents-IMOEX-" +
                (new SimpleDateFormat("yyyyMMdd")).format(calendar.getTime()) +
                ".gif?page=0";
    }
}

