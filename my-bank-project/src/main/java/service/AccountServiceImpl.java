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
	public Account findAccount(long id) {
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
	public void deleteAccount(long id) {
	    Account account = accountDAO.getAccountById(id);
	    if (account == null) {
	        throw new IllegalArgumentException("Account not found");
	    }
	    if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
	        throw new IllegalArgumentException("Account balance must be zero before deletion");
	    }
	    accountDAO.deleteAccount(id);
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
