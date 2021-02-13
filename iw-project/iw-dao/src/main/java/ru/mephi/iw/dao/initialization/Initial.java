package ru.mephi.iw.dao.initialization;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import ru.mephi.iw.dao.mappers.*;
import ru.mephi.iw.dao.mappers.stocks.*;
import java.io.IOException;
import java.io.InputStream;

public class Initial {

    private static volatile InputStream inputStream;
    static {
        try {
            inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static final SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    static {
        sqlSessionFactory.getConfiguration().addMapper(CompanyMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(CurrencyMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(DoicMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(IndexMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(SectorMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(SiiMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(SocMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(SpMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(StockMapper.class);
    }
    private static final SqlSession session = Initial.sqlSessionFactory.openSession();
    public static final CompanyMapper companyMapper = session.getMapper(CompanyMapper.class);
    public static final CurrencyMapper currencyMapper = session.getMapper(CurrencyMapper.class);
    public static final DoicMapper doicMapper = session.getMapper(DoicMapper.class);
    public static final IndexMapper indexMapper = session.getMapper(IndexMapper.class);
    public static final SectorMapper sectorMapper = session.getMapper(SectorMapper.class);
    public static final SiiMapper siiMapper = session.getMapper(SiiMapper.class);
    public static final SocMapper socMapper = session.getMapper(SocMapper.class);
    public static final SpMapper spMapper = session.getMapper(SpMapper.class);
    public static final StockMapper stockMapper = session.getMapper(StockMapper.class);

    private Initial() {
    }
}
