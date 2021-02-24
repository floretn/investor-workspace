package ru.mephi.iw.fillIMOEX;

import ru.mephi.iw.exceptions.CanNotDeleteFile;
import java.io.IOException;

public class Test {

    public static void main(String[] args) throws InterruptedException, IOException, CanNotDeleteFile {
        FillIMOEXInDB.fill();
    }
}
