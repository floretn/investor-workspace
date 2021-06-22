package ru.mephi.iw.ui.beans.auth_pages;

import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.auth.UserMapper;
import ru.mephi.iw.dao.work.WorkWithCurrentUserInfo;
import ru.mephi.iw.models.auth.collections.CurrentUserInfo;
import ru.mephi.iw.security.PwdCoder;
import ru.mephi.iw.ui.beans.hat.Hat;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name="profile")
@ViewScoped
@Data
public class Profile implements Serializable {

    private CurrentUserInfo currentUserInfo;
    private List<String> rolesOfUserS;

    private String pwdOld = "";
    private String pwdOldU = "";
    private PwdCoder pwdCoder = new PwdCoder();

    @ManagedProperty(value = "#{hat}")
    private Hat hat;

    @PostConstruct
    private void init() {
        currentUserInfo = (CurrentUserInfo) ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest()).getSession().getAttribute("user");
    }

    public void updateUser() {
        if (checkInfo()) {
            try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
                try {
                    sqlSession.getMapper(UserMapper.class).updateUser(currentUserInfo.getUser().getId(),
                            currentUserInfo.getUser());
                    sqlSession.commit();
                } catch (Exception e) {
                    try {
                        sqlSession.rollback();
                    } catch (Exception e1) {
                        e.addSuppressed(e1);
                        throw e;
                    }
                }
            }
            hat.setUsername(currentUserInfo.getUser().getUsername());
            addMessage(FacesMessage.SEVERITY_INFO,
                    "Успех!", "Изменения приняты!");
        }
    }

    public String deleteUser() {
        if (!checkPwd(pwdOld)) {
            pwdOld = "";
            return "";

        }
        pwdOld = "";

        new WorkWithCurrentUserInfo().deleteUser(currentUserInfo);
        return hat.loginLogout();
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    private boolean checkInfo() {

        boolean check = true;

        if (!checkPwd(pwdOldU)) {
            check = false;
        }
        pwdOldU = "";

        if (!currentUserInfo.getUser().getPhone().matches("\\+7[0-9]{10}")) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка!", "Телефон должен быть представлен в формате +7XXXXXXXXXX");
            check = false;
        }

        if (!currentUserInfo.getUser().getEmail().matches(".+@[a-z]+\\.[a-z]+")) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка!", "Email введён неправильно!");
            check = false;
        }

        return check;
    }

    private boolean checkPwd(String pwd) {
        if (!pwdCoder.encodePwd(pwd).equals(hat.getCurrentUserInfo().getAuthInfo().getPwd())) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка!", "Неверный пароль!");
            return false;
        }
        return true;
    }
}
