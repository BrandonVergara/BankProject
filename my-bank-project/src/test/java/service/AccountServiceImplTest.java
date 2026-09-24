package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import domain.Account;
import persistence.AccountDAO;

public class AccountServiceImplTest {
	private AccountDAO dao;
	private AccountService service;
	
	@BeforeEach
	void setUp() {
		dao = mock(AccountDAO.class);
		service = new AccountServiceImpl(dao);
	}
	
	@Test
	void logInReturnsAccountWhenCredentialsCorrect() {
		Account account = new Account(1000L, "1234", new BigDecimal("50.00"));
		when(dao.getAccountById(1000L)).thenReturn(account);
		
		Account result = service.login(1000L, "1234");
        assertEquals(1000L, result.getId());
        assertEquals("1234", result.getPin());
	}


}
