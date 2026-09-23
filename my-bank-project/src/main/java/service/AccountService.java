package service;

import domain.Account;

public interface AccountService {
	
	Account register (String pin);
	
	Account findAccount(int id);
	
	Account login (long id, String pin);
	
	void updateAccount(Account account);
	
	void deleteAccount(int id);
	
}
