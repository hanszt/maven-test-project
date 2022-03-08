package hzt.serialization;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

//q 46 test 3
class SerializationTests {

//    While instantiating a new Moo, all the constructors will be executed.
//    So it will print In Boo k = 5 In BooBoo k = 5 In Moo There will not be any problem while serializing the Moo object.
//    However, while deserializing, the JVM will not find any no-arg constructor that can be invoked to initialize BooBoo.
//    No-args constructor is required in BooBoo because BooBoo is the most specific class in the class hierarchy
//    that doesn't implement Serializable. So it will throw an InvalidClassException.
    @Test
    void testDeserializingMooNotPossibleBecauseDefaultConstructorMissing() {
        Throwable throwable = Assertions.assertThrows(InvalidClassException.class,
                () -> serializeAndDeserialize(new Moo(), "moo1.ser"));
        assertEquals("hzt.serialization.SerializationTests$Moo; no valid constructor",
                throwable.getMessage());
    }


    // uncomment this when a default contructor is present
//    @Test
//    void testDeserializingMoo() throws IOException, ClassNotFoundException {
//        final var input = new Moo();
//        Object deserializeMoo = serializeAndDeserialize(input, "moo1.ser");
//
//        System.out.println("input = " + input);
//        System.out.println("deserializeMoo = " + deserializeMoo);
//
//        assertEquals(input.getClass(), deserializeMoo.getClass());
//    }


    private Object serializeAndDeserialize(Object object, String targetFileName) throws IOException, ClassNotFoundException {
        try (var objectOutputStream = new ObjectOutputStream(new FileOutputStream(targetFileName));
             var objectInputStream = new ObjectInputStream(new FileInputStream(targetFileName))) {
            objectOutputStream.writeObject(object);
            return objectInputStream.readObject();
        }
    }

    /**
     * Q 39 test 5
     * @see hzt.serialization.PortFolio
     * @see hzt.serialization.Bond
     */
    @Test
    void testCustomSerializationOfNonSerializableImplementingObjects() throws IOException, ClassNotFoundException {
        Bond[] bonds = {
                new Bond("1", 2, LocalDate.of(2020, Month.JANUARY, 3)),
                new Bond("2", 3, LocalDate.now()),
                new Bond("3", 56, LocalDate.of(1980, Month.FEBRUARY, 23))
        };

        final var portFolio = new PortFolio("My account", bonds);
        final var o = serializeAndDeserialize(portFolio, "custom.ser");

        if (o instanceof PortFolio) {
            final var dsPortFolio = (PortFolio) o;
            final var dsBonds = dsPortFolio.getBonds();
            Stream.of(dsBonds).forEach(System.out::println);

            assertArrayEquals(portFolio.getBonds(), dsBonds);
            assertEquals(portFolio.getAccountName(), dsPortFolio.getAccountName());
        } else {
            fail(o + " not of type " + PortFolio.class.getSimpleName());
        }
    }

    static class Boo {
        int boo = 10;

        public Boo(int k) {
            System.out.println("In Boo k = " + k);
            boo = k;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Boo boo1 = (Boo) o;
            return boo == boo1.boo;
        }

        @Override
        public int hashCode() {
            return Objects.hash(boo);
        }

        @Override
        public String toString() {
            return "Boo{" +
                    "boo=" + boo +
                    '}';
        }
    }

    static class BooBoo extends Boo {

//        public BooBoo() {
//            super(0);
//        }

        public BooBoo(int k) {
            super(k);
            System.out.println("In BooBoo k = " + k);
        }
    }

    static class Moo extends BooBoo implements Serializable {
        int moo = 10;

        public Moo() {
            super(5);
            System.out.println("In Moo");
        }
    }
}
