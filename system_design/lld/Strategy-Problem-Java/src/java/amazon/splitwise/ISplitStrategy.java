package amazon.splitwise;

import java.util.List;

/**
 * Strategy interface for splitting expenses.
 */
public interface ISplitStrategy {
    List<Split> calculateSplits(double totalAmount, List<User> participants, List<Double> values);
    SplitType getType();
}

