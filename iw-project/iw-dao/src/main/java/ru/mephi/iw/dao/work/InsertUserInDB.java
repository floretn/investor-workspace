package ru.mephi.iw.dao.work;

import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.auth.AuthInfoMapper;
import ru.mephi.iw.dao.mappers.auth.UserMapper;
import ru.mephi.iw.models.auth.AuthInfo;
import ru.mephi.iw.models.auth.User;

public class InsertUserInDB {

    public void insertUser(User user, AuthInfo authInfo) {
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            try {
                sqlSession.getMapper(UserMapper.class).insertUser(user);
                authInfo.setUserId(user.getId());
                sqlSession.getMapper(AuthInfoMapper.class).insertAuthInfo(authInfo);
                sqlSession.commit();
            } catch (Exception ex) {
                try {
                    sqlSession.rollback();
                } catch (Exception ex1) {
                    ex.addSuppressed(ex1);
                }
                throw ex;
            }
        }
    }
}
