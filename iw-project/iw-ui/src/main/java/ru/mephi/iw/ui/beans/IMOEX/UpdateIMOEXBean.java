package ru.mephi.iw.ui.beans.IMOEX;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.stocks.association.PriceStockInIndexMapper;
import ru.mephi.iw.download.FillIndexInDB;
import ru.mephi.iw.download.fillIMOEX.FillIMOEXInDB;
import ru.mephi.iw.exceptions.IwRuntimeException;
import ru.mephi.iw.models.stocks.associations.PriceStockInIndex;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@ManagedBean(name="updateIMOEXBean")
@ViewScoped
@Data
public class UpdateIMOEXBean implements Serializable {

    /**
     * Логгер. Предполагается, что конфигурация для логгирования предоставляется сервером приложений.
     * Например на сервере Tomcat должен лежать файл logback.xml с конфигурацией в папке %TOMCAT_HOME%/lib.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateIMOEXBean.class);

    private boolean trustInfo;
    private List<PriceStockInIndex> priceStockInIndices;
    private Date dateForShow;
    private Date dateForUpload;
    private Date dateForUploadFile;
    private UploadedFile downloadFile;
    private File fileWithEr;
    transient private FillIndexInDB fillIndexInDB;

    @PostConstruct
    private void init() {
        trustInfo = false;
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            priceStockInIndices = sqlSession.getMapper(PriceStockInIndexMapper.class).
                    selectLastStocksPricesForIndex(1);
        } catch (Exception e) {
            LOGGER.error("Ошибка инициализации взаимодействия с БД", e);
            throw e;
        }
        dateForShow = new Date();
        dateForUpload = new Date();
        dateForUploadFile = new Date();
        fileWithEr = null;

        try (ClassPathXmlApplicationContext context =
                     new ClassPathXmlApplicationContext("IMOEXConfigDISpring.xml")) {
            fillIndexInDB = context.getBean("fillIMOEXInDB", FillIMOEXInDB.class);
        } catch (Exception e) {
            LOGGER.error("Ошибка инициализации классов", e);
            throw e;
        }

        LOGGER.info("Страница успешно загружена!");
    }

    public void downloadIMOEX() {
        PrimeFaces primeFaces = PrimeFaces.current();
        PrimeFaces.Dialog dialog = primeFaces.dialog();
        boolean check;
        try {
            check = fillIndexInDB.fill(trustInfo, dateForUpload, null);
        } catch (IwRuntimeException e) {
            dialog.showMessageDynamic(new FacesMessage("Ошибка!", e.getMessage()));
            Calendar calendarForDownload = Calendar.getInstance();
            calendarForDownload.setTime(dateForUpload);
            fileWithEr = new File(System.getProperty("user.home") + "\\Downloads\\Picture_With_IMOEX_Data" +
                    (new SimpleDateFormat("yyyyMMdd")).format(calendarForDownload.getTime()) + ".xlsx");
            LOGGER.error(e.getMessage(), e);
            return;
        } catch (Exception exception) {
            dialog.showMessageDynamic(new FacesMessage("Ошибка!", "Внутренняя ошибка!"));
            LOGGER.error(exception.getMessage(), exception);
            return;
        }

        if (check) {
            dialog.showMessageDynamic(new FacesMessage("Успешная загрузка!",
                    "Индекс Московской Биржи успешно загружен в систему!"));
            LOGGER.info("Индекс был успешно загружен в систему!");
        } else {
            dialog.showMessageDynamic(new FacesMessage("Загрузка была выполнена!",
                    "Сегодня Индекс уже был загружен в систему!"));
            LOGGER.info("Инекс за указанную дату уже был загружен в систему...");
        }
    }

    public void showIndexStruct() {
        LOGGER.info("Запрос на показ структуры индекса за " + dateForShow.toString());
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            priceStockInIndices = sqlSession.getMapper(PriceStockInIndexMapper.class).selectStocksPricesForIndexOnPeriod(1,
                    new Timestamp(dateForShow.getTime()), new Timestamp(dateForShow.getTime() + 86400000));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void uploadFile() {
        LOGGER.info("Запрос на загрузку фаила со структурой индекса в систему за " + dateForUploadFile.toString());
        boolean check;
        PrimeFaces primeFaces = PrimeFaces.current();
        PrimeFaces.Dialog dialog = primeFaces.dialog();

        if (!downloadFile.getFileName().endsWith(".xlsx")) {
            dialog.showMessageDynamic(new FacesMessage("Ошибка!", "Формат файла не соответствует xlsx!"));
            LOGGER.error("Пользователь попытался загрузить файл неверного формата: " + downloadFile.getFileName());
            return;
        }

        try {
            File fileUpload = File.createTempFile("uploaded_file", ".xlsx");
            try (OutputStream writer = new FileOutputStream(fileUpload)) {
                writer.write(downloadFile.getContent());
            }
            check = fillIndexInDB.fill(true, dateForUploadFile, fileUpload);
        } catch (IwRuntimeException e) {
            dialog.showMessageDynamic(new FacesMessage("Ошибка!", e.getMessage()));
            LOGGER.error(e.getMessage(), e);
            return;
        }catch (NullPointerException npe) {
            dialog.showMessageDynamic(new FacesMessage("Ошибка!", "Файл не был загружен на страницу!"));
            LOGGER.error(npe.getMessage(), npe);
            return;
        } catch (Exception exception) {
            dialog.showMessageDynamic(new FacesMessage("Ошибка!", "Внутренняя ошибка!"));
            LOGGER.error(exception.getMessage(), exception);
            return;
        }

        if (check) {
            dialog.showMessageDynamic(new FacesMessage("Успешная загрузка!",
                    "Индекс Московской Биржи успешно загружен в систему!"));
            LOGGER.info("Индекс был успешно загружен в систему!");
        } else {
            dialog.showMessageDynamic(new FacesMessage("Загрузка была выполнена!",
                    "Сегодня Индекс уже был загружен в систему!"));
            LOGGER.info("Инекс за указанную дату уже был загружен в систему...");
        }
    }

    public void checkFile() {
        LOGGER.info("Запрос на скачивание файла со структурой индекса за " + dateForUpload.toString());
        if (fileWithEr == null) {
            FacesMessage message = new FacesMessage("Файла не существует!");
            FacesContext.getCurrentInstance().addMessage(null, message);
            FacesContext.getCurrentInstance().validationFailed();
            LOGGER.error("Файла не существует!");
            return;
        }

        if (!fileWithEr.exists()) {
            FacesMessage message = new FacesMessage("Файла не существует!");
            FacesContext.getCurrentInstance().addMessage(null, message);
            FacesContext.getCurrentInstance().validationFailed();
            LOGGER.error("Файла не существует!");
        }
    }

    public DefaultStreamedContent createFile() {
        return DefaultStreamedContent.builder().stream(() -> {
            try {
                return new FileInputStream(fileWithEr);
            } catch (FileNotFoundException e) {
                PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Ошибка!", "Внутренняя ошибка!" +
                        "Файл не найден по непонятным причинам"));
                LOGGER.error(e.getMessage(), e);
            }
            return null;
        }).name(fileWithEr.getName()).contentType(FacesContext.getCurrentInstance().getExternalContext().
                        getMimeType(fileWithEr.getAbsolutePath())).build();
    }
}