package hzt.creational_patterns.singleton_pattern;

import java.sql.*;

class JDBCSingleton {
    //Step 1
    // create a JDBCSingleton class.
    //static member holds only one instance of the JDBCSingleton class.

    private static JDBCSingleton jdbc;

    //JDBCSingleton prevents the instantiation from any other class.
    private JDBCSingleton() {
    }

    //Now we are providing global point of access.
    public static JDBCSingleton getInstance() {
        if (jdbc == null) {
            jdbc = new JDBCSingleton();
        }
        return jdbc;
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
            e.printStackTrace();
        }
        return recordCounter;
    }

    public void view(String name) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("select * from userdata where uname=?");
             ResultSet rs = ps.executeQuery()) {

            ps.setString(1, name);
            while (rs.next()) {
                System.out.println("Name= " + rs.getString(2) + "\t" + "Password= " + rs.getString(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return recordCounter;
    }
}
