package ru.mephi.iw.fillIMOEX;

import java.io.*;
import java.net.URL;
import java.util.Calendar;

public class DownloadIMOEXPicture {

    static File fileGif;

    DownloadIMOEXPicture() {
    }

    static void downloadFiles() throws IOException {
        fileGif = new File("file.gif");
        try(InputStream in = (new URL(getURL())).openStream();
            OutputStream writer = new FileOutputStream(fileGif)){
            byte[] buffer = new byte[1024];
            int c;
            while ((c = in.read(buffer)) > 0) {
                writer.write(buffer, 0, c);
            }
            writer.flush();
        }
    }

    private static String getURL(){
        String strURL1 = "https://informer.moex.com/ru/index/constituents-IMOEX-";
        String strURL2 = ".gif?page=0";
        Calendar calendar = Calendar.getInstance();
        String month  = (calendar.get(Calendar.MONTH) + 1) + "";
        if (month.length() == 1){
            month = "0" + month;
        }

        String day = (calendar.get(Calendar.DAY_OF_MONTH) - 1) + "";
        if (day.length() == 1){
            day = "0" + day;
        }

        if (day.equals("00")) {
            int m = calendar.get(Calendar.MONTH);
            if (m == 0 || m == 1 ||m == 3 ||m == 5 ||m == 7 ||m == 8 ||m == 10){
                day = 31 + "";
            }else if (m == 4 || m == 6 || m == 9 || m == 11){
                day = 30 + "";
            }else {
                if (calendar.get(Calendar.YEAR) % 4 == 0) {
                    day = 29 + "";
                }else {
                    day = 28 + "";
                }
            }
        }

        String year = calendar.get(Calendar.YEAR) + "";
        return strURL1 + year + month + day + strURL2;
    }
}

