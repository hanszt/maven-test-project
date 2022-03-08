package hzt;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class BoundedWildCardTests {

    @Test
    void testBoundedWildCardList() {
        final List<Number> numbers = List.of(3, 4, 3, 3.0);
        final List<? super Number> numberSuperTypeList = getNumberSuperTypeList();
        numberSuperTypeList.addAll(numbers);
        numberSuperTypeList.add(BigDecimal.valueOf(3));
        numberSuperTypeList.add(3);

        assertFalse(numbers.isEmpty());
        assertFalse(getNumberSubTypeList().isEmpty());
    }

    public List<? extends Number> getNumberSubTypeList() {
//        return new ArrayList<Number>();// compileert
        final var integers = new ArrayList<Integer>();
        integers.add(3);
        return integers;// compileert
//        return new ArrayList<Double>(); // compileert
//        return new ArrayList<Object>();// compileert niet
    }

    public List<? super Number> getNumberSuperTypeList() {
//        return new ArrayList<Number>();// compileert
        return new ArrayList<Object>();// compileert
//        return new ArrayList<Double>(); // compileert niet
//        return new ArrayList<Integer>(); // compileert niet
    }
}
