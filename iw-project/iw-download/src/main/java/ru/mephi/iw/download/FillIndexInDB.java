package ru.mephi.iw.download;

import java.io.File;
import java.util.Date;

public interface FillIndexInDB {
    boolean fill(boolean trustInfo, Date dateForDownload, File fileWithIndex);
}
