package hzt.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Function;

public class DataAccess {

    private final String dataBaseUrl;
    private final String user;
    private final String password;

    public DataAccess(String dataBaseUrl, String user, String password) {
        this.dataBaseUrl = dataBaseUrl;
        this.user = user;
        this.password = password;
    }

    public int executeUpdateQuery(String sql) {
        try (var statement = DriverManager
                .getConnection(dataBaseUrl, user, password)
                .createStatement()) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public <R> R sqlQueryExecutor(String sql, Function<PreparedStatement, R> statementFunction) {
        try (final var connection = DriverManager
                .getConnection(dataBaseUrl, user, password);
                var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            final var result = statementFunction.apply(preparedStatement);
            connection.commit();
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dataBaseUrl, user, password);
    }
}
