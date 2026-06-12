package amazon.atm;

import java.util.HashMap;
import java.util.Map;

/**
 * Bank service - simulates the bank backend.
 * Maps accountNumber -> Account.
 */
public class BankService {
    private final Map<String, Account> accounts;

    public BankService() {
        this.accounts = new HashMap<>();
    }

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) throw new RuntimeException("Account not found: " + accountNumber);
        return account;
    }

    public double checkBalance(String accountNumber) {
        return getAccount(accountNumber).getBalance();
    }

    public void withdraw(String accountNumber, double amount) {
        getAccount(accountNumber).debit(amount);
    }

    public void deposit(String accountNumber, double amount) {
        getAccount(accountNumber).credit(amount);
    }
}

