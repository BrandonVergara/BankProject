package persistence;

import java.util.List;
import domain.Transaction;

public interface TransactionDAO {
	
	Transaction addTransaction(Transaction transaction);

	public List<Transaction> getHistory(long id);
	
}
