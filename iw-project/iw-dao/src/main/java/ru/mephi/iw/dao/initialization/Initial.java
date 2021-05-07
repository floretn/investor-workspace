package ru.mephi.iw.dao.initialization;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import ru.mephi.iw.exceptions.IwRuntimeException;
import java.io.IOException;
import java.io.InputStream;

public class Initial {

    private Initial() {
    }

    public static final SqlSessionFactory SQL_SESSION_FACTORY;

    static {
        try (InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml")) {
            SQL_SESSION_FACTORY = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new IwRuntimeException("Не найден файл конфигурации взаимодействия с Базой Данных", e);
        }
    }
}