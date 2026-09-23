package service;

import domain.Account;
import java.math.BigDecimal;

import persistence.AccountDAO;

public class AccountServiceImpl implements AccountService {
	private final AccountDAO accountDAO;
	
	public AccountServiceImpl(AccountDAO accountDAO) {
		this.accountDAO=accountDAO;
	}
	
	@Override
	public Account register(String pin) {
		if(!validatePin(pin)) {
			throw new IllegalArgumentException("Pin must be exactly 4 digits");
		}
		Account newAccount = new Account(pin);
		return accountDAO.addAccount(newAccount);
	}

	@Override
	public Account findAccount(int id) {
		return accountDAO.getAccountById(id);
	}

	@Override
	public Account login(long id, String pin) {
		Account account = accountDAO.getAccountById(id);
		if (account == null || !account.getPin().equals(pin)) {
			throw new IllegalArgumentException("Invalid account ID or PIN");
		}
		return account;
	}

	@Override
	public void updateAccount(Account account) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAccount(int id) {
		if(accountDAO.getAccountById(id) == null) {
			throw new IllegalArgumentException("Account not found");
		}
		else if(accountDAO.getAccountById(id).getBalance().compareTo(BigDecimal.ZERO) > 0) {
			System.out.println("Account balance must be zero.");
		}else {
			accountDAO.deleteAccount(id);
		}
	}

	
	private boolean validatePin(String pin) {
	    if (pin == null || pin.length() != 4) {
	        return false;
	    }
	    
	    for (int i = 0; i < pin.length(); i++) {
	        if (!Character.isDigit(pin.charAt(i))) {
	            return false;
	        }
	    }
	    
	    return true;
	}


}
