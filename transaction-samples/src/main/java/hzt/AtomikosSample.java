package hzt;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jdbc.AtomikosNonXADataSourceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @see <a href="https://www.baeldung.com/java-atomikos">A Guide to Atomikos</a> for the tutorial where this code comes from
 */
public class AtomikosSample {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtomikosSample.class);

    public static void main(String... args) {
        try {
            new AtomikosSample().placeOrder("3", 12);
        } catch (HeuristicRollbackException | SystemException | HeuristicMixedException | RollbackException e) {
            LOGGER.error("Something went wrong while placing the order", e);
        }
    }

    @SuppressWarnings({"squid:S1160", "SqlNoDataSourceInspection"})
    public void placeOrder(String productId, int amount) throws HeuristicRollbackException, SystemException,
            HeuristicMixedException, RollbackException {
        var userTransaction = new UserTransactionImp();
        var inventoryDataSource = new AtomikosDataSourceBean();
        var orderDataSource = new AtomikosNonXADataSourceBean();

        var orderId = UUID.randomUUID().toString();

        try (var inventoryConnection = inventoryDataSource.getConnection();
             var orderConnection = orderDataSource.getConnection();
             var statement1 = inventoryConnection.createStatement();
             var statement2 = orderConnection.createStatement()) {

            userTransaction.begin();

            var query1 = "update Inventory set balance = balance - " + amount + " where productId ='" +
                    productId + "'";
            statement1.executeUpdate(query1);
            var query2 = "insert into Orders values ( '" + orderId + "', '" + productId + "', " + amount + " )";
            statement2.executeUpdate(query2);
            userTransaction.commit();
        } catch (NotSupportedException | SQLException e) {
            LOGGER.error("NotSupportedException or SQLException", e);
            userTransaction.rollback();
        }
    }
}
