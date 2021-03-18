package ru.mephi.iw.ui.beans;

import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.association.PriceStockInIndexMapper;
import ru.mephi.iw.download.fillIMOEX.FillIMOEXInDB;
import ru.mephi.iw.exceptions.IwRuntimeException;
import ru.mephi.iw.models.associations.PriceStockInIndex;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import java.io.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

@ManagedBean(name="updateIMOEXBean")
@ViewScoped
@Data
@Named
@RequestScoped
public class UpdateIMOEXBean implements Serializable {

    private boolean trustThePrice;
    private List<PriceStockInIndex> priceStockInIndices;
    private Date dateForIndexStruct;
    private UploadedFile file;

    @PostConstruct
    private void init() {
        trustThePrice = false;
        try (SqlSession sqlSession = Initial.getSqlSessionFactory().openSession()) {
            priceStockInIndices = sqlSession.getMapper(PriceStockInIndexMapper.class).
                    selectLastStocksPricesForIndex(1);
        }
        dateForIndexStruct = new Date(priceStockInIndices.get(0).getStockPrice().getSettingTime().getTime());
    }

    public void downloadIMOEX() {
        PrimeFaces primeFaces = PrimeFaces.current();
        PrimeFaces.Dialog dialog = primeFaces.dialog();
        boolean check;
        try {
            check = FillIMOEXInDB.fill(trustThePrice, null);
        } catch (IwRuntimeException e) {
            dialog.showMessageDynamic(new FacesMessage("Ошибка!", e.getMessage()));
            e.printStackTrace();
            return;
        }

        if (check) {
            dialog.showMessageDynamic(new FacesMessage("Успешная загрузка!",
                    "Индекс Московской Биржи успешно загружен в систему!"));
        } else {
            dialog.showMessageDynamic(new FacesMessage("Загрузка была выполнена!",
                    "Сегодня Индекс уже был загружен в систему!"));
        }
    }

    public void showIndexStruct() {
        System.out.println(dateForIndexStruct);
        System.out.println(new Date(dateForIndexStruct.getTime() + 86400000));
        try (SqlSession sqlSession = Initial.getSqlSessionFactory().openSession()) {
            priceStockInIndices = sqlSession.getMapper(PriceStockInIndexMapper.class).selectStocksPricesForIndexForDate(1,
                    new Timestamp(dateForIndexStruct.getTime()), new Timestamp(dateForIndexStruct.getTime() + 86400000));
        }
    }

    public void uploadFile() {
        boolean check;
        PrimeFaces primeFaces = PrimeFaces.current();
        PrimeFaces.Dialog dialog = primeFaces.dialog();
        try {
            File fileUpload = File.createTempFile("uploaded_file", ".xlsx");
            try (OutputStream writer = new FileOutputStream(fileUpload)) {
                writer.write(file.getContent());
            }
            check = FillIMOEXInDB.fill(false, fileUpload);
        } catch (Exception e) {
            dialog.showMessageDynamic(new FacesMessage("Ошибка!", e.getMessage()));
            e.printStackTrace();
            return;
        }

        if (check) {
            dialog.showMessageDynamic(new FacesMessage("Успешная загрузка!",
                    "Индекс Московской Биржи успешно загружен в систему!"));
        } else {
            dialog.showMessageDynamic(new FacesMessage("Загрузка была выполнена!",
                    "Сегодня Индекс уже был загружен в систему!"));
        }
    }
}