package amazon.splitwise;

import java.util.ArrayList;
import java.util.List;

public class PercentageSplitStrategy implements ISplitStrategy {

    @Override
    public List<Split> calculateSplits(double totalAmount, List<User> participants, List<Double> values) {
        if (participants.size() != values.size()) {
            throw new RuntimeException("Participants and percentages count mismatch");
        }
        double totalPercent = values.stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(totalPercent - 100.0) > 0.01) {
            throw new RuntimeException("Percentages must add up to 100, got " + totalPercent);
        }

        List<Split> splits = new ArrayList<>();
        for (int i = 0; i < participants.size(); i++) {
            double amount = (values.get(i) / 100.0) * totalAmount;
            splits.add(new Split(participants.get(i), amount));
        }
        return splits;
    }

    @Override
    public SplitType getType() { return SplitType.PERCENTAGE; }
}

