package hzt.annotations;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationsTargetTest {

    @Test
    void testClassHasAnnotation() {
        final var annotationsTargetClass = AnnotationsTarget.class;
        assertNotNull(annotationsTargetClass.getAnnotation(MyArtifact.class));
        assertEquals(1, Arrays.stream(annotationsTargetClass.getAnnotations()).count());
    }

}
