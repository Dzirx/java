package pl.javastart.library.io.file;

import pl.javastart.library.exception.DataExportException;
import pl.javastart.library.exception.DataImportException;
import pl.javastart.library.model.Liblary;

import java.io.*;

public class SerializableFileManager implements FileManager{
    private static final String FILE_NAME = "Library.o";

    @Override
    public Liblary importData() {
        try (FileInputStream fis = new FileInputStream(FILE_NAME);
             ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            return (Liblary) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new DataImportException("Brak pliku " + FILE_NAME);
        } catch (IOException e) {
            throw new DataImportException("Błąd odczytu pliku " + FILE_NAME);
        } catch (ClassNotFoundException e) {
            throw new DataImportException("Niezgodny typ danych w pliku " + FILE_NAME);
        }
    }

    @Override
    public void exportData(Liblary liblary) {
        try(
                var fos = new FileOutputStream(FILE_NAME);
                var oos = new ObjectOutputStream(fos);
                ){
            oos.writeObject(liblary);
        } catch (FileNotFoundException e) {
            throw new DataExportException("Brak Pliku " + FILE_NAME);
        } catch (IOException e) {
            throw new DataExportException("Blad zapisu danych do pliku " + FILE_NAME);
        }
    }
}
