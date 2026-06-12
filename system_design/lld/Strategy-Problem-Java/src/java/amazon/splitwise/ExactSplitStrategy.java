package amazon.splitwise;

import java.util.ArrayList;
import java.util.List;

public class ExactSplitStrategy implements ISplitStrategy {

    @Override
    public List<Split> calculateSplits(double totalAmount, List<User> participants, List<Double> values) {
        if (participants.size() != values.size()) {
            throw new RuntimeException("Participants and values count mismatch");
        }
        double sum = values.stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(sum - totalAmount) > 0.01) {
            throw new RuntimeException("Exact amounts ($" + sum + ") don't add up to total ($" + totalAmount + ")");
        }

        List<Split> splits = new ArrayList<>();
        for (int i = 0; i < participants.size(); i++) {
            splits.add(new Split(participants.get(i), values.get(i)));
        }
        return splits;
    }

    @Override
    public SplitType getType() { return SplitType.EXACT; }
}

