package fillIMOEX;

import org.apache.ibatis.io.Resources;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

class DownloadXlsFile {

    DownloadXlsFile() {
    }

    static void giveGif() throws IOException, InterruptedException {
        System.setProperty("webdriver.chrome.driver", Resources.getResourceAsFile("chromedriver.exe").getCanonicalPath());
        ChromeDriver driverG = new ChromeDriver();
        File file = Resources.getResourceAsFile("file.gif");
        BufferedImage image = ImageIO.read(file);
        File f = new File("file.jpg");
        ImageIO.write(image, "jpg", f);
        try {
            driverG.get("https://www.onlineocr.net/");
            WebElement fileupload = driverG.findElementById("fileupload");
            fileupload.sendKeys(f.getAbsolutePath());
            Select select = new Select(driverG.findElement(By.name("ctl00$MainContent$comboOutput")));
            select.selectByValue("Microsoft Excel (xlsx)");
            WebElement el = driverG.findElementById("MainContent_btnOCRConvert");
            TimeUnit.SECONDS.sleep(1);
            el.click();
            TimeUnit.SECONDS.sleep(5);
            el = driverG.findElementById("MainContent_lnkBtnDownloadOutput");
            el.click();
            TimeUnit.SECONDS.sleep(2);
        }finally {
            driverG.close();
        }
    }
}
