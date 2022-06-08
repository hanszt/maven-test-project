package hzt;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static hzt.LocaleSample.APPMESSAGES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocaleSampleTest {

    @Test
    void testLocaleDeBe() {
        final var match = Arrays.stream(Locale.getAvailableLocales())
                .map(Locale::toString)
                .anyMatch("de_BE"::equals);

        assertTrue(match);

        final var germanBelgiumLocale = new Locale("de_BE");
        final var country = germanBelgiumLocale.toString();

        assertEquals("de_be", country);
    }

    @Test
    void testAvailableLocales() {
        final var availableLocales = Locale.getAvailableLocales();

        Arrays.stream(availableLocales).forEach(System.out::println);

        assertEquals(1017, availableLocales.length);
    }

    @Test
    void testIterableFromResourceBundleKeyEnumeration() {
        final var bundle = ResourceBundle.getBundle(APPMESSAGES);
        final Iterable<String> keyIterable = () -> bundle.getKeys().asIterator();

        final var strings = StreamSupport
                .stream(keyIterable.spliterator(), false)
                .collect(Collectors.toUnmodifiableList());

        assertEquals(4, strings.size());
    }

}
