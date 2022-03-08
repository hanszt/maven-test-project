package hzt.annotations;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationsTargetTest {

    @Test
//    @Disabled("This does not work for some reason")
    void testClassHasAnnotation() {
        final var annotationsTargetClass = AnnotationsTarget.class;
        assertNotNull(annotationsTargetClass.getAnnotation(MyArtifact.class));
        assertEquals(1, Arrays.stream(annotationsTargetClass.getAnnotations()).count());
    }

}
