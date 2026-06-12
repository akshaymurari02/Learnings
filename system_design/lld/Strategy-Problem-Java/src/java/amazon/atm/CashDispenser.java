package amazon.atm;

import java.util.*;

/**
 * Cash Dispenser - manages denominations using Chain of Responsibility.
 * Greedy approach: dispenses largest bills first.
 */
public class CashDispenser {

    private final Map<Denomination, Integer> cashStock; // denomination -> count

    public CashDispenser() {
        cashStock = new LinkedHashMap<>();
        // Initialize with some cash
        cashStock.put(Denomination.HUNDRED, 50);
        cashStock.put(Denomination.FIFTY, 100);
        cashStock.put(Denomination.TWENTY, 200);
        cashStock.put(Denomination.TEN, 300);
        cashStock.put(Denomination.FIVE, 500);
    }

    /**
     * Dispenses cash using greedy approach (largest denomination first).
     * Returns map of denomination -> count dispensed.
     */
    public Map<Denomination, Integer> dispense(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (amount % 5 != 0) throw new RuntimeException("Amount must be multiple of 5");

        Map<Denomination, Integer> dispensed = new LinkedHashMap<>();
        int remaining = amount;

        for (Denomination denom : Denomination.values()) {
            if (remaining <= 0) break;

            int needed = remaining / denom.getValue();
            int available = cashStock.get(denom);
            int toDispense = Math.min(needed, available);

            if (toDispense > 0) {
                dispensed.put(denom, toDispense);
                remaining -= toDispense * denom.getValue();
            }
        }

        if (remaining > 0) {
            throw new RuntimeException("ATM cannot dispense $" + amount + ". Insufficient denominations.");
        }

        // Deduct from stock
        for (Map.Entry<Denomination, Integer> entry : dispensed.entrySet()) {
            cashStock.put(entry.getKey(), cashStock.get(entry.getKey()) - entry.getValue());
        }

        return dispensed;
    }

    public void addCash(Denomination denomination, int count) {
        cashStock.put(denomination, cashStock.getOrDefault(denomination, 0) + count);
    }

    public int getTotalCash() {
        return cashStock.entrySet().stream()
                .mapToInt(e -> e.getKey().getValue() * e.getValue())
                .sum();
    }

    public void printStock() {
        System.out.println("  ATM Cash Stock:");
        for (Map.Entry<Denomination, Integer> entry : cashStock.entrySet()) {
            System.out.println("    $" + entry.getKey().getValue() + " x " + entry.getValue()
                    + " = $" + (entry.getKey().getValue() * entry.getValue()));
        }
        System.out.println("    Total: $" + getTotalCash());
    }
}

