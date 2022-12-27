package pl.javastart.library.io.file;

import pl.javastart.library.model.Liblary;

public interface FileManager {
    Liblary importData();
    void exportData(Liblary liblary);
}
