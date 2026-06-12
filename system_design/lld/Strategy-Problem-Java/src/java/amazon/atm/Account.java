package amazon.atm;

public class Account {
    private final String accountNumber;
    private final String holderName;
    private double balance;

    public Account(String accountNumber, String holderName, double balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }

    public void credit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        this.balance += amount;
    }

    public void debit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (amount > balance) throw new RuntimeException("Insufficient balance");
        this.balance -= amount;
    }

    @Override
    public String toString() {
        return holderName + " [" + accountNumber + "] Balance: $" + String.format("%.2f", balance);
    }
}

