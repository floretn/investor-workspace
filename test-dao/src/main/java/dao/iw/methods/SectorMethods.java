package dao.iw.methods;

import dao.iw.mappers.SectorMapper;
import models.Sector;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

public class SectorMethods {

    private static volatile SqlSession sqlSession;
    private static volatile SectorMapper mapper;

    public SectorMethods() throws IOException {
        if(mapper == null) {
            init();
        }
    }

    private static void init() throws IOException {
        InitClass.sqlSessionFactory.getConfiguration().addMapper(SectorMapper.class);
        SqlSession session = InitClass.sqlSessionFactory.openSession();
        mapper = session.getMapper(SectorMapper.class);
        sqlSession = session;
    }

    public List<Sector> showAllSectors(){
        return sqlSession.selectList("selectAllSectors");
    }

    public Sector selectSector(int id){
        return mapper.selectSector(id);
    }

    public void insertSector(Sector Sector){
        mapper.insertSector(Sector);
    }

    public void deleteSector(int id){
        mapper.deleteSector(id);
    }

    public void updateSector(int id, Sector Sector){
        mapper.updateSector(id, Sector);
    }
}
