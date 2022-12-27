package pl.javastart.library.io.file;

import pl.javastart.library.exception.NoSuchFieldException;
import pl.javastart.library.exception.NoSuchOptionException;
import pl.javastart.library.io.ConsolePrinter;
import pl.javastart.library.io.DataReader;

public class FileManagerBuilder {
    private ConsolePrinter printer;
    private DataReader reader;

    public FileManagerBuilder(ConsolePrinter printer, DataReader reader) {
        this.printer = printer;
        this.reader = reader;
    }
    public FileManager build(){
        printer.printLine("Wybierz format");
        FileType fileType = getFileType();
        switch (fileType){
            case CSV:
                return new CsvFileManager();
            case SERIAL:
                return new SerializableFileManager();
            default:
                throw new NoSuchFieldException("Nie obslugiwany typ");
        }
    }

    private FileType getFileType() {
        boolean typeOk = false;
        FileType result = null;
        do {
            printerTypes();
            String type = reader.getString().toUpperCase();
            try {
                result = FileType.valueOf(type);
                typeOk = true;
            }catch (IllegalArgumentException e){
                printer.printLine("Nie obslugiwany typ danych, wybierz ponownie");
            }
        }while (!typeOk);
        return result;
    }

    private void printerTypes() {
        for (FileType value : FileType.values()){
            printer.printLine(value.name());
        }
    }
}
