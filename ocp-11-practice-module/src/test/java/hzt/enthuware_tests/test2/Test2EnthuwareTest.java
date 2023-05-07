package hzt.enthuware_tests.test2;

import hzt.OsAssumptions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Test2EnthuwareTest {

    // Source: Enthuware Java11OCP Test 2 Q 2
    @Test
    void stringInternEqualsTest() {
        String examName = "OCP Java 11";
        String uniqueExamName = new String(examName);
        String internedExamName = uniqueExamName.intern();

        assertAll(
                () -> assertSame(examName, internedExamName),
                () -> assertNotSame(examName, uniqueExamName),
                () -> assertNotSame(uniqueExamName, internedExamName)
        );
    }

    // Source: Enthuware Java11OCP Test 2 Q 7
//    Since the first argument is a proper prefix of the second argument, compare(a, c)
//    will return a negative number and the number will be the same as the difference in the number of elements in both the arrays
//    i.e -6 = -1.  Since b and c have a common prefix and b is smaller than c, compare(b, c) will return -1.
//
//    (Observe that it does not return the amount of the difference i.e. 3-6 = -3)

    @Test
    void testComparingArrays() {
        var a = new int[]{1, 2, 3, 4, 5};
        var b = new int[]{1, 2, 3, 4, 5, 3};
        var c = new int[]{1, 2, 3, 4, 5, 6};
        int x = Arrays.compare(a, c);
        int y = Arrays.compare(b, c);
        System.out.println(x + " " + y);

        assertEquals(-1, x);
        assertEquals(-1, y);
    }

    //    BTW, both mismatch(a, c) and mismatch(b, c) will return 5 because the arrays differ at index 5.
    @Test
    void testMismatchMethodArrays() {
        var a = new int[]{1, 2, 3, 4, 5};
        var b = new int[]{1, 2, 3, 4, 5, 3};
        var c = new int[]{1, 2, 3, 4, 5, 6};
        int x = Arrays.mismatch(a, c);
        int y = Arrays.mismatch(b, c);
        System.out.println(x + " " + y);

        assertEquals(5, x);
        assertEquals(5, y);
    }

    @Test
    void testSwitchWithoutBreakAddsAllCasesFromMatchingCaseUntilBreak() {
        List<String> strings = new ArrayList<>();
        int l = 3;
        switch (l) {
            case 1:
                strings.add("1");
            case 3:
                strings.add("3");
            case 4:
                strings.add("4");
            default:
                strings.add("default");
                break;
            case 5:
                strings.add("5");
        }
        strings.forEach(System.out::println);

        assertEquals(3, strings.size());
    }

    //q 14 test 2
    @Test
    void testModuloOnFloatingPointNumbers() {
        float two = 2;    //1
        float three = 3;
        float four = 4;

        float mod1 = two % four;   //2
        System.out.println("mod1 = " + mod1);

        float mod2 = four % two;
        System.out.println("mod2 = " + mod2);

        float val = mod1 > mod2 ? three : four; //3
        System.out.println(val);
        assertEquals(3.0, val);
    }

    @Test
    void testModulo() {
        int two = 2;    //1
        int three = 3;
        int four = 4;

        int mod1 = two % four;   //2
        System.out.println("mod1 = " + mod1);

        int mod2 = four % two;
        System.out.println("mod2 = " + mod2);

        int val = mod1 > mod2 ? three : four; //3
        System.out.println(val);

        assertEquals(3, val);
    }

    // test 2 Q 33
//    Remember the following points about Path.subpath(int beginIndex, int endIndex)
//    1. Indexing starts from 0.
//    2. Root (i.e. c:\) is not considered as the beginning.
//    3. name at beginIndex is included but name at endIndex is not.
//    4. paths do not start or end with \.
    @Test
    void testSubPath() {
        OsAssumptions.assumeIsWindowsOs();

        Path p1 = Paths.get("C:\\Temp\\pathtest\\someFolder\\otherFolder\\test.txt");
        final var subPath = p1.subpath(0, 2);
        assertEquals(Path.of("Temp\\pathtest"), subPath);
    }

    // Q42 test 2
    @Test
    void crazyLoop() {
        var c = 0;
        JACK:
        while (c < 8) {
            JILL:
            System.out.println(c);
            TRALALA:
            if (c > 3) break JACK;
            else c++;
        }
        assertEquals(4, c);
    }

//    Q 46 test 2
//All that if() needs is a boolean, now b1 == false returns true,
// which is a boolean and since b2 = true is an expression and every expression has a return value
// (which is the Left Hand Side of the expression), it returns true, which is again a boolean.  FYI:
// the return value of expression i = 10; is 10 (an int).
    @Test
    void testAnExpressionInAnIf() {
        var b1 = false;
        var b2 = false;
        if (b2 = b1 == false) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
        assertTrue(b2);
    }
}
