package ru.mephi.iw.ui.beans.settings;

import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.auth.AuthInfoMapper;
import ru.mephi.iw.dao.work.WorkWithCurrentUserInfo;
import ru.mephi.iw.models.auth.AuthInfo;
import ru.mephi.iw.ui.beans.auth_pages.Auth;
import ru.mephi.iw.security.PwdCoder;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name="ss")
@ViewScoped
@Data
public class SafetySettings implements Serializable {

    @ManagedProperty(value = "#{auth}")
    private Auth auth;

    private String login;
    private String pwd = "";
    private String pwd1 = "";

    private String pwdOld;

    private PwdCoder pwdCoder = new PwdCoder();

    @PostConstruct
    private void init() {
        login = auth.getCurrentUserInfo().getAuthInfo().getLogin();
    }

    public void updateLog() {

        if (!checkLog()) {
            login = auth.getCurrentUserInfo().getAuthInfo().getLogin();
            return;
        }

        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {

            AuthInfo authInfo = sqlSession.getMapper(AuthInfoMapper.class).selectAuthInfo(login);

            if (authInfo != null) {
                addMessage(FacesMessage.SEVERITY_ERROR,
                        "Ошибка!", "Пользователь с таким логином уже существует!");
            }

            AuthInfo authInfoNew = auth.getCurrentUserInfo().getAuthInfo();
            authInfoNew.setLogin(login);

            new WorkWithCurrentUserInfo().updateAuthInfo(authInfoNew);
        }

        addMessage(FacesMessage.SEVERITY_INFO,
                "Успех!", "Изменения приняты!");

        login = auth.getCurrentUserInfo().getAuthInfo().getLogin();
    }

    private boolean checkLog() {

        if (checkInfo()) {
            return false;
        }

        if (login.equals(auth.getCurrentUserInfo().getAuthInfo().getLogin())) {
            addMessage(FacesMessage.SEVERITY_WARN,
                    "Предупреждение!", "Логин должен отличаться от старого!");
            return false;
        }

        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            AuthInfo authInfo = sqlSession.getMapper(AuthInfoMapper.class).selectAuthInfo(login);

            if (authInfo != null) {
                addMessage(FacesMessage.SEVERITY_WARN,
                        "Предупреждение!", "Пользователь с таким логином уже существует!");
                return false;
            }
        }

        return true;
    }

    public void updatePwd() {

        if (!checkPwd()) {
            pwd = "";
            pwd1 = "";
            return;
        }

        pwd = pwdCoder.encodePwd(pwd);


        AuthInfo authInfoNew = auth.getCurrentUserInfo().getAuthInfo();
        authInfoNew.setPwd(pwd);

        new WorkWithCurrentUserInfo().updateAuthInfo(authInfoNew);

        addMessage(FacesMessage.SEVERITY_INFO,
                "Успех!", "Изменения приняты!");
        pwd = "";
        pwd1 = "";
    }

    private boolean checkPwd() {
        if (checkInfo()) {
            return false;
        }

        if (!pwd.equals(pwd1)) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка!", "Вы неправильно повторили пароль!");
            return false;
        }

        if (pwdCoder.encodePwd(pwd).equals(auth.getCurrentUserInfo().getAuthInfo().getPwd())) {
            addMessage(FacesMessage.SEVERITY_WARN,
                    "Предупреждение: Пароль не изменился!",
                    "Пароль должен отличаться от старого!");
            return false;
        }

        if (!pwd.matches("^(?=.*[0-9])(?=.*[a-zа-я])(?=.*[A-ZА-Я]).{8,}$")) {
            addMessage(FacesMessage.SEVERITY_WARN,
                    "Предупреждение: Пароль слишком простой!",
                    "Пароль должен состоять минимум из 8 символов: цифр, заглавных и строчных букв!");
            return false;
        }

        return true;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    private boolean checkInfo() {

        if (!pwdCoder.encodePwd(pwdOld).equals(auth.getCurrentUserInfo().getAuthInfo().getPwd())) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка!", "Неверный пароль!");
            return true;
        }

        return false;
    }

}
