package ru.mephi.iw.ui.beans.briefcases;

import lombok.Data;
import org.primefaces.model.file.UploadedFile;
import ru.mephi.iw.download.briefcases.UpsertBriefcase;
import ru.mephi.iw.exceptions.IwRuntimeException;
import ru.mephi.iw.models.briefcases.Briefcases;
import ru.mephi.iw.ui.beans.hat.Hat;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.*;

@ManagedBean(name = "ab")
@ViewScoped
@Data
public class AddBriefcase implements Serializable{

    private Briefcases newBriefcase = new Briefcases();
    private UploadedFile downloadFile;
    private UpsertBriefcase upsertBriefcase = new UpsertBriefcase();

    @ManagedProperty(value = "#{hat}")
    private Hat hat;

    public String uploadFile() {
        if (downloadFile.getFileName() == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Предупреждение!"
                    , "Файл с данными не был загружен на страницу!");
            return "";
        }

        if (!downloadFile.getFileName().endsWith(".xml")) {
            addMessage(FacesMessage.SEVERITY_WARN, "Предупреждение!"
                    , "Файл с данными должен быть в xml формате!");
            return "";
        }

        try {
            File fileUpload = File.createTempFile("uploaded_file", ".xml");
            try (OutputStream writer = new FileOutputStream(fileUpload)) {
                writer.write(downloadFile.getContent());
            }
            int userId = hat.getCurrentUserInfo().getId();
            newBriefcase.setUserId(userId);
            upsertBriefcase.addBriefcase(downloadFile.getFileName(), fileUpload, newBriefcase);
        } catch (IwRuntimeException e) {
            addMessage(FacesMessage.SEVERITY_INFO, "Информация!"
                    , e.getMessage());
            e.printStackTrace();
            return "";
        } catch (FileNotFoundException e) {
            addMessage(FacesMessage.SEVERITY_WARN, "Предупреждение!"
                    , "Файл с данными не был загружен на страницу!");
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Ошибка!"
                    , "Внутренняя ошибка, обратитесь к администратору");
            e.printStackTrace();
            return "";
        }

        return "/ru/mephi/iw/briefcases/BriefcaseView.xhtml?id="+ newBriefcase.getId() + "faces-redirect=true";
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }
}
