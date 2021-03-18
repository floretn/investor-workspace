package ru.mephi.iw.download.fillIMOEX;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import ru.mephi.iw.exceptions.IwRuntimeException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

class DownloadXlsIMOEXFile {

    private DownloadXlsIMOEXFile() {
    }

    static File convertGifToXlsx(File fileGif) {
        WebDriverManager.chromedriver().browserVersion("77.0.3865.40").setup();
        ChromeOptions options = new ChromeOptions();
        ChromeDriver driverG = new ChromeDriver(options);
        File fileJpg;
        try {
            BufferedImage image = ImageIO.read(fileGif);
            fileJpg = File.createTempFile("Picture_With_IMOEX_Data", ".jpg"); //new File("file.jpg");
            ImageIO.write(image, "jpg", fileJpg);

            driverG.get("https://www.onlineocr.net/");
            WebElement fileupload = driverG.findElementById("fileupload");
            fileupload.sendKeys(fileJpg.getAbsolutePath());

            Select select = new Select(driverG.findElement(By.name("ctl00$MainContent$comboOutput")));
            select.selectByValue("Microsoft Excel (xlsx)");
            TimeUnit.SECONDS.sleep(10);

            WebElement el = driverG.findElementById("MainContent_btnOCRConvert");
            TimeUnit.SECONDS.sleep(1);
            el.click();
            TimeUnit.SECONDS.sleep(10);

            el = driverG.findElementById("MainContent_lnkBtnDownloadOutput");
            TimeUnit.SECONDS.sleep(1);
            el.click();
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException | IOException ioe){
            throw new IwRuntimeException("Внетренняя ошибка!", ioe);
        } finally {
            driverG.close();
        }

        File xlsxFile = new File(System.getProperty("user.home") + "\\Downloads\\" +
                fileJpg.getName().replaceAll(".jpg", ".xlsx"));
        if (!(xlsxFile.renameTo(new File(System.getProperty("user.home") +
                "\\Downloads\\Picture_With_IMOEX_Data.xlsx")))) {
            throw new IwRuntimeException("Не могу получить доступ к файлу " + xlsxFile.getAbsolutePath());
        }
        return xlsxFile;
    }
}
