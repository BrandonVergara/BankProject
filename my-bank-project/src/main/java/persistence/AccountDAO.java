package persistence;

import domain.Account;

public interface AccountDAO {

    Account addAccount(Account account);

    Account getAccountById(long id);

    void updatePin(Account account);

    void updateBalance(Account account);

    void deleteAccount(long id);
}