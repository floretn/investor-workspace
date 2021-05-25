package ru.mephi.iw.ui.auth_pages;

import lombok.Data;
import ru.mephi.iw.dao.work.InsertUserInDB;
import ru.mephi.iw.models.auth.AuthInfo;
import ru.mephi.iw.models.auth.User;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name="registration")
@SessionScoped
@Data
public class Registration implements Serializable {

    private User userNew = new User(0, "", "", "", "", "", "");
    private AuthInfo authInfoNew = new AuthInfo(0, 0, "", "");
    private String pwdNew = "";
    private String pwd1New = "";

    @ManagedProperty(value = "#{auth}")
    private Auth auth;

    public String insertUser() {

        if (auth.getUser() != null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Предупреждение!", "Сначала нужно выйти из аккаунта!");
            return "";
        }

        if (!pwdNew.equals(pwd1New)) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка!", "Вы неправильно повторили пароль!");
            return "";
        }

        auth.setUser(userNew);
        authInfoNew.setPwd(pwdNew);
        new InsertUserInDB().insertUser(userNew, authInfoNew);

        pwdNew = "";
        pwd1New = "";
        userNew = new User(0, "", "", "", "", "", "");
        authInfoNew = new AuthInfo(0, 0, "", "");

        return "Profile?faces-redirect=true";
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }
}
