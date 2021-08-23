package com.dnb.custom_annotations;

import com.dnb.model.Book;
import com.dnb.model.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectToJsonConverterTest {

    @Test
    void givenObjectSerializedThenReturnExpectedJson() throws JsonSerializationException {
        Person person = new Person("soufiane", "cheouati", "34");
        ObjectToJsonConverter serializer = new ObjectToJsonConverter();
        String jsonString = serializer.convertToJson(person);
        assertEquals("{\"firstName\":\"Soufiane\",\"lastName\":\"Cheouati\",\"personAge\":\"34\"}", jsonString);
    }

    @Test
    void givenObjectNotJsonSerializableThenThrow() {
        Book book = new Book( "test");
        ObjectToJsonConverter serializer = new ObjectToJsonConverter();
        assertThrows(JsonSerializationException.class, () -> serializer.convertToJson(book));
    }
}