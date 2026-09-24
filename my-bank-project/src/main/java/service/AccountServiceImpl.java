package service;

import domain.Account;
import java.math.BigDecimal;

import persistence.AccountDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountServiceImpl implements AccountService {
	private final AccountDAO accountDAO;
	private static Logger logger;
	
	public AccountServiceImpl(AccountDAO accountDAO) {
		this.accountDAO=accountDAO;
		logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	}
	
	@Override
	public Account register(String pin) {
		if(!validatePin(pin)) {
			logger.error("Could not create new account, pin invalid");
			throw new IllegalArgumentException("Pin must be exactly 4 digits");
		}
		Account newAccount = new Account(pin);
		Account savedAccount = accountDAO.addAccount(newAccount);
		logger.info("New account {} created succesfully", savedAccount.getId());
		return savedAccount;
	}

	@Override
	public Account findAccount(long id) {
		return accountDAO.getAccountById(id);
	}

	@Override
	public Account login(long id, String pin) {
		Account account = accountDAO.getAccountById(id);
		if (account == null || !account.getPin().equals(pin)) {
			logger.error("Failed login attempt for account ID:{}", id);
			throw new IllegalArgumentException("Invalid account ID or PIN");
		}
		logger.info("Account {} logged in successfully", id);
		return account;
	}

	@Override
	public void deleteAccount(long id) {
	    Account account = accountDAO.getAccountById(id);
	    if (account == null) {
	    	logger.error("Account ID:{} does not exist", id);
	        throw new IllegalArgumentException("Account not found");
	    }
	    if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
	    	logger.error("Account ID:{} not deleted, funds still available", id);
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
