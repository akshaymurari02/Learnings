package amazon.splitwise;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private final String id;
    private final String name;
    private final List<User> members;
    private final List<Expense> expenses;

    public Group(String id, String name) {
        this.id = id;
        this.name = name;
        this.members = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    public void addMember(User user) { members.add(user); }
    public void addExpense(Expense expense) { expenses.add(expense); }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<User> getMembers() { return members; }
    public List<Expense> getExpenses() { return expenses; }

    @Override
    public String toString() { return name; }
}

