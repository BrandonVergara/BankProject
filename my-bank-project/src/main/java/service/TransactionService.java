package service;

import java.math.BigDecimal;

public interface TransactionService {
	
	void makeTransaction(int source, int destination, BigDecimal amount);
	
	void makeDeposit(int id, BigDecimal amount);
	
	void makeWithdraw(int id, BigDecimal amount);
}
