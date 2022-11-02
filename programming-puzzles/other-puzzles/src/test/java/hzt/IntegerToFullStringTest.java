package hzt;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class IntegerToFullStringTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "2 -> two",
            "9 -> nine",
            "29 -> twenty nine",
            "84 -> eighty four",
            "30 -> thirty",
            "60 -> sixty",
            "100 -> hundred"})
    void testConvertIntToFullName(final String string) {
        final var split = string.split(" -> ");
        final var input = Integer.parseInt(split[0]);
        final var expected = split[1];

        final var actual = IntegerToFullString.convertToFullString(input);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2 -> three",
            "9 -> eight",
            "29 -> twenty one",
            "84 -> sixty four",
            "30 -> thirty one",
            "60 -> sixty four",
            "100 -> hundred two"})
    void testConvertIntToFullNameWrong(final String string) {
        final var split = string.split(" -> ");
        final var input = Integer.parseInt(split[0]);
        final var expected = split[1];

        final var actual = IntegerToFullString.convertToFullString(input);

        assertNotEquals(expected, actual);
    }

}
