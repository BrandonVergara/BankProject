package service;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDateTime;

import domain.Transaction;
import domain.TransactionType;
import domain.Account;
import persistence.AccountDAO;
import persistence.TransactionDAO;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TransactionServiceImpl implements TransactionService {
	
	private final AccountDAO accountDAO;
	private final TransactionDAO transactionDAO;
	private final Connection conn;
	private static Logger logger;
	
	public TransactionServiceImpl(AccountDAO accountDAO, TransactionDAO transactionDAO, Connection conn) {
		this.accountDAO = accountDAO;
		this.transactionDAO = transactionDAO;
		this.conn = conn;
		logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
	}

	@Override
	public void makeDeposit(long id, BigDecimal amount) {
		validatePositive(amount);
		Account account = accountExist(id);
		
		try {
			conn.setAutoCommit(false);
			
			account.setBalance(account.getBalance().add(amount));
			accountDAO.updateBalance(account);
			String description = "Deposited $" + amount.setScale(2); 
		
			Transaction transaction = newTransaction(TransactionType.DEPOSIT, amount, null, id, description);
			transactionDAO.addTransaction(transaction);
			logger.info("${} deposited to account {}", amount, id);
			
			conn.commit();
		}catch(Exception e) {
			rollBack();
			logger.error("Failed to deposit into account {}", id, e);
			throw new IllegalStateException("Deposit failed, no changes made", e);
		}finally {
			resetAutoCommit();
		}
	}

	@Override
	public void makeWithdraw(long id, BigDecimal amount) {
		validatePositive(amount);
		Account account = accountExist(id);
		validateFunds(account, amount);
		try {
			conn.setAutoCommit(false);
			
			account.setBalance(account.getBalance().subtract(amount));
			accountDAO.updateBalance(account);
			String description = "Withdrew $" + amount.setScale(2);
		
			Transaction transaction = newTransaction(TransactionType.WITHDRAW, amount, id, null, description);
			transactionDAO.addTransaction(transaction);
			logger.info("${} withdrew from account {}", amount, id);
			conn.commit();
		}catch(Exception e) {
			rollBack();
			logger.error("Failed to withdraw from account {}", id, e);
			throw new IllegalStateException("Withdraw failed, no changes made", e);
		} finally {
			resetAutoCommit();
		}

	}

	@Override
	public void makeTransfer(long source, long destination, BigDecimal amount) {
		validatePositive(amount);
		Account srcAccount = accountExist(source);
		Account desAccount = accountExist(destination);
		validateFunds(srcAccount, amount);
		try {
			conn.setAutoCommit(false);
			
			srcAccount.setBalance(srcAccount.getBalance().subtract(amount));
			desAccount.setBalance(desAccount.getBalance().add(amount));
			accountDAO.updateBalance(srcAccount);
			accountDAO.updateBalance(desAccount);
			String description = "Transferred $" + amount.setScale(2) + " from Account: " + source + " into Account: " + destination;
		
			Transaction transaction = newTransaction(TransactionType.TRANSFER, amount, source, destination, description);
			transactionDAO.addTransaction(transaction);
			logger.info("Transferred ${} from account {} into account {}", amount, source, destination);
			
			conn.commit();
		} catch(Exception e) {
			rollBack();
			logger.error("Failed to make transfer", e);
			throw new IllegalStateException("Transfer failed, no changes made" , e);
		} finally {
			resetAutoCommit();
		}
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
			logger.error("Withdraw failed for account {}: insufficient funds", account.getId());
			throw new IllegalArgumentException("Account has insufficient funds");
		}
	}
	
	private void rollBack() {
		try {
			conn.rollback();
		} catch(SQLException e) {
			throw new IllegalStateException("Rollback failed", e);
		}
	}
	
	private void resetAutoCommit() {
		try {
			conn.setAutoCommit(true);
		} catch(SQLException e) {
			throw new IllegalStateException("Could not reset auto-commit", e);
		}
	}

}
