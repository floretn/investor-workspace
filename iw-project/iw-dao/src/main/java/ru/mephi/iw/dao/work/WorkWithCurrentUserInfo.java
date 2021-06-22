package ru.mephi.iw.dao.work;

import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.constants.RolesKeys;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.auth.*;
import ru.mephi.iw.dao.mappers.auth.collections.CurrentUserInfoMapper;
import ru.mephi.iw.dao.mappers.briefcases.BriefcasesMapper;
import ru.mephi.iw.exceptions.IwRuntimeException;
import ru.mephi.iw.models.auth.*;
import ru.mephi.iw.models.auth.collections.CurrentUserInfo;
import ru.mephi.iw.models.auth.collections.UsersInfoForAdmin;
import ru.mephi.iw.models.briefcases.Briefcases;

public class WorkWithCurrentUserInfo {

    public CurrentUserInfo insertUser(User user, AuthInfo authInfo) {
        IwRuntimeException exception = null;
        CurrentUserInfo currentUserInfo = null;
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            try {
                sqlSession.getMapper(UserMapper.class).insertUser(user);
                authInfo.setUserId(user.getId());
                sqlSession.getMapper(AuthInfoMapper.class).insertAuthInfo(authInfo);
                sqlSession.getMapper(ROUMapper.class).insertROU(new RolesOfUsers(0, user.getId(), RolesKeys.USER_KEY, true));
                sqlSession.getMapper(ROUMapper.class).insertROU(new RolesOfUsers(0, user.getId(), RolesKeys.ADMIN_KEY, false));
                sqlSession.commit();
                currentUserInfo = sqlSession.getMapper(CurrentUserInfoMapper.class)
                        .selectCurrentUserInfo(authInfo.getLogin(), authInfo.getPwd());
            } catch (Exception ex) {
                try {
                    sqlSession.rollback();
                } catch (Exception ex1) {
                    ex.addSuppressed(ex1);
                }
                exception = new IwRuntimeException("Внутренняя ошибка!", ex);
            }
        }

        if (exception != null) {
            throw exception;
        }
        return currentUserInfo;
    }


    public void deleteUser(CurrentUserInfo currentUserInfo) {
        met(currentUserInfo.getId());
    }

    private void met(int id) {
        IwRuntimeException exception = null;
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            try {

                sqlSession.getMapper(AuthInfoMapper.class).deleteAllAuthInfoOfUser(id);

                sqlSession.getMapper(ROUMapper.class).deleteAllROUOfUser(id);

                BriefcasesMapper briefcasesMapper = sqlSession.getMapper(BriefcasesMapper.class);
                for (Briefcases briefcase : briefcasesMapper.selectBriefcasesOfUser(id)) {
                    new WorkWithBriefcases().deleteBriefcase(briefcase);
                }

                sqlSession.getMapper(UserMapper.class).deleteUser(id);

                sqlSession.commit();
            } catch (Exception e) {
                try {
                    sqlSession.rollback();
                    throw e;
                } catch (Exception e1) {
                    e.addSuppressed(e1);
                    exception = new IwRuntimeException("Внутренняя ошибка!", e);
                }
            }

            if (exception != null) {
                throw exception;
            }
        }
    }

    public void deleteUser(UsersInfoForAdmin usersInfoForAdmin) {
        met(usersInfoForAdmin.getId());
    }

    public void updateAuthInfo (AuthInfo authInfo){
        IwRuntimeException exception = null;
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            try {
                sqlSession.getMapper(AuthInfoMapper.class).updateAuthInfo(authInfo.getId(), authInfo);
                sqlSession.commit();
            } catch (Exception e) {
                try {
                    sqlSession.rollback();
                    throw e;
                } catch (Exception e1) {
                    e.addSuppressed(e1);
                    exception = new IwRuntimeException("Внутренняя ошибка!", e);
                }
            }

            if (exception != null) {
                throw exception;
            }
        }
    }
}
