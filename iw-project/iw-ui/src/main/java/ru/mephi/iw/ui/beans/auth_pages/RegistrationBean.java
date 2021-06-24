package ru.mephi.iw.ui.beans.auth_pages;

import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.auth.AuthInfoMapper;
import ru.mephi.iw.dao.mappers.auth.UserMapper;
import ru.mephi.iw.dao.work.WorkWithCurrentUserInfo;
import ru.mephi.iw.models.auth.AuthInfo;
import ru.mephi.iw.models.auth.User;
import ru.mephi.iw.models.auth.collections.CurrentUserInfo;
import ru.mephi.iw.security.PwdCoder;
import ru.mephi.iw.ui.beans.hat.HatBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name="registration")
@ViewScoped
@Data
public class RegistrationBean implements Serializable {

    private User userNew = new User(0, "", "", "", "", "", "");
    private AuthInfo authInfoNew = new AuthInfo(0, 0, "", "");
    private String pwdNew = "";
    private String pwd1New = "";
    private PwdCoder pwdCoder = new PwdCoder();

    @ManagedProperty(value = "#{hat}")
    private HatBean hat;

    public String insertUser() {

        if (!checkInfo()) {
            return "";
        }

        String password = pwdCoder.encodePwd(pwdNew);

        authInfoNew.setPwd(password);
        CurrentUserInfo currentUserInfo = new WorkWithCurrentUserInfo().insertUser(userNew, authInfoNew);
        hat.setCurrentUserInfo(currentUserInfo);

        ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession()
                .setAttribute("user", currentUserInfo);

        return "Profile?faces-redirect=true";
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    private boolean checkInfo() {

        boolean check = true;


        if (hat.getCurrentUserInfo() != null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Предупреждение!", "Сначала нужно выйти из аккаунта!");
            check = false;
        }

        if (!pwdNew.equals(pwd1New)) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка!", "Вы неправильно повторили пароль!");
            check = false;
        }

        if (!userNew.getPhone().matches("\\+7[0-9]{10}")) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка!", "Телефон должен быть представлен в формате +7XXXXXXXXXX");
            check = false;
        }

        if (!userNew.getEmail().matches(".+@[a-z]+\\.[a-z]+")) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка!", "Email введён неправильно!");
            check = false;
        }

        if (!pwdNew.matches("^(?=.*[0-9])(?=.*[a-zа-я])(?=.*[A-ZА-Я]).{8,}$")) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Предупреждение: Пароль слишком простой!",
                    "Пароль должен состоять минимум из 8 символов: цифр, заглавных и строчных букв!");
            check = false;
        }

        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            AuthInfo authInfo = sqlSession.getMapper(AuthInfoMapper.class).selectAuthInfo(authInfoNew.getLogin());

            if (authInfo != null) {
                addMessage(FacesMessage.SEVERITY_WARN,
                        "Предупреждение!", "Пользователь с таким логином уже существует!");
                check = false;
            }

            List<User> users = sqlSession.getMapper(UserMapper.class).selectUserByUN(userNew.getUsername());

            if (users.size() != 0) {
                addMessage(FacesMessage.SEVERITY_ERROR,
                        "Ошибка!", "Пользователь с таким псевдонимом уже существует!");
                check = false;
            }
        }

        return check;
    }
}
