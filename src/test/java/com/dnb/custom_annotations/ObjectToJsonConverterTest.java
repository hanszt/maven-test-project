package com.dnb.custom_annotations;

import com.dnb.model.Book;
import com.dnb.model.Person;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ObjectToJsonConverterTest {

    @Test
    void testGivenObjectSerializedThenReturnExpectedJson() throws JsonSerializationException {
        Person person = new Person("soufiane", "cheouati", LocalDate.of(1986, 10, 2));
        ObjectToJsonConverter serializer = new ObjectToJsonConverter();
        String jsonString = serializer.convertToJson(person);
        assertEquals("{\"firstName\":\"Soufiane\",\"lastName\":\"Cheouati\",\"birthDate\":\"1986-10-02\"}", jsonString);
    }

    @Test
    void testGivenObjectNotJsonSerializableThenThrow() {
        Book book = new Book( "test");
        ObjectToJsonConverter serializer = new ObjectToJsonConverter();
        assertThrows(JsonSerializationException.class, () -> serializer.convertToJson(book));
    }
}
