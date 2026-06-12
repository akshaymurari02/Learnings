package amazon.splitwise;

import java.util.Arrays;
import java.util.List;

public class SplitwiseDemo {

    public static void main(String[] args) {

        SplitwiseService service = new SplitwiseService();

        // 1. Add users
        User alice = new User("U1", "Alice", "alice@email.com");
        User bob = new User("U2", "Bob", "bob@email.com");
        User charlie = new User("U3", "Charlie", "charlie@email.com");
        User dave = new User("U4", "Dave", "dave@email.com");

        service.addUser(alice);
        service.addUser(bob);
        service.addUser(charlie);
        service.addUser(dave);

        // 2. Create group
        List<User> tripMembers = Arrays.asList(alice, bob, charlie, dave);
        service.createGroup("G1", "Goa Trip", tripMembers);

        System.out.println("=== Adding Expenses ===\n");

        // 3. Equal split: Alice pays $400 dinner for all 4
        service.addEqualExpense(alice, 400, "Dinner", tripMembers);

        // 4. Equal split: Bob pays $200 cab for all 4
        service.addEqualExpense(bob, 200, "Cab", tripMembers);

        // 5. Exact split: Charlie pays $600 hotel
        service.addExpense(charlie, 600, "Hotel", SplitType.EXACT,
                tripMembers, Arrays.asList(200.0, 100.0, 100.0, 200.0));

        // 6. Percentage split: Dave pays $100 snacks
        service.addExpense(dave, 100, "Snacks", SplitType.PERCENTAGE,
                tripMembers, Arrays.asList(40.0, 30.0, 20.0, 10.0));

        // 7. Show balances
        System.out.println("\n=== Alice's Balances ===");
        service.showBalancesForUser("U1");

        System.out.println("\n=== Bob's Balances ===");
        service.showBalancesForUser("U2");

        service.showAllBalances();

        // 8. Simplified debts
        service.showSimplifiedDebts();

        // 9. Settle up
        System.out.println("\n=== Settling ===");
        service.settle(bob, alice, 50);

        System.out.println("\n=== Alice's Balances After Settlement ===");
        service.showBalancesForUser("U1");
    }
}

