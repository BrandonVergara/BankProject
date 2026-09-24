package persistence;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import domain.Transaction;
import domain.TransactionType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionDAOImpl implements TransactionDAO {
	
	private static final String CREATE_TABLE_SQL = """
			CREATE TABLE IF NOT EXISTS transactions (
				id BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 5000 INCREMENT BY 1),
				transaction_type VARCHAR(10) NOT NULL,
				amount NUMERIC (12,2) NOT NULL,
				timestamp TIMESTAMP NOT NULL,
				source_id BIGINT,
				destination_id BIGINT,
				description VARCHAR(255),
				PRIMARY KEY (id),
				FOREIGN KEY(source_id) REFERENCES accounts(id),
				FOREIGN KEY(destination_id) REFERENCES accounts(id)
			)
			""";
	
	private static final String INSERT_SQL = "INSERT INTO transactions (transaction_type, amount, timestamp, source_id, destination_id, description) "
			+ "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
	private static final String FIND_ALL_SQL = "SELECT id, transaction_type, amount, timestamp, source_id, destination_id, description " + 
			"FROM transactions WHERE source_id = ? OR destination_id = ? ORDER BY timestamp DESC LIMIT ?";
	private final Connection conn;
	private static Logger logger;
	
	public TransactionDAOImpl(Connection conn) {
		this.conn = conn;
		logger = LoggerFactory.getLogger(TransactionDAOImpl.class);
		initializeSchema();
	}
	@Override
	public Transaction addTransaction(Transaction transaction) {
		try(PreparedStatement statement = conn.prepareStatement(INSERT_SQL)){
			statement.setString(1, transaction.getType().name());
			statement.setBigDecimal(2, transaction.getAmount());
			statement.setObject(3, transaction.getTimeStamp());
			statement.setObject(4, transaction.getSourceId());
			statement.setObject(5, transaction.getDestinationId());
			statement.setString(6, transaction.getDescription());
			
			try(ResultSet resultSet = statement.executeQuery()){
				resultSet.next();
				long generatedId = resultSet.getLong("id");
				logger.info("New {} transaction {} recorded", transaction.getType(), generatedId);
				return new Transaction(generatedId, transaction.getType(),transaction.getAmount(),transaction.getTimeStamp(),
						 transaction.getSourceId(), transaction.getDestinationId(), transaction.getDescription());
			}
		} catch(SQLException e) {
			logger.error("New {} transaction not created", transaction.getType(), e);
			throw databaseError("Could not add transaction", e);
		}
	}

	@Override
	public List<Transaction> getHistory(long id) {
		List<Transaction> transactions = new ArrayList<>();
		try(PreparedStatement statement = conn.prepareStatement(FIND_ALL_SQL)){
			statement.setLong(1, id);
			statement.setLong(2, id);
			statement.setInt(3, 10);
			
			try(ResultSet resultSet = statement.executeQuery()){
				while(resultSet.next()) {
					transactions.add(mapTransaction(resultSet));
				}
				logger.info("Transaction history for account {} accessed", id);
				return transactions;
			}
		} catch(SQLException e) {
			logger.error("Could not get transaction history for account {}", id, e);
			throw databaseError("Could not get history" , e);
		}
	}
	
	private void initializeSchema() {
		try(PreparedStatement statement = conn.prepareStatement(CREATE_TABLE_SQL)){
			statement.executeUpdate();
			logger.info("Database schema initialized");
		} catch(SQLException e) {
			logger.error("Could not initialize database schema", e);
			throw databaseError("Could not initialize database schema", e);
		}
	}
	
	private Transaction mapTransaction(ResultSet resultSet) throws SQLException{
		long sourceID = resultSet.getLong("source_id");
		Long source = resultSet.wasNull() ? null : sourceID;
		
		long destinationID = resultSet.getLong("destination_id");
		Long destination = resultSet.wasNull() ? null : destinationID;
		
		return new Transaction(
				resultSet.getLong("id"),
				TransactionType.valueOf(resultSet.getString("transaction_type")),
				resultSet.getBigDecimal("amount"),
				resultSet.getObject("timestamp", java.time.LocalDateTime.class),
				source,
				destination,
				resultSet.getString("description"));
	}
	
	 private IllegalStateException databaseError(String message, SQLException cause) {
	        return new IllegalStateException(message, cause);
	 }

}
