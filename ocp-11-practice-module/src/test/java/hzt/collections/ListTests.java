package hzt.collections;

import hzt.model.Student;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ListTests {

    @Test
    void testViewModFromSubListChangesOriginalList() {
        List<String> vowels = new ArrayList<>();
        vowels.add("a");
        vowels.add("e");
        vowels.add("i");
        vowels.add("o");
        vowels.add("u");
        Function<List<String>, List<String>> function = list -> list.subList(2, 4);

        List<String> view = function.apply(vowels);//get a view backed by the original list
        view.add("x");//modify the view
        vowels.forEach(System.out::print); //updates visible in original list

        assertEquals(List.of("a", "e", "i", "o", "x", "u"), vowels);
    }

    //q24 test 4
    @Test
    void testListWithVarAndNoTypeSpecificationCreatesObjectList() {
        var allStudents = new ArrayList<>();
        var student = new Student(2, "Hans", 7);
        allStudents.add(student);
        Student hans = (Student) allStudents.get(0);
        assertSame(student, hans);
    }

//    All modification operations of a CopyOnWriteArrayList are considered atomic. So the thread that calls size()
//    will either see no data in the list or will see all the elements added to the list.

    //    A thread-safe variant of ArrayList in which all mutative operations (add, set, and so on)
//    are implemented by making a fresh copy of the underlying array.
//    This is ordinarily too costly, but may be more efficient than alternatives when traversal
//    operations vastly outnumber mutations, and is useful when you cannot or don't want to synchronize traversals,
//    yet need to preclude interference among concurrent threads.
//
//    The "snapshot" style iterator method uses a reference to the state of the array at the point that the iterator was created.
//    This array never changes during the lifetime of the iterator,
//    so interference is impossible and the iterator is guaranteed not to throw ConcurrentModificationException.
//    The iterator will not reflect additions, removals, or changes to the list since the iterator was created.
//    Element-changing operations on iterators themselves (remove, set, and add) are not supported.
//    These methods throw UnsupportedOperationException
    @Test
    void testCopyOnWriteArrayList() {
        CopyOnWriteArrayList<String> cal = new CopyOnWriteArrayList<>(List.of("Dit", "is", "een", "multithreading", "test"));
        Thread thread2 = new Thread(() -> {
            final var size = cal.size();
            assertEquals(6, size);
        });
        Thread thread1 = new Thread(() -> cal.add("Added"));
        thread1.start();
        thread2.start();
    }

    @Test
    @Disabled("Takes to long")
    void testListUnboundedAddThrowsOutOfMemoryError() {
        assertThrows(OutOfMemoryError.class, this::fillListUntilOutOfMemory);
    }

    private void fillListUntilOutOfMemory() {
        List<String> s1 = new ArrayList<>();
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                s1.add("sdfa");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            System.out.println(s1.size());
        }
    }

    // q4 test 5
    @Test
    void testListCopyOf() {
        Collection<Integer> collection = List.of(40, 30, 20);  //
        List<Integer> bList = List.copyOf(collection); //2

        //noinspection ConstantConditions
        assertThrows(UnsupportedOperationException.class, () -> bList.add(2));
        assertEquals(collection, bList);
    }

    @Test
    void testListCopyOfIsMutable() {
        Collection<Number> col = new HashSet<>();
        col.add(1);
        var list1 = List.of(col); //1
        col.add(2); //2
        var list2 = List.copyOf(col); //3

        assertEquals(List.of(Set.of(1, 2)), list1);
        assertEquals(List.of(1, 2), list2);
    }

    @Test
    void testListPredefinedCapacityThrowsIndexOutOfBound() {
        List<Integer> integers = new ArrayList<>(8);
        //noinspection ResultOfMethodCallIgnored
        assertThrows(IndexOutOfBoundsException.class, () -> integers.get(7));
    }

    @Test
    void testListReplaceAll() {
        List<String> names = new ArrayList<>(List.of("Sophie", "Rashied", "Piet", "Dominic"));

        names.replaceAll(name -> name.toUpperCase(Locale.ROOT));

        assertEquals(List.of("SOPHIE", "RASHIED", "PIET", "DOMINIC"), names);
    }

}
