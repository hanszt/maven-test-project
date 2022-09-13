package hzt.creational_patterns.singleton_pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings({"SqlNoDataSourceInspection", "squid:S2974"})
class JDBCSingleton {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCSingleton.class);
    //Step 1
    // create a JDBCSingleton class.
    //static member holds only one instance of the JDBCSingleton class.

    private static class InstanceHolder {

        private static final JDBCSingleton jdbc = new JDBCSingleton();
    }

    //JDBCSingleton prevents the instantiation from any other class.
    private JDBCSingleton() {
    }

    //Now we are providing global point of access.
    public static JDBCSingleton getInstance() {
        return InstanceHolder.jdbc;
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/ashwanirajput", "root", "ashwani");
    }

    public int insert(String name, String pass) {
        int recordCounter = 0;
        String sqlQuery = "insert into userdata(uname,upassword)values(?,?)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.setString(1, name);
            ps.setString(2, pass);
            recordCounter = ps.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Could not insert username and password...", e);
        }
        return recordCounter;
    }

    public void view(String name) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("select * from userdata where uname=?");
             ResultSet rs = ps.executeQuery()) {

            ps.setString(1, name);
            while (rs.next()) {
                final var userName = rs.getString(2);
                final var password = rs.getString(3);
                LOGGER.info("Name= {}\tPassword= {}", userName, password);
            }

        } catch (SQLException e) {
            LOGGER.error("Could not view name...", e);
        }
    }

    public int update(String name, String password) {
        int recordCounter = 0;
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(
                     " update userdata set upassword=? where uname=?")) {

            ps.setString(1, password);
            ps.setString(2, name);
            recordCounter = ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Could not update name and password...", e);
        }
        return recordCounter;
    }

    public int delete(int userid) {
        int recordCounter = 0;
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement("delete from userdata where uid=?")) {
            ps.setInt(1, userid);
            recordCounter = ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Could not delete user with id: " + userid, e);
        }
        return recordCounter;
    }
}
