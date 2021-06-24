package ru.mephi.iw.dao.test;

import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.briefcases.BriefcasesMapper;
import ru.mephi.iw.dao.mappers.briefcases.collections.BIFUMapper;

public class Test {
    public static void main(String[] args) {
        try (SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            sqlSession.getMapper(BriefcasesMapper.class).selectBriefcase(24);
            System.out.println(sqlSession.getMapper(BIFUMapper.class).selectAllLastBIFU(24));
        }
    }
}
