package service;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDateTime;

import domain.Transaction;
import domain.TransactionType;
import domain.Account;
import persistence.AccountDAO;
import persistence.TransactionDAO;


public class TransactionServiceImpl implements TransactionService {
	
	private final AccountDAO accountDAO;
	private final TransactionDAO transactionDAO;
	
	public TransactionServiceImpl(AccountDAO accountDAO, TransactionDAO transactionDAO) {
		this.accountDAO = accountDAO;
		this.transactionDAO = transactionDAO;
	}

	@Override
	public void makeDeposit(long id, BigDecimal amount) {
		validatePositive(amount);
		Account account = accountExist(id);
		
		account.setBalance(account.getBalance().add(amount));
		accountDAO.updateBalance(account);
		String description = "Deposited $" + amount.setScale(2); 
		
		Transaction transaction = newTransaction(TransactionType.DEPOSIT, amount, null, id, description);
		transactionDAO.addTransaction(transaction);
	}

	@Override
	public void makeWithdraw(long id, BigDecimal amount) {
		validatePositive(amount);
		Account account = accountExist(id);
		
		validateFunds(account, amount);
		account.setBalance(account.getBalance().subtract(amount));
		accountDAO.updateBalance(account);
		String description = "Withdrew $" + amount.setScale(2);
		
		Transaction transaction = newTransaction(TransactionType.WITHDRAW, amount, id, null, description);
		transactionDAO.addTransaction(transaction);

	}

	@Override
	public void makeTransfer(long source, long destination, BigDecimal amount) {
		validatePositive(amount);
		Account srcAccount = accountExist(source);
		Account desAccount = accountExist(destination);
		validateFunds(srcAccount, amount);
		
		srcAccount.setBalance(srcAccount.getBalance().subtract(amount));
		desAccount.setBalance(desAccount.getBalance().add(amount));
		accountDAO.updateBalance(srcAccount);
		accountDAO.updateBalance(desAccount);
		String description = "Transfered $" + amount.setScale(2) + " from Account: " + source + " into Account: " + destination;
		
		Transaction transaction = newTransaction(TransactionType.TRANSFER, amount, source, destination, description);
		transactionDAO.addTransaction(transaction);
	}

	@Override
	public List<Transaction> getHistory(long id) {
		return transactionDAO.getHistory(id);
	}
	
	private Account accountExist(long id) {
		Account account = accountDAO.getAccountById(id);
		if(account == null) {
			throw new IllegalArgumentException("Account not found");
		}
		return account;
	}
	
	private Transaction newTransaction(TransactionType type, BigDecimal amount, 
			Long source, Long destination, String description) {
		return new Transaction(null, type, amount, LocalDateTime.now(), source, destination, description);
	}
	
	private void validatePositive(BigDecimal amount) {
	    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
	        throw new IllegalArgumentException("Amount must be greater than zero");
	    }
	}
	
	private void validateFunds(Account account, BigDecimal amount) {
		if(account.getBalance().compareTo(amount) < 0) {
			throw new IllegalArgumentException("Account has insufficient funds");
		}
	}

}
