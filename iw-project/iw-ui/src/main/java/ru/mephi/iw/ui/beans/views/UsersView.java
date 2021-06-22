package ru.mephi.iw.ui.beans.views;

import lombok.Data;
import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.auth.collections.CurrentUserInfoMapper;
import ru.mephi.iw.dao.work.WorkWithCurrentUserInfo;
import ru.mephi.iw.models.auth.Roles;
import ru.mephi.iw.models.auth.collections.CurrentUserInfo;
import ru.mephi.iw.security.PwdCoder;
import ru.mephi.iw.ui.beans.hat.Hat;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@ManagedBean(name="usersView")
@SessionScoped
@Data
public class UsersView implements Serializable {

    private Set<CurrentUserInfo> usersInfo;
    private Set<CurrentUserInfo> usersInfoInDB;

    private String username = "";
    private String lastName = "";
    private String firstName = "";
    private String patronymic = "";
    private String phone = "";
    private String email = "";
    private String role = "";

    private CurrentUserInfo selectedUserForDelete;

    private String pwd;
    private String pwd1;
    private String pwdOld;

    private PwdCoder pwdCoder = new PwdCoder();

    @ManagedProperty(value = "#{hat}")
    private Hat hat;

    @PostConstruct
    private void init() {
        updateData();
    }

    public String prepareForUpdate(CurrentUserInfo currentUserInfo) {
        return "/ru/mephi/iw/views/UserInfoChange.xhtml?id=" + currentUserInfo.getId() + "faces-redirect=true";
    }

    public void updateData() {
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            usersInfoInDB = sqlSession.getMapper(CurrentUserInfoMapper.class).selectAllCurrentUserInfo();
            usersInfo = usersInfoInDB;
        }
    }

    public void prepareForDelete(CurrentUserInfo currentUserInfo) {
        selectedUserForDelete = currentUserInfo;
    }

    public void updateUserList() {
        usersInfo = usersInfoInDB.parallelStream().filter(u -> username.equals("") || u.getUser().getUsername().toLowerCase(Locale.ROOT)
                .contains(username.toLowerCase(Locale.ROOT)))
                .filter(u -> lastName.equals("") || u.getUser().getLastName().toLowerCase(Locale.ROOT)
                        .contains(lastName.toLowerCase(Locale.ROOT)))
                .filter(u -> firstName.equals("") || u.getUser().getFirstName().toLowerCase(Locale.ROOT)
                        .contains(firstName.toLowerCase(Locale.ROOT)))
                .filter(u -> patronymic.equals("") || u.getUser().getPatronymic().toLowerCase(Locale.ROOT)
                        .contains(patronymic.toLowerCase(Locale.ROOT)))
                .filter(u -> phone.equals("") || u.getUser().getPhone().equals(phone))
                .filter(u -> email.equals("") || u.getUser().getEmail().equals(email))
                .filter(u -> role.equals("") || (role.equals("Без роли") && u.getRolesOfUser().isEmpty())
                        || checkRole(u.getRolesOfUser()))
                .collect(Collectors.toSet());

        if (usersInfo.isEmpty()) {
            addMessage(FacesMessage.SEVERITY_WARN,
                    "Предупреждение!", "Совпадений не найдено!");
        }
    }

    private boolean checkRole(Set<Roles> rolesOfUsers) {
        for (Roles roleOfUser : rolesOfUsers) {
            if (roleOfUser.getName().equals(role)) {
                return true;
            }
        }
        return false;
    }

    public void deleteUser() {

        if (checkInfo()) {
            return;
        }

        new WorkWithCurrentUserInfo().deleteUser(selectedUserForDelete);
        usersInfoInDB.remove(selectedUserForDelete);
        usersInfo.remove(selectedUserForDelete);
        selectedUserForDelete = null;
    }

    public void trash() {
        username = "";
        lastName = "";
        firstName = "";
        patronymic = "";
        phone = "";
        email = "";
        usersInfo = usersInfoInDB;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    private boolean checkInfo() {

        if (!pwdCoder.encodePwd(pwdOld).equals(hat.getCurrentUserInfo().getAuthInfo().getPwd())) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка!", "Неверный пароль!");
            return true;
        }

        return false;
    }
}
