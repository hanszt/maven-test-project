package hzt.stream;

import hzt.stream.model.Book;
import hzt.stream.model.Painter;
import hzt.stream.model.Painting;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

public final class TestSampleGenerator {

    private TestSampleGenerator() {
    }

    public static List<Book> createBookList() {
        return List.of(new Book("Harry Potter", "Fiction"),
                new Book("Lord of the Rings", "Fiction"),
                new Book("Pragmatic Programmer", "Programming"),
                new Book("OCP 11 Volume 1", "Programming"),
                new Book("Homo Deus", "Educational"),
                new Book("The da Vinci Code", "Fiction"),
                new Book("The da Vinci Code", "Fiction"));
    }

    public static List<Painting> getPaintingList() {
        final Painter picasso = new Painter("Pablo", "Picasso", LocalDate.of(1881, 10, 25));
        picasso.setDateOfDeath(LocalDate.of(1973, 4, 9));
        final Painter vermeer = new Painter("Johannes", "Vermeer", LocalDate.of(1632, 10, 31));
        vermeer.setDateOfDeath(LocalDate.of(1675, 12, 1));
        final Painter vanGogh = new Painter("Vincent", "van Gogh", LocalDate.of(1853, 3,20));
        vanGogh.setDateOfDeath(LocalDate.of(1890, 7, 29));
        return List.of(
                new Painting("Guernica", picasso, Year.of(1937), true),
                new Painting("Les Demoiselles d'Avignon", picasso, Year.of(1907), true),
                new Painting("Le Rêve", picasso, Year.of(1932), true),
                new Painting("Meisje met de parel", vermeer, Year.of(1665), true),
                new Painting("Het melkmeisje", vermeer, Year.of(1658), true),
                new Painting("Meisje met de rode hoed", vermeer, Year.of(1665), true),
                new Painting("Lentetuin, de pastorietuin te Nuenen in het voorjaar", vanGogh, Year.of(1884), false),
                new Painting("De sterrennacht", vanGogh, Year.of(1889), true)
        );
    }

}
