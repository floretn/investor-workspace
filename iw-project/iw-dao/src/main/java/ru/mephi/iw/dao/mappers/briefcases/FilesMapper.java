package ru.mephi.iw.dao.mappers.briefcases;

import org.apache.ibatis.annotations.Param;
import ru.mephi.iw.models.briefcases.Files;

public interface FilesMapper {

    Files selectFile(@Param("id") int id);

    void insertFile(@Param("file") Files file);

    void deleteFile(@Param("id") int id);
}
