package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import domain.Account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountDAOImpl implements AccountDAO {

    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS accounts (
                id BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 1000 INCREMENT BY 1),
                pin VARCHAR(4) NOT NULL,
                balance NUMERIC(12,2) NOT NULL DEFAULT 0,
                PRIMARY KEY (id)
            )
            """;
    private static final String INSERT_SQL = "INSERT INTO accounts (pin, balance) VALUES (?, ?) RETURNING id";
    private static final String FIND_BY_ID_SQL = "SELECT id, pin, balance FROM accounts WHERE id = ?";
    private static final String UPDATE_BALANCE_SQL = "UPDATE accounts SET balance = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM accounts WHERE id = ?";
    private final Connection conn;
    private static Logger logger;

    public AccountDAOImpl(Connection conn) {
        this.conn = conn;
        logger = LoggerFactory.getLogger(AccountDAOImpl.class);
        initializeSchema();
    }

    @Override
    public Account addAccount(Account account) {
        try (PreparedStatement statement = conn.prepareStatement(INSERT_SQL)) {
            statement.setString(1, account.getPin());
            statement.setBigDecimal(2, account.getBalance());

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                long generatedId = resultSet.getLong("id");
                logger.info("New account {} created", generatedId);
                return new Account(generatedId, account.getPin(), account.getBalance());
            }
        } catch (SQLException e) {
        	logger.error("Could not create new account", e);
            throw databaseError("Could not add account", e);
        }
    }

    @Override
    public Account getAccountById(long id) {
        try (PreparedStatement statement = conn.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapAccount(resultSet);
                }
            }
            return null;
        } catch (SQLException e) {
            throw databaseError("Could not find account", e);
        }
    }

    @Override
    public void updateBalance(Account account) {
        try (PreparedStatement statement = conn.prepareStatement(UPDATE_BALANCE_SQL)) {
            statement.setBigDecimal(1, account.getBalance());
            statement.setLong(2, account.getId());
            statement.executeUpdate();
            logger.info("Account {} balance updated", account.getId());
        } catch (SQLException e) {
        	logger.error("Could not update balance for account {}", account.getId(), e);
            throw databaseError("Could not update balance", e);
        }
    }

    @Override
    public void deleteAccount(long id) {
        try (PreparedStatement statement = conn.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            logger.info("Account {} successfully deleted", id);
        } catch (SQLException e) {
        	logger.error("Could not delete account {}", id, e);
            throw databaseError("Could not delete account", e);
        }
    }

    private void initializeSchema() {
        try (PreparedStatement statement = conn.prepareStatement(CREATE_TABLE_SQL)) {
            statement.executeUpdate();
            logger.info("Database schema initialized");
        } catch (SQLException e) {
        	logger.error("Could not initialize database schema");
            throw databaseError("Could not initialize database schema", e);
        }
    }

    private Account mapAccount(ResultSet resultSet) throws SQLException {
        return new Account(
                resultSet.getLong("id"),
                resultSet.getString("pin"),
                resultSet.getBigDecimal("balance"));
    }

    private IllegalStateException databaseError(String message, SQLException cause) {
        return new IllegalStateException(message, cause);
    }
}