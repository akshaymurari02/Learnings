package amazon.atm;

public class ATMDemo {

    public static void main(String[] args) {

        // 1. Setup Bank with accounts
        BankService bank = new BankService();
        bank.addAccount(new Account("ACC-001", "Alice", 5000.00));
        bank.addAccount(new Account("ACC-002", "Bob", 1200.00));

        // 2. Create cards linked to accounts
        Card aliceCard = new Card("4111111111111111", "ACC-001", "Alice", "1234", CardType.DEBIT);
        Card bobCard = new Card("5222222222222222", "ACC-002", "Bob", "5678", CardType.DEBIT);

        // 3. Create ATM
        ATM atm = new ATM("ATM-001", "Main Street Branch", bank);

        // ==================== Alice: Check Balance + Withdraw ====================
        System.out.println("=== Alice's Session ===\n");

        atm.insertCard(aliceCard);
        atm.enterPin("1234");

        atm.checkBalance();

        System.out.println();
        atm.withdraw(270); // $100x2 + $50x1 + $20x1

        System.out.println();
        atm.ejectCard();

        // ==================== Bob: Deposit ====================
        System.out.println("\n=== Bob's Session ===\n");

        atm.insertCard(bobCard);
        atm.enterPin("5678");

        atm.checkBalance();
        System.out.println();
        atm.deposit(500.00);

        System.out.println();
        atm.ejectCard();

        // ==================== Wrong PIN ====================
        System.out.println("\n=== Wrong PIN Demo ===\n");

        atm.insertCard(aliceCard);
        atm.enterPin("0000"); // wrong
        atm.enterPin("9999"); // wrong
        atm.enterPin("1111"); // wrong -> blocked

        // ==================== ATM Cash Stock ====================
        System.out.println("\n=== ATM Cash Status ===");
        atm.getCashDispenser().printStock();
    }
}

