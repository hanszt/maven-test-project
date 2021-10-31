package com.dnb.custom_annotations;

import com.dnb.model.Person;
import org.hzt.model.Book;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ObjectToJsonConverterTest {

    @Test
    void testGivenObjectSerializedThenReturnExpectedJson() throws JsonSerializationException {
        final String EXPECTED = """
                {"firstName":"Soufiane","lastName":"Cheouati","birthDate":"1986-10-02"}
                """.strip();
        Person person = new Person("soufiane", "cheouati", LocalDate.of(1986, 10, 2));

        ObjectToJsonConverter serializer = new ObjectToJsonConverter();
        String jsonString = serializer.convertToJson(person);

        assertEquals(EXPECTED, jsonString);
    }

    @Test
    void testGivenObjectNotJsonSerializableThenThrow() {
        Book book = new Book( "test");
        ObjectToJsonConverter serializer = new ObjectToJsonConverter();
        assertThrows(JsonSerializationException.class, () -> serializer.convertToJson(book));
    }
}
