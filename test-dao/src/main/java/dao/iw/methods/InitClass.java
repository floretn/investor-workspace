package dao.iw.methods;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.IOException;
import java.io.InputStream;

public class InitClass {

    private static volatile InputStream inputStream;
    static {
        try {
            inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static final SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);;

    private InitClass() {
    }
}