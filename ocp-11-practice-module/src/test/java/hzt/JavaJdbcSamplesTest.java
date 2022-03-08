package hzt;

import hzt.jdbc.DataAccess;
import hzt.jdbc.StudentDao;
import hzt.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class JavaJdbcSamplesTest {

    private final StudentDao studentDao = new StudentDao(
            new DataAccess("jdbc:h2:mem:/students", "app", "app"));

    @BeforeEach
    void setup() {
        final var studentTabelPresent = studentDao.isStudentTabelPresent();
        System.out.println("studentTabelPresent = " + studentTabelPresent);
        if (!studentTabelPresent) {
            System.out.println("studentDao.createStudentTable() = " + studentDao.createStudentTable());
            final var status = studentDao.insertStudent(new Student(3, "Hans", 8.5));
            System.out.println("status = " + status);
        }
    }

    @Test
    void testGetAllStudents() {
        final var statusList = studentDao.insertStudents(List.of(
                new Student(2, "Sophie", 9.0),
                new Student(1, "Dominic", 7.5)));

        System.out.println("statusList = " + statusList);
        final var allStudents = studentDao.getAllStudents();

        System.out.println(allStudents);

        assertEquals(3, allStudents.size());
    }

    @Test
    void testGetAllStudentsEmptyAfterFirstTestCompletes() {
        final var allStudents = studentDao.getAllStudents();

        System.out.println(allStudents);

        assertFalse(allStudents.isEmpty());
    }

}
