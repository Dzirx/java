package pl.javastart.library.io;

import pl.javastart.library.model.*;

import java.util.Collection;

public class ConsolePrinter {
    public void printBooks(Collection<Publication> publications) {
        long counter = publications.stream()
                .filter(p->p instanceof Book)
                .map(Publication::toString)
                .peek(this::printLine)
                .count();
        if (counter == 0)
            printLine("Brak książek w bibliotece");
    }
    //gorna na stream dolna na normalnej petli wywolana
    public void printMagazines(Collection<Publication> publications) {
        int counter = 0;
        for (Publication publication : publications) {
            if(publication instanceof Magazine) {
                printLine(publication.toString());
                counter++;
            }
        }
        if (counter == 0)
            printLine("Brak magazynów w bibliotece");
    }
    public void printUsers(Collection<LibraryUser> users) {
     //   for (LibraryUser user : users) {
     //       printLine(user.toString());
     //   }to samo inaczej zapisane
        users.stream()
                .map(User::toString)
                .forEach(this::printLine);
    }

    public void printLine(String text) {
        System.out.println(text);
    }
}
