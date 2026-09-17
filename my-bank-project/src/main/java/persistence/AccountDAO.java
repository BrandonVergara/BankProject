package persistence;

import java.util.List;
import domain.Account;

public interface AccountDAO {
	
	void addAccount(Account account);
	
	Account getAccountByID(int id);
	
	void updateAccount(Account account);
	
	void deleteAccount(int id);

}
