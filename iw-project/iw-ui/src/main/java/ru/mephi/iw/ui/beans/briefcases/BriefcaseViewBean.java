package ru.mephi.iw.ui.beans.briefcases;

import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import org.primefaces.model.file.UploadedFile;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.briefcases.BriefcasesMapper;
import ru.mephi.iw.dao.mappers.briefcases.collections.BIFUMapper;
import ru.mephi.iw.download.briefcases.UpsertBriefcase;
import ru.mephi.iw.exceptions.IwRuntimeException;
import ru.mephi.iw.models.briefcases.collections.BriefcaseInfoForUser;
import ru.mephi.iw.ui.beans.hat.HatBean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@ManagedBean(name = "bv")
@ViewScoped
@Data
public class BriefcaseViewBean implements Serializable {

    private int briefcaseId;
    private BriefcaseInfoForUser currentBriefcase;

    private Date dateForShow;
    private Date dateForUploadFile = new Date();

    private UploadedFile downloadFile;

    @ManagedProperty(value = "#{hat}")
    private HatBean hat;

    @PostConstruct
    private void init() {
        String briefcaseId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .getParameter("id");

        if (briefcaseId != null) {
            try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
                try {
                    this.briefcaseId = Integer.parseInt(briefcaseId);
                    currentBriefcase = sqlSession.getMapper(BIFUMapper.class)
                            .selectLastBIFU(this.briefcaseId);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    err();
                    return;
                }

                if (currentBriefcase == null || currentBriefcase.getBriefcase().getUserId()
                        != hat.getCurrentUserInfo().getId()) {
                    err();
                    return;
                }

                dateForShow = Date.from(currentBriefcase.getBriefcaseState().getDate()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
        }
    }

    private void err() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                    .getExternalContext().getRequest();

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
                    .getExternalContext().getResponse();
            response.sendRedirect(request.getContextPath() + "/ru/mephi/iw/briefcases/UserBriefcases.xhtml");
        } catch (Exception e) {
            throw new IwRuntimeException("", e);
        }
    }

    public void showBriefcaseStruct(){
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            currentBriefcase = sqlSession.getMapper(BIFUMapper.class)
                    .selectBIFUByDate(briefcaseId, dateForShow);
        }
    }

    public void uploadFile() {
        if (downloadFile.getFileName() == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Предупреждение!"
                    , "Файл с данными не был загружен на страницу!");
            return;
        }

        if (!downloadFile.getFileName().endsWith(".xml")) {
            addMessage(FacesMessage.SEVERITY_WARN, "Предупреждение!"
                    , "Файл с данными должен быть в xml формате!");
            return;
        }

        try {
            File fileUpload = File.createTempFile("uploaded_file", ".xml");
            try (OutputStream writer = new FileOutputStream(fileUpload)) {
                writer.write(downloadFile.getContent());
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateForUploadFile);
            new UpsertBriefcase().uploadBriefcase(downloadFile.getFileName(), fileUpload
                    , currentBriefcase.getBriefcase()
                    , LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1
                            , calendar.get(Calendar.DAY_OF_MONTH)));

        } catch (IwRuntimeException e) {
            addMessage(FacesMessage.SEVERITY_INFO, "Информация!"
                    , e.getMessage());
            e.printStackTrace();
            return;
        } catch (FileNotFoundException e) {
            addMessage(FacesMessage.SEVERITY_WARN, "Предупреждение!"
                    , "Файл с данными не был загружен на страницу!");
            e.printStackTrace();
            return;
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Ошибка!"
                    , "Внутренняя ошибка, обратитесь к администратору");
            e.printStackTrace();
            return;
        }

        addMessage(FacesMessage.SEVERITY_INFO, "Успех!"
                , "Данные успешно загружены!");
    }

    public void uploadData() {
        Exception exception;
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            try {
                sqlSession.getMapper(BriefcasesMapper.class).updateBriefcase(briefcaseId, currentBriefcase.getBriefcase());
                sqlSession.commit();
            } catch (Exception e) {
                try {
                    sqlSession.rollback();
                    throw e;
                } catch (Exception e1) {
                    e.addSuppressed(e1);
                    throw e;
                }
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Ошибка!"
                    , "Внутренняя ошибка, обратитесь к администратору!");
            e.printStackTrace();
            return;
        }

        addMessage(FacesMessage.SEVERITY_INFO, "Успех!"
                , "Данные успешно обновлены!");
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }
}
