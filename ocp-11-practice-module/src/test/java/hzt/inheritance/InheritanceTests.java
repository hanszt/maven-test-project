package hzt.inheritance;

import hzt.model.Person;
import hzt.model.Student;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class InheritanceTests {

    // Source: Enthuware Java11OCP Test 1 Q 37
    @Test
    void testChildConstructorMayThrowMoreExceptionsThenParentConstructor() throws NoSuchMethodException {
        final var childExceptionTypes = CleanConnector.class.getConstructor(int.class).getExceptionTypes();
        final var parentExceptionTypes = PortConnector.class.getConstructor(int.class).getExceptionTypes();

        assertTrue(childExceptionTypes.length > parentExceptionTypes.length);
    }

    // Source: Enthuware Java11OCP Test 1 Q 37
    @Test
    void testChildMethodMayThrowLessExceptionsThenParentMethod() throws Exception {
        ThrowingFunction<Class<?>, Method> toConnectMethod = c -> c.getMethod("connect", String.class);

        final var childExceptionTypes = toConnectMethod.apply(CleanConnector.class).getExceptionTypes();
        final var parentExceptionTypes = toConnectMethod.apply(PortConnector.class).getExceptionTypes();

        System.out.println("childExceptionTypes.length = " + childExceptionTypes.length);
        assertTrue(childExceptionTypes.length < parentExceptionTypes.length);
    }

    @Test
    void testSuperCallingMethodTakesDirectParentMethod() {
        final var c = new C();
        assertEquals("C", c.classNameAsString());
        assertEquals("B", c.classNameViaSuperCall());
    }

    interface ThrowingFunction<T, R> {
        R apply(T t) throws Exception;
    }

    @Test
    void testComparingObjectsUsingIsOperator() {
        var student = new Student(1, "Test", 4);
        var person = new Person(2, "Test");
        var localDate = java.time.LocalDate.of(2021, Month.NOVEMBER, 24);
        if (student == person) {
            fail();
        }
//        if (localDate == student) { not allowed because they don't extend each other
    }

}
