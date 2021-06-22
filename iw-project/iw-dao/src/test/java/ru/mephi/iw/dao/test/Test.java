package ru.mephi.iw.dao.test;

import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.auth.collections.CurrentUserInfoMapper;
import ru.mephi.iw.dao.mappers.auth.collections.UsersInfoForAdminMapper;
import ru.mephi.iw.dao.mappers.briefcases.BriefcasesMapper;
import ru.mephi.iw.dao.mappers.briefcases.FilesMapper;
import ru.mephi.iw.dao.mappers.briefcases.collections.BIFUMapper;
import ru.mephi.iw.models.briefcases.Files;

import java.util.HashSet;

public class Test {
    public static void main(String[] args) {
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            sqlSession.getMapper(BriefcasesMapper.class).selectBriefcase(24);
            System.out.println(sqlSession.getMapper(BIFUMapper.class).selectLastBIFU(24));
        }
    }
}
