package pl.javastart.library.app;

import pl.javastart.library.exception.*;
import pl.javastart.library.io.ConsolePrinter;
import pl.javastart.library.io.DataReader;
import pl.javastart.library.io.file.FileManager;
import pl.javastart.library.io.file.FileManagerBuilder;
import pl.javastart.library.model.*;
import pl.javastart.library.model.comparator.AlphabeticalTitleComparator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;

public class LiblaryControl {
    // zmienne do kontrolowania programu

    ConsolePrinter printer = new ConsolePrinter();
    private DataReader dataReader = new DataReader(printer);

    private FileManager fileManager;

    private Liblary liblary;

    LiblaryControl() {
        fileManager = new FileManagerBuilder(printer, dataReader).build();
        try {
            liblary = fileManager.importData();
            printer.printLine("Zaimportowane dane z pliku");
        } catch (DataImportException | InvalidDataException e) {
            printer.printLine(e.getMessage());
            printer.printLine("Zainicjowano nową bazę.");
            liblary = new Liblary();
        }
    }

    public void controlLoop() {
        Option option;

        do {
            printOptions();
            option = getOption();
            switch (option) {
                case ADD_BOOK:
                    addBook();
                    break;
                case ADD_MAGAZINE:
                    addMagazine();
                    break;
                case PRINT_BOOKS:
                    printBooks();
                    break;
                case PRINT_MAGAZINES:
                    printMagazines();
                    break;
                case DELETE_BOOK:
                    deleteBook();
                    break;
                case DELETE_MAGAZINE:
                    deleteMagazine();
                    break;
                case ADD_USER: //dodano
                    addUser();
                    break;
                case PRINT_USERS: //dodano
                    printUsers();
                    break;
                case EXIT:
                    exit();
                    break;
                case FIND_BOOK:
                    findBook();
                    break;
                default:
                    printer.printLine("Nie ma takiej opcji, wprowadź ponownie: ");
            }
        } while (option != Option.EXIT);
    }

    private void findBook() {
        printer.printLine("Podaj tytlul publikacji");
        String data = dataReader.getString();
        String notFoundMessage = "Brak publikacji o takim tytule";
        liblary.findPublicationByTitle(data)
                .map(Publication::toString)
                .ifPresentOrElse(System.out::println,()-> System.out.println(notFoundMessage));
    }

    private Option getOption(){
        boolean optionOk = false;
        Option option = null;
        while (!optionOk){
            try {
                option = Option.createFromInt(dataReader.getInt());
                optionOk = true;
            }catch (NoSuchOptionException e){
                printer.printLine(e.getMessage() + " podaj ponownie");
            }catch (InputMismatchException e){
                printer.printLine(e.getMessage() + " Wprowadzono wartosc ktora nie jest liczba");
            }
        }
        return option;
    }
    private void printOptions() {
        printer.printLine("Wybierz opcję: ");
        for (Option value : Option.values()) {
            printer.printLine(value.toString());
        }
    }

    private void addBook() {
        try {
        Book book = dataReader.readAndCreateBook();
        liblary.addPublication(book);
        }catch (InputMismatchException e) {
            printer.printLine("Nie udało się utworzyć książki, niepoprawne dane");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Osiągnięto limit pojemności, nie można dodać kolejnej książki");
        }
    }

    private void printBooks() {
        printer.printBooks(liblary.getSortedPublications(
                Comparator.comparing(Publication::getTitle, String.CASE_INSENSITIVE_ORDER)));
    }

    private void addMagazine() {
        Magazine magazine = dataReader.readAndCreateMagazine();
        liblary.addPublication(magazine);
    }
    private void addUser() {
        LibraryUser libraryUser = dataReader.createLibraryUser();
        try {
            liblary.addUser(libraryUser);
        } catch (UserAlreadyExistsException e) {
            printer.printLine(e.getMessage());
        }
    }
    private void printMagazines() {
        printer.printMagazines(liblary.getSortedPublications((p1,p2) ->p1.getTitle().compareToIgnoreCase(p2.getTitle())));
    }
    private void printUsers() {
        printer.printUsers(liblary.getSortedUsers((p1, p2) -> p1.getLastName().compareToIgnoreCase(p2.getLastName())));
    }


    private void deleteMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            if (liblary.removePublication(magazine))
                printer.printLine("Usunięto magazyn.");
            else
                printer.printLine("Brak wskazanego magazynu.");
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utworzyć magazynu, niepoprawne dane");
        }
    }

    private void deleteBook() {
        try {
            Book book = dataReader.readAndCreateBook();
            if (liblary.removePublication(book))
                printer.printLine("Usunięto książkę.");
            else
                printer.printLine("Brak wskazanej książki.");
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utworzyć książki, niepoprawne dane");
        }
    }
    private void exit() {
        try {
            fileManager.exportData(liblary);
            printer.printLine("Export danych do pliku zakończony powodzeniem");
        } catch (DataExportException e) {
            printer.printLine(e.getMessage());
        }
        dataReader.close();
        printer.printLine("Koniec programu, papa!");
    }
    private enum Option {
        EXIT(0, "Wyjście z programu"),
        ADD_BOOK(1, "Dodanie książki"),
        ADD_MAGAZINE(2,"Dodanie magazynu/gazety"),
        PRINT_BOOKS(3, "Wyświetlenie dostępnych książek"),
        PRINT_MAGAZINES(4, "WYświetlenie dostępnych magazynów/gazet"),

        DELETE_BOOK(5, "Usuń książkę"),

        DELETE_MAGAZINE(6, "Usuń magazyn"),

        ADD_USER(7, "Dodaj czytelnika"), //dodano

        PRINT_USERS(8, "Wyświetl czytelników"), //dodano

        FIND_BOOK(9,"Znajdz ksiazke");
        private int value;
        private String description;

        Option(int value, String desc) {
            this.value = value;
            this.description = desc;
        }

        @Override
        public String toString() {
            return value + " - " + description;
        }

        static Option createFromInt(int option) throws NoSuchOptionException {
            try {
                return Option.values()[option];
            }catch (ArrayIndexOutOfBoundsException ex){
                throw new NoSuchOptionException("Brak opcji o id " + option);
            }
        }
    }
}
