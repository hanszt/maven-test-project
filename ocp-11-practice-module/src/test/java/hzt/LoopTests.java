package hzt;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

class LoopTests {

    @Test
    void testEnhancedForLoopOf3DArray() {
        //noinspection CStyleArrayDeclaration
        int[][] ia[] = new int[3][4][];
        for (int[] array : ia[0]) {
            assertNull(array);
        }
    }

    @Test
    void testEnhancedForLoopOfCollectionWithVarInLoop() {
        Object o = null;
        //noinspection rawtypes
        Collection c = new ArrayList();
//        for(o : c) { not valid
        for (final var var : c) {
            fail();
        }

    }

    @Test
    @SuppressWarnings("all")
    void testCrazyLoops() {
        var i = 0;
        for (i = 1; i < 5; i++) continue;  //(1)
        for (i = 0; ; i++) break;       //(2)
        for (; i < 5 ? false : true; ) ;     //(3)
        assertEquals(0, i);
    }

}
