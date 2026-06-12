package amazon.atm;

import java.util.Map;

/**
 * ATM - main class managing state, authentication, and operations.
 * 
 * Flow: IDLE → insertCard → CARD_INSERTED → enterPin → AUTHENTICATED
 *       → withdraw/deposit/checkBalance → IDLE (ejectCard)
 */
public class ATM {

    private final String id;
    private final String location;
    private final BankService bankService;
    private final CashDispenser cashDispenser;
    private ATMState state;
    private Card currentCard;
    private int pinAttempts;
    private static final int MAX_PIN_ATTEMPTS = 3;

    public ATM(String id, String location, BankService bankService) {
        this.id = id;
        this.location = location;
        this.bankService = bankService;
        this.cashDispenser = new CashDispenser();
        this.state = ATMState.IDLE;
        this.pinAttempts = 0;
    }

    // ==================== CARD & AUTH ====================

    public void insertCard(Card card) {
        if (state != ATMState.IDLE) {
            throw new RuntimeException("ATM is busy. Current state: " + state);
        }
        this.currentCard = card;
        this.state = ATMState.CARD_INSERTED;
        this.pinAttempts = 0;
        System.out.println("Card inserted: " + card);
        System.out.println("Please enter your PIN...");
    }

    public boolean enterPin(String pin) {
        if (state != ATMState.CARD_INSERTED) {
            throw new RuntimeException("Please insert card first");
        }

        if (currentCard.validatePin(pin)) {
            state = ATMState.AUTHENTICATED;
            System.out.println("✅ PIN verified. Welcome, " + currentCard.getHolderName() + "!");
            return true;
        } else {
            pinAttempts++;
            int remaining = MAX_PIN_ATTEMPTS - pinAttempts;
            if (remaining <= 0) {
                System.out.println("❌ Too many wrong attempts. Card blocked. Ejecting...");
                ejectCard();
                return false;
            }
            System.out.println("❌ Wrong PIN. " + remaining + " attempts remaining.");
            return false;
        }
    }

    // ==================== OPERATIONS ====================

    public double checkBalance() {
        validateAuthenticated();
        state = ATMState.TRANSACTION_IN_PROGRESS;

        String accountNumber = currentCard.getAccountNumber();
        double balance = bankService.checkBalance(accountNumber);

        System.out.println("💰 Current Balance: $" + String.format("%.2f", balance));

        state = ATMState.AUTHENTICATED;
        return balance;
    }

    public Map<Denomination, Integer> withdraw(int amount) {
        validateAuthenticated();
        state = ATMState.TRANSACTION_IN_PROGRESS;

        String accountNumber = currentCard.getAccountNumber();

        // Check account has enough
        double balance = bankService.checkBalance(accountNumber);
        if (amount > balance) {
            state = ATMState.AUTHENTICATED;
            throw new RuntimeException("Insufficient balance. Available: $" + String.format("%.2f", balance));
        }

        // Check ATM can dispense
        Map<Denomination, Integer> dispensed = cashDispenser.dispense(amount);

        // Debit account
        bankService.withdraw(accountNumber, amount);

        System.out.println("✅ Withdrawn: $" + amount);
        System.out.println("  Bills dispensed:");
        for (Map.Entry<Denomination, Integer> entry : dispensed.entrySet()) {
            System.out.println("    $" + entry.getKey().getValue() + " x " + entry.getValue());
        }
        System.out.println("  Remaining balance: $" + String.format("%.2f", bankService.checkBalance(accountNumber)));

        state = ATMState.AUTHENTICATED;
        return dispensed;
    }

    public void deposit(double amount) {
        validateAuthenticated();
        state = ATMState.TRANSACTION_IN_PROGRESS;

        String accountNumber = currentCard.getAccountNumber();
        bankService.deposit(accountNumber, amount);

        System.out.println("✅ Deposited: $" + String.format("%.2f", amount));
        System.out.println("  New balance: $" + String.format("%.2f", bankService.checkBalance(accountNumber)));

        state = ATMState.AUTHENTICATED;
    }

    // ==================== SESSION ====================

    public void ejectCard() {
        System.out.println("Card ejected. Thank you, " + (currentCard != null ? currentCard.getHolderName() : "") + "!");
        currentCard = null;
        pinAttempts = 0;
        state = ATMState.IDLE;
    }

    // ==================== HELPERS ====================

    private void validateAuthenticated() {
        if (state != ATMState.AUTHENTICATED) {
            throw new RuntimeException("Not authenticated. Current state: " + state);
        }
    }

    public ATMState getState() { return state; }
    public String getId() { return id; }
    public String getLocation() { return location; }
    public CashDispenser getCashDispenser() { return cashDispenser; }
}

