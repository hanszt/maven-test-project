package org.hzt;

import org.hzt.model.Person;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.regex.Pattern;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterResolverTests {

    @Nested
    @ExtendWith(ParameterMonthResolverTest.MonthParameterResolver.class)
    class ParameterMonthResolverTest {

        @Test
        void testWithParameter(Month month) {
            assertEquals(Month.MAY, month);
        }

        /**
         * @see <a href="https://www.baeldung.com/junit-5-parameters">Inject Parameters into JUnit Jupiter Unit Tests</a>
         */
        static class MonthParameterResolver implements ParameterResolver {

            @Override
            public boolean supportsParameter(ParameterContext parameterContext,
                                             ExtensionContext extensionContext) throws ParameterResolutionException {
                return parameterContext.getParameter().getType() == Month.class;
            }

            @Override
            public Object resolveParameter(ParameterContext parameterContext,
                                           ExtensionContext extensionContext) throws ParameterResolutionException {
                return Month.MAY;
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = "MAY")
    void testParameterizedTest(Month month) {
        assertEquals(Month.MAY, month);
    }

    static class PersonAggregator implements ArgumentsAggregator {

        @Override
        public Person aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            return new Person(
                    accessor.getString(1),
                    accessor.getString(2),
                    LocalDate.parse(accessor.getString(3)));
        }
    }

    static class ToIntArrayConverter implements ArgumentConverter {

        private static final Pattern whiteCharPattern = Pattern.compile(" ");

        @Override
        public int[] convert(Object source, ParameterContext context) throws ArgumentConversionException {
            return whiteCharPattern.splitAsStream(source.toString())
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }
    }

    @ParameterizedTest
    @CsvSource({
            "Isaac Newton, Isaac, Newton, 1643-01-04",
            "Charles Robert Darwin, Charles, Robert Darwin, 1809-12-02"})
    void fullName_ShouldGenerateTheExpectedFullName(String expectedFullName, @AggregateWith(PersonAggregator.class) Person person) {
        println(person.getDateOfBirth());
        assertEquals(expectedFullName, person.fullName());
    }

    @ParameterizedTest
    @CsvSource({
            "1 2 3 4 3 5 6 5 4 7 3, 11",
            "1 2 3 2 34 35 23 1212, 8"})
    void testArrayFromString(@ConvertWith(ToIntArrayConverter.class) int[] array, int size) {
        println(Arrays.toString(array));
        assertEquals(array.length, size);
    }
}
