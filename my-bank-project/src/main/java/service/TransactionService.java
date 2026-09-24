package service;

import java.math.BigDecimal;
import java.util.List;
import domain.Transaction;

public interface TransactionService {
	
	void makeDeposit(long id, BigDecimal amount);
	
	void makeWithdraw(long id, BigDecimal amount);
	
	void makeTransfer(long source, long destination, BigDecimal amount);
	
	List<Transaction> getHistory(long id);
	
}
