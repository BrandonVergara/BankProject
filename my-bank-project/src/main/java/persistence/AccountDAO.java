package persistence;

import java.util.List;
import domain.Account;

public interface AccountDAO {
	
	void addAccount(Account account);
	
	Account getAccountByID(int id);
	
	List<Account> getAllAccounts();
	
	void updateAccount(Account account);
	
	void deleteAccount(int id);
	
	void makeTransfer(int source, int destination, double amount);
	
	void makeDeposit(int id, double amount);
	
	void makeWithdraw(int id, double amount);

}
