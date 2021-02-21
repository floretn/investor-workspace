package ru.mephi.iw.fill;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.ibatis.io.Resources;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
        ChromeDriver driverG;
        WebDriverManager.chromedriver().browserVersion("77.0.3865.40").setup();
        ChromeOptions options = new ChromeOptions();
        driverG = new ChromeDriver(options);
        BufferedImage image = ImageIO.read(DownloadIMOEXPicture.fileGif);
        File f = new File("file.jpg");
        ImageIO.write(image, "jpg", f);
        try {
            driverG.get("https://www.onlineocr.net/");
            WebElement fileupload = driverG.findElementById("fileupload");
            fileupload.sendKeys(f.getAbsolutePath());
            Select select = new Select(driverG.findElement(By.name("ctl00$MainContent$comboOutput")));
            select.selectByValue("Microsoft Excel (xlsx)");
            TimeUnit.SECONDS.sleep(30);
            WebElement el = driverG.findElementById("MainContent_btnOCRConvert");
            TimeUnit.SECONDS.sleep(1);
            el.click();
            TimeUnit.SECONDS.sleep(30);
            el = driverG.findElementById("MainContent_lnkBtnDownloadOutput");
            TimeUnit.SECONDS.sleep(1);
            el.click();
            TimeUnit.SECONDS.sleep(2);
        }finally {
            driverG.close();
        }
    }
}
