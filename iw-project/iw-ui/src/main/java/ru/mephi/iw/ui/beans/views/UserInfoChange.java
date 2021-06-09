package ru.mephi.iw.ui.beans.views;

import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.constants.RolesKeys;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.auth.AuthInfoMapper;
import ru.mephi.iw.dao.mappers.auth.ROUMapper;
import ru.mephi.iw.dao.work.WorkWithCurrentUserInfo;
import ru.mephi.iw.models.auth.AuthInfo;
import ru.mephi.iw.models.auth.RolesOfUsers;
import ru.mephi.iw.models.auth.collections.UsersInfoForAdmin;
import ru.mephi.iw.security.PwdCoder;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="userInfoChange")
@ViewScoped
@Data
public class UserInfoChange implements Serializable {

    @ManagedProperty(value = "#{usersView}")
    UsersView usersView;

    UsersInfoForAdmin user;

    private RolesOfUsers adminRole;
    private RolesOfUsers userRole;

    private String login = "";
    private List<String> logins;

    private String pwdOld;
    private String pwd;
    private String pwd1;

    private PwdCoder pwdCoder = new PwdCoder();

    @PostConstruct
    private void init() {
        user = usersView.getSelectedUserForUpdate();
        for (RolesOfUsers role : user.getRolesOfUser()) {
            switch (role.getRoleId()) {
                case RolesKeys.ADMIN_KEY:
                    adminRole = role;
                    break;
                case RolesKeys.USER_KEY:
                    userRole = role;
            }
        }
        logins = new ArrayList<>();
        for (AuthInfo authInfo : user.getAuthInfo()) {
            logins.add(authInfo.getLogin());
        }
    }

    public void updateUserPwd() {
        if (!checkPwd()) {
            return;
        }

        if (login.equals("")) {
            addMessage(FacesMessage.SEVERITY_WARN,
                    "Предупреждение!", "Выберите логин пользователя!");
            return;
        }

        AuthInfo authInfo = null;
        for (AuthInfo ai : user.getAuthInfo()) {
            if (ai.getLogin().equals(login)) {
                authInfo = ai;
                break;
            }
        }

        if (authInfo == null) {
            addMessage(FacesMessage.SEVERITY_WARN,
                    "Предупреждение!", "Такой логин у пользователя не существует! \nВозможно, только что был удалён");
            return;
        }
        authInfo.setPwd(pwdCoder.encodePwd(pwd));

        new WorkWithCurrentUserInfo().updateAuthInfo(authInfo);

        addMessage(FacesMessage.SEVERITY_INFO,
                "Успех!", "Данные успешно изменены!");
    }

    public void updateUserRoles() {
        if (checkInfo()) {
            return;
        }

        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            try {
                sqlSession.getMapper(ROUMapper.class).updateROUStatus(userRole.getId(), userRole);
                sqlSession.getMapper(ROUMapper.class).updateROUStatus(adminRole.getId(), adminRole);
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
        }
        addMessage(FacesMessage.SEVERITY_INFO,
                "Успех!", "Данные успешно изменены!");
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

        return true;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    private boolean checkInfo() {

        if (!pwdCoder.encodePwd(pwdOld).equals(usersView.getAuth().getCurrentUserInfo().getAuthInfo().getPwd())) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка!", "Неверный пароль!");
            return true;
        }

        return false;
    }

}
