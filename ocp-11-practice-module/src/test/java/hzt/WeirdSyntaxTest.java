package hzt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeirdSyntaxTest {

    @Test
    void testIncrementChars() {
        int counter ='A';
        for (char character = 'A'; character <= 'z'; character++) {
            System.out.println("character = " + character);
            final var nr = (int) character;
            System.out.println("asciNr = " + nr);
            final var castedToChar = (char) nr;
            System.out.println("casted back to char: " + castedToChar);
            counter++;
        }
        assertEquals('z', counter - 1);
    }

}
