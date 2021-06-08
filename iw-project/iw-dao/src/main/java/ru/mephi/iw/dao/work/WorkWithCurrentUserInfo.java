package ru.mephi.iw.dao.work;

import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.constants.RolesKeys;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.auth.*;
import ru.mephi.iw.dao.mappers.auth.collections.CurrentUserInfoMapper;
import ru.mephi.iw.models.auth.*;
import ru.mephi.iw.models.auth.collections.CurrentUserInfo;
import ru.mephi.iw.models.auth.collections.UsersInfoForAdmin;

public class WorkWithCurrentUserInfo {

    public CurrentUserInfo insertUser(User user, AuthInfo authInfo) {
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            try {
                sqlSession.getMapper(UserMapper.class).insertUser(user);
                authInfo.setUserId(user.getId());
                sqlSession.getMapper(AuthInfoMapper.class).insertAuthInfo(authInfo);
                sqlSession.getMapper(ROUMapper.class).insertROU(new RolesOfUsers(0, user.getId(), RolesKeys.USER_KEY, true));
                sqlSession.getMapper(ROUMapper.class).insertROU(new RolesOfUsers(0, user.getId(), RolesKeys.ADMIN_KEY, false));
                sqlSession.commit();
            } catch (Exception ex) {
                try {
                    sqlSession.rollback();
                } catch (Exception ex1) {
                    ex.addSuppressed(ex1);
                }
                throw ex;
            }
            return sqlSession.getMapper(CurrentUserInfoMapper.class).selectCurrentUserInfo(authInfo.getLogin(), authInfo.getPwd());
        }
    }

    public void deleteUser(CurrentUserInfo currentUserInfo) {
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            try {

                AuthInfoMapper authInfoMapper = sqlSession.getMapper(AuthInfoMapper.class);
                for (AuthInfo authInfo : sqlSession.getMapper(AuthInfoMapper.class).
                        selectAuthInfoOfUser(currentUserInfo.getId())) {
                    authInfoMapper.deleteAuthInfo(authInfo.getId());
                }

                ROUMapper rouMapper = sqlSession.getMapper(ROUMapper.class);
                for (RolesOfUsers roles : sqlSession.getMapper(ROUMapper.class).selectROU(currentUserInfo.getId())) {
                    rouMapper.deleteROU(roles.getId());
                }

                sqlSession.getMapper(UserMapper.class).deleteUser(currentUserInfo.getUser().getId());

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
    }

    public void deleteUser(UsersInfoForAdmin usersInfoForAdmin) {
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            try {

                AuthInfoMapper authInfoMapper = sqlSession.getMapper(AuthInfoMapper.class);
                for (AuthInfo authInfo : usersInfoForAdmin.getAuthInfo()) {
                    authInfoMapper.deleteAuthInfo(authInfo.getId());
                }

                ROUMapper rouMapper = sqlSession.getMapper(ROUMapper.class);
                for (RolesOfUsers roles : usersInfoForAdmin.getRolesOfUser()) {
                    rouMapper.deleteROU(roles.getId());
                }

                sqlSession.getMapper(UserMapper.class).deleteUser(usersInfoForAdmin.getUser().getId());

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
    }

    public void updateAuthInfo(AuthInfo authInfo) {
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
                    throw e;
                }
            }
        }
    }

}
