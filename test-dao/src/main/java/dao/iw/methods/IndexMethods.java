package dao.iw.methods;

import dao.iw.mappers.IndexMapper;
import models.Index;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

public class IndexMethods {

    private static volatile SqlSession sqlSession;
    private static volatile IndexMapper mapper;

    public IndexMethods() throws IOException {
        if(mapper == null) {
            init();
        }
    }

    private static void init() throws IOException {
        InitClass.sqlSessionFactory.getConfiguration().addMapper(IndexMapper.class);
        SqlSession session = InitClass.sqlSessionFactory.openSession();
        mapper = session.getMapper(IndexMapper.class);
        sqlSession = session;
    }

    public List<Index> showAllIndexes(){
        return sqlSession.selectList("selectAllIndexes");
    }

    public Index selectIndex(int id){
        return mapper.selectIndex(id);
    }

    public void insertIndex(Index Index){
        mapper.insertIndex(Index);
    }

    public void deleteIndex(int id){
        mapper.deleteIndex(id);
    }

    public void updateIndex(int id, Index Index){
        mapper.updateIndex(id, Index);
    }
}
