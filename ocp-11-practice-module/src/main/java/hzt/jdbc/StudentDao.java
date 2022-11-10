package hzt.jdbc;

import hzt.model.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @see <a href="https://www.tutorialspoint.com/jdbc/index.htm">JDBC Tutorial</a>
 */
@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class StudentDao {

    private final DataAccess dataAccess;

    public StudentDao(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public int createStudentTable() {
        String sql = "CREATE TABLE Student " +
                "(id BIGINT not NULL, " +
                " name VARCHAR(255), " +
                " average_grade FLOAT, " +
                " PRIMARY KEY ( id ))";
        return dataAccess.executeUpdateQuery(sql);
    }

    public List<Integer> insertStudents(List<Student> students) {
        final var sql = "insert into Student(id, name, average_grade) values (?,?,?)";
        return dataAccess.sqlQueryExecutor(sql, students, StudentDao::insertStudents);
    }

    private static List<Integer> insertStudents(List<Student> students, PreparedStatement ps) {
        return students.stream()
                .map(student -> insertStudent(student, ps))
                .collect(Collectors.toList());
    }

    public int insertStudent(Student student) {
        final var sql = "insert into Student(id, name, average_grade) values (?,?,?)";
        return dataAccess.sqlQueryExecutor(sql, student, StudentDao::insertStudent);
    }

    private static int insertStudent(Student student, PreparedStatement ps) {
        try {
            ps.setLong(1, student.getId());
            ps.setString(2, student.getName());
            ps.setDouble(3, student.getAverageGrade());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    // Source: Enthuware Java11OCP Test 1 Q 27
    public List<Student> getAllStudents() {
        try (var statement = dataAccess.getConnection().createStatement()) {

            //noinspection SqlDialectInspection,SqlNoDataSourceInspection
            final var sql = "select * from Student";
            var resultSet = statement.executeQuery(sql);
            return getStudents(resultSet);

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    //the column index starts at 1 instead of 0
    private static List<Student> getStudents(ResultSet resultSet) throws SQLException {
        List<Student> studentList = new ArrayList<>();
        while (resultSet.next()) {
            final var id = resultSet.getLong(1);
            final var name = resultSet.getString(2);
            final var averageGrade = resultSet.getDouble(3);
            studentList.add(new Student(id, name, averageGrade));
        }
        return studentList;
    }

    public boolean isStudentTabelPresent() {
        try (var connection = dataAccess.getConnection()) {
            return connection.getMetaData()
                    .getTables(null, null, "STUDENT", new String[] {"TABLE"})
                    .next();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
