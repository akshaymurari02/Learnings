package amazon.splitwise;

import java.util.*;

/**
 * SplitwiseService - manages users, groups, expenses, and balances.
 * 
 * Balance sheet: Map<userId, Map<userId, double>>
 *   balanceSheet[A][B] = +50 means B owes A $50
 *   balanceSheet[B][A] = -50 means B owes A $50 (from B's perspective)
 */
public class SplitwiseService {

    private final Map<String, User> users;
    private final Map<String, Group> groups;
    private final Map<String, ISplitStrategy> strategies;

    // balanceSheet[userId][otherUserId] = amount (positive = other owes you)
    private final Map<String, Map<String, Double>> balanceSheet;
    private int expenseCounter = 0;

    public SplitwiseService() {
        this.users = new HashMap<>();
        this.groups = new HashMap<>();
        this.balanceSheet = new HashMap<>();

        this.strategies = new HashMap<>();
        strategies.put(SplitType.EQUAL.name(), new EqualSplitStrategy());
        strategies.put(SplitType.EXACT.name(), new ExactSplitStrategy());
        strategies.put(SplitType.PERCENTAGE.name(), new PercentageSplitStrategy());
    }

    // ==================== USER MANAGEMENT ====================

    public void addUser(User user) {
        users.put(user.getId(), user);
        balanceSheet.put(user.getId(), new HashMap<>());
    }

    public User getUser(String userId) {
        User user = users.get(userId);
        if (user == null) throw new RuntimeException("User not found: " + userId);
        return user;
    }

    // ==================== GROUP MANAGEMENT ====================

    public Group createGroup(String groupId, String name, List<User> members) {
        Group group = new Group(groupId, name);
        for (User member : members) {
            group.addMember(member);
        }
        groups.put(groupId, group);
        return group;
    }

    // ==================== ADD EXPENSE ====================

    public Expense addExpense(User paidBy, double amount, String description,
                              SplitType splitType, List<User> participants, List<Double> values) {

        ISplitStrategy strategy = strategies.get(splitType.name());
        List<Split> splits = strategy.calculateSplits(amount, participants, values);

        String expenseId = "EXP-" + (++expenseCounter);
        Expense expense = new Expense(expenseId, description, amount, paidBy, splits, splitType);

        // Update balance sheet
        for (Split split : splits) {
            User owes = split.getUser();
            if (owes.getId().equals(paidBy.getId())) continue; // skip payer's own share

            double owed = split.getAmount();

            // paidBy is owed money from 'owes'
            updateBalance(paidBy.getId(), owes.getId(), owed);
        }

        System.out.println("Added: " + expense);
        return expense;
    }

    public Expense addEqualExpense(User paidBy, double amount, String description, List<User> participants) {
        return addExpense(paidBy, amount, description, SplitType.EQUAL, participants, null);
    }

    // ==================== SETTLE UP ====================

    public void settle(User from, User to, double amount) {
        updateBalance(to.getId(), from.getId(), -amount);
        System.out.println("✅ " + from.getName() + " paid $" + String.format("%.2f", amount) + " to " + to.getName());
    }

    // ==================== BALANCE QUERIES ====================

    public void showBalancesForUser(String userId) {
        User user = getUser(userId);
        Map<String, Double> userBalances = balanceSheet.get(userId);
        boolean hasBalance = false;

        for (Map.Entry<String, Double> entry : userBalances.entrySet()) {
            double amount = entry.getValue();
            if (Math.abs(amount) < 0.01) continue;

            hasBalance = true;
            User other = getUser(entry.getKey());
            if (amount > 0) {
                System.out.println("  " + other.getName() + " owes " + user.getName() + ": $" + String.format("%.2f", amount));
            } else {
                System.out.println("  " + user.getName() + " owes " + other.getName() + ": $" + String.format("%.2f", -amount));
            }
        }

        if (!hasBalance) {
            System.out.println("  No outstanding balances for " + user.getName());
        }
    }

    public void showAllBalances() {
        System.out.println("\n=== All Balances ===");
        Set<String> printed = new HashSet<>();

        for (Map.Entry<String, Map<String, Double>> entry : balanceSheet.entrySet()) {
            String userId = entry.getKey();
            for (Map.Entry<String, Double> bal : entry.getValue().entrySet()) {
                String otherId = bal.getKey();
                double amount = bal.getValue();
                if (Math.abs(amount) < 0.01) continue;

                String key = userId.compareTo(otherId) < 0 ? userId + "-" + otherId : otherId + "-" + userId;
                if (printed.contains(key)) continue;
                printed.add(key);

                if (amount > 0) {
                    System.out.println("  " + getUser(otherId).getName() + " owes " + getUser(userId).getName() + ": $" + String.format("%.2f", amount));
                } else {
                    System.out.println("  " + getUser(userId).getName() + " owes " + getUser(otherId).getName() + ": $" + String.format("%.2f", -amount));
                }
            }
        }
    }

    // ==================== SIMPLIFY DEBTS ====================

    /**
     * Minimizes number of transactions to settle all debts.
     * Calculates net balance per user, then matches creditors with debtors.
     */
    public void showSimplifiedDebts() {
        System.out.println("\n=== Simplified Debts (Minimum Transactions) ===");

        // Calculate net balance for each user
        Map<String, Double> netBalance = new HashMap<>();
        for (String userId : users.keySet()) {
            double net = 0;
            Map<String, Double> userBal = balanceSheet.get(userId);
            for (double amount : userBal.values()) {
                net += amount;
            }
            netBalance.put(userId, net);
        }

        // Separate into creditors (net > 0) and debtors (net < 0)
        List<Map.Entry<String, Double>> creditors = new ArrayList<>();
        List<Map.Entry<String, Double>> debtors = new ArrayList<>();

        for (Map.Entry<String, Double> entry : netBalance.entrySet()) {
            if (entry.getValue() > 0.01) creditors.add(entry);
            else if (entry.getValue() < -0.01) debtors.add(entry);
        }

        int i = 0, j = 0;
        while (i < creditors.size() && j < debtors.size()) {
            double credit = creditors.get(i).getValue();
            double debt = -debtors.get(j).getValue();
            double settle = Math.min(credit, debt);

            System.out.println("  " + getUser(debtors.get(j).getKey()).getName()
                    + " pays $" + String.format("%.2f", settle)
                    + " to " + getUser(creditors.get(i).getKey()).getName());

            creditors.get(i).setValue(credit - settle);
            debtors.get(j).setValue(-(debt - settle));

            if (creditors.get(i).getValue() < 0.01) i++;
            if (-debtors.get(j).getValue() < 0.01) j++;
        }
    }

    // ==================== INTERNAL ====================

    private void updateBalance(String creditorId, String debtorId, double amount) {
        // creditor's sheet: debtor owes them
        balanceSheet.get(creditorId).merge(debtorId, amount, Double::sum);
        // debtor's sheet: they owe creditor (negative)
        balanceSheet.get(debtorId).merge(creditorId, -amount, Double::sum);
    }
}

