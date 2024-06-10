package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }
    
    public Account registerAccount(Account account) {
        if (account.getUsername().length() == 0 || account.getPassword().length() < 4) {
            return null;
        }
        return accountDAO.insertAccount(account);
    }

    public Account canLogin(Account account) {
        Account loginAs = accountDAO.getAccountByUsername(account.getUsername());
        if ((loginAs != null) && loginAs.getPassword().equals(account.getPassword())) {
            return loginAs;
        }

        return null;
    }
}
