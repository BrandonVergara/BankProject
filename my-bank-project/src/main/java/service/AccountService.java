package service;

import domain.Account;

public interface AccountService {

	void addAccount(Account account);
	
	Account findAccount(int id);
	
	Account login (int id, String pin);
	
	Account register (String pin);
	
	void updateAccount(Account account);
	
	void deleteAccount(int id);
	
	void makeTransaction(int source, int destination, double balance);
	
	void makeDeposity(int id, double amount);
	
	void makeWithdraw(int id, double amount);
	
}
