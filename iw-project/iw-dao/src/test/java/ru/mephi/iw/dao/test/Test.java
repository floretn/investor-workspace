package ru.mephi.iw.dao.test;

import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.auth.collections.CurrentUserInfoMapper;
import ru.mephi.iw.dao.mappers.auth.collections.UsersInfoForAdminMapper;

import java.util.HashSet;

public class Test {
    public static void main(String[] args) {
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            System.out.println(sqlSession.getMapper(CurrentUserInfoMapper.class).
                    selectCurrentUserInfo("kek", "2yjzmvsnm7lesgf2ee76u9a61pci736vwdngv3xqvwtbvono5s"));
            System.out.println(sqlSession.getMapper(CurrentUserInfoMapper.class).
                    selectCurrentUserInfo("AneMone", "648m4rsgqf187bjl33kjmzqosrg9j20ka3gcdnxe6arpzhzi6d"));
            System.out.println(sqlSession.getMapper(CurrentUserInfoMapper.class).
                    selectCurrentUserInfo("AneMone", "srg9j20ka3gcdnxe6arpzhzi6d"));
        }
    }
}
