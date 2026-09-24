package api;

import persistence.AccountDAO;
import persistence.AccountDAOImpl;
import persistence.TransactionDAO;
import persistence.TransactionDAOImpl;
import persistence.ConnectionFactory;
import java.sql.Connection;

import service.AccountService;
import service.AccountServiceImpl;
import service.TransactionService;
import service.TransactionServiceImpl;

public class Main {
	public static void main(String[] args) {
		Connection conn = ConnectionFactory.getConnectionFactory().getConnection();
		
		AccountDAO accountDAO = new AccountDAOImpl(conn);
		AccountService accountService = new AccountServiceImpl(accountDAO);
		
		TransactionDAO transactionDAO = new TransactionDAOImpl(conn);
		TransactionService transactionService = new TransactionServiceImpl(accountDAO, transactionDAO, conn);
		
		new BankRepl(accountService, transactionService).run();
		
	}
}
