package amazon.splitwise;

import java.util.List;

public class Expense {
    private final String id;
    private final String description;
    private final double amount;
    private final User paidBy;
    private final List<Split> splits;
    private final SplitType splitType;

    public Expense(String id, String description, double amount, User paidBy, List<Split> splits, SplitType splitType) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splits = splits;
        this.splitType = splitType;
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public User getPaidBy() { return paidBy; }
    public List<Split> getSplits() { return splits; }
    public SplitType getSplitType() { return splitType; }

    @Override
    public String toString() {
        return description + " - $" + String.format("%.2f", amount) + " paid by " + paidBy.getName() + " (" + splitType + ")";
    }
}

