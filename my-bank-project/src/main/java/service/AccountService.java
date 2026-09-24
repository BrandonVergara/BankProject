package service;

import domain.Account;

public interface AccountService {
	
	Account register (String pin);
	
	Account findAccount(long id);
	
	Account login (long id, String pin);
	
	void deleteAccount(long id);
	
}
