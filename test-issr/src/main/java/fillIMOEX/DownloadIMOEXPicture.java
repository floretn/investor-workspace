package fillIMOEX;

import org.apache.ibatis.io.Resources;
import java.io.*;
import java.net.URL;
import java.util.Calendar;

public class DownloadIMOEXPicture {

    DownloadIMOEXPicture() {
    }

    static void downloadFiles() throws IOException {
        try(InputStream in = (new URL(getURL())).openStream();
            OutputStream writer = new FileOutputStream(Resources.getResourceAsFile("file.gif"))){
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
        String day = (calendar.get(Calendar.DAY_OF_MONTH) - 1) + "";
        if (day.length() == 1){
            day = "0" + day;
        }
        String month  = (calendar.get(Calendar.MONTH) + 1) + "";
        if (month.length() == 1){
            month = "0" + month;
        }
        String year = calendar.get(Calendar.YEAR) + "";
        return strURL1 + year + month + day + strURL2;
    }
}

