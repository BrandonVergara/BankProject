package service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Connection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.Account;
import persistence.AccountDAO;
import persistence.TransactionDAO;



public class TransactionServiceImplTest {
	private AccountDAO aDao;
	private TransactionDAO tDao;
	private TransactionService service;
	private Connection conn;
	
	@BeforeEach
	void setUp(){
		aDao = mock(AccountDAO.class);
		tDao = mock(TransactionDAO.class);
		conn = mock(Connection.class);
		service = new TransactionServiceImpl(aDao, tDao, conn);
	}
	
	@Test
	void makeWithdrawThrowsExceptionWhenFundsInsufficient() {
		Account account = new Account(1000L, "1234", new BigDecimal("50.00"));
		when(aDao.getAccountById(1000L)).thenReturn(account);
		
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				()-> service.makeWithdraw(1000L, new BigDecimal("100.00")));
		
		assertEquals("Account has insufficient funds", exception.getMessage());
	}
}
