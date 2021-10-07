package org.hzt;

import org.hzt.model.BankAccount;
import org.hzt.model.Book;
import org.hzt.model.Customer;
import org.hzt.model.Museum;
import org.hzt.model.Painter;
import org.hzt.model.Painting;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class TestSampleGenerator {

    private static final String FICTION = "Fiction";

    private TestSampleGenerator() {
    }

    public static List<Book> createBookList() {
        return List.of(new Book("Harry Potter", FICTION),
                new Book("Lord of the Rings", FICTION),
                new Book("Pragmatic Programmer", "Programming"),
                new Book("OCP 11 Volume 1", "Programming"),
                new Book("Homo Deus", "Educational"),
                new Book("The da Vinci Code", FICTION),
                new Book("The da Vinci Code", FICTION));
    }

    public static List<Painting> createPaintingList() {
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

    public static List<Museum> createMuseumList() {

        final var groupedByLastName = createPaintingList().stream()
                .collect(Collectors.groupingBy(painting -> painting.painter().getLastname()));

        final List<Painting> vanGoghPaintings = groupedByLastName.get("van Gogh");
        final List<Painting> vermeerPaintings = groupedByLastName.get("Vermeer");
        final List<Painting> picassoPaintings = groupedByLastName.get("Picasso");

        return List.of(
                new Museum("Van Gogh Museum", LocalDate.of(1992, Month.APRIL, 2), vanGoghPaintings),
                new Museum("Vermeer Museum", LocalDate.of(1940, Month.JANUARY, 23), vermeerPaintings),
                new Museum("Picasso Museum", LocalDate.of(1965, Month.AUGUST, 4), picassoPaintings));
    }

    public static List<BankAccount> createSampleBankAccountList() {
        return List.of(
                new BankAccount("NL43INGB0008541648", new Customer("1", "Zuidervaart"), BigDecimal.valueOf(-4323)),
                new BankAccount("BL54ABNA0004536472", new Customer("2", "Jansen"), BigDecimal.valueOf(234235.34)),
                new BankAccount("NL32BUNQ0004358592", new Customer("3", "Vullings"), BigDecimal.valueOf(2342)),
                new BankAccount("NL32INGB0004524542", new Customer("8", "Burgmeijer"), BigDecimal.valueOf(23)),
                new BankAccount("NL65RABO00004342356", new Customer("22", "Claassen"), BigDecimal.valueOf(234))
        );
    }

    public static List<BankAccount> createSampleBankAccountListContainingNulls() {
        final List<BankAccount> bankAccounts = new ArrayList<>(createSampleBankAccountList());
        bankAccounts.add(new BankAccount("", null, null));
        bankAccounts.add(new BankAccount("Test", new Customer("", "", Collections.emptyList()), null));
        return bankAccounts;
    }

}
