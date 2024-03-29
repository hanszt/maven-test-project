package hzt.only_jdk.records;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecordTest {

    @Test
    void testImmutableRecordThrowsUnSupportedOperationException() {
        final var hans = new ImmutableRecord("Hans", new ArrayList<>(List.of("Developer", "Maintainer")));
        final var roles = hans.roles();
        assertThrows(UnsupportedOperationException.class, () -> roles.remove("Maintainer"));
        System.out.println(roles);
        assertEquals(2, roles.size());
    }

    @Test
    void testMutableRecordRoleListCanBeAltered() {
        final var hans = new MutableRecord("Hans", new ArrayList<>(List.of("Developer", "Maintainer")));
        final var roles = hans.roles();
        assertDoesNotThrow(() -> roles.remove("Maintainer"));
        System.out.println(roles);
        assertEquals(1, roles.size());
    }
}
