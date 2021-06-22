package ru.mephi.iw.dao.work;

import org.apache.ibatis.session.SqlSession;
import ru.mephi.iw.dao.initialization.Initial;
import ru.mephi.iw.dao.mappers.briefcases.*;
import ru.mephi.iw.exceptions.IwRuntimeException;
import ru.mephi.iw.models.briefcases.Account;
import ru.mephi.iw.models.briefcases.BriefcaseStates;
import ru.mephi.iw.models.briefcases.Briefcases;
import ru.mephi.iw.models.briefcases.StocksInBriefcases;

public class WorkWithBriefcases {

    public void deleteBriefcase(Briefcases briefcase) {
        IwRuntimeException exception = null;
        try(SqlSession sqlSession = Initial.SQL_SESSION_FACTORY.openSession()) {
            try {
                BSMapper bsMapper = sqlSession.getMapper(BSMapper.class);
                for (BriefcaseStates briefcaseStates : bsMapper.selectAllBSOfBriefcase(briefcase.getId())) {

                    sqlSession.getMapper(AccountMapper.class).deleteAllAccountsInBS(briefcaseStates.getId());

                    sqlSession.getMapper(SIBMapper.class).deleteAllSIBInBS(briefcaseStates.getId());

                    bsMapper.deleteBS(briefcaseStates.getId());

                    sqlSession.getMapper(FilesMapper.class).deleteFile(briefcaseStates.getFileId());
                }

                sqlSession.getMapper(BriefcasesMapper.class).deleteBriefcases(briefcase.getId());
                sqlSession.commit();
            }  catch (Exception e) {
                try {
                    sqlSession.rollback();
                } catch (Exception e1) {
                    e.addSuppressed(e1);
                }
                exception = new IwRuntimeException("", e);
            }
        }

        if (exception != null) {
            throw exception;
        }
    }
}
