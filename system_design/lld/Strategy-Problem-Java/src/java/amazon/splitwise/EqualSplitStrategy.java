package amazon.splitwise;

import java.util.ArrayList;
import java.util.List;

public class EqualSplitStrategy implements ISplitStrategy {

    @Override
    public List<Split> calculateSplits(double totalAmount, List<User> participants, List<Double> values) {
        List<Split> splits = new ArrayList<>();
        double perPerson = totalAmount / participants.size();
        for (User user : participants) {
            splits.add(new Split(user, perPerson));
        }
        return splits;
    }

    @Override
    public SplitType getType() { return SplitType.EQUAL; }
}

