package amazon.vendingmachine;

import java.util.*;

/**
 * VendingMachine - State pattern driven vending machine.
 */
public class VendingMachine {

    private final Map<String, Slot> slots; // slotCode -> Slot
    private VendingMachineState state;
    private double currentBalance;
    private String selectedSlotCode;

    public VendingMachine(int rows, int cols) {
        this.slots = new LinkedHashMap<>();
        this.state = VendingMachineState.IDLE;
        this.currentBalance = 0.0;

        // Initialize slots: A1, A2... B1, B2...
        for (int r = 0; r < rows; r++) {
            for (int c = 1; c <= cols; c++) {
                String code = (char) ('A' + r) + String.valueOf(c);
                slots.put(code, new Slot(code));
            }
        }
    }

    // ==================== ADMIN OPERATIONS ====================

    public void loadProduct(String slotCode, Product product, int quantity) {
        Slot slot = getSlot(slotCode);
        slot.load(product, quantity);
        System.out.println("Loaded " + product.getName() + " x" + quantity + " into slot " + slotCode);
    }

    // ==================== USER OPERATIONS ====================

    public void insertCoin(Coin coin) {
        if (state == VendingMachineState.OUT_OF_SERVICE) {
            throw new RuntimeException("Machine is out of service");
        }
        if (state == VendingMachineState.DISPENSING) {
            throw new RuntimeException("Please wait, dispensing in progress");
        }

        state = VendingMachineState.ACCEPTING_MONEY;
        currentBalance += coin.getValue();
        System.out.println("Inserted " + coin + " ($" + String.format("%.2f", coin.getValue())
                + ") | Balance: $" + String.format("%.2f", currentBalance));
    }

    public void selectProduct(String slotCode) {
        if (state != VendingMachineState.ACCEPTING_MONEY) {
            throw new RuntimeException("Please insert money first");
        }

        Slot slot = getSlot(slotCode);

        if (!slot.isAvailable()) {
            throw new RuntimeException("Product not available in slot " + slotCode);
        }

        double price = slot.getProduct().getPrice();

        if (currentBalance < price) {
            System.out.println("❌ Insufficient balance. Need $" + String.format("%.2f", price)
                    + ", have $" + String.format("%.2f", currentBalance)
                    + ". Insert $" + String.format("%.2f", price - currentBalance) + " more.");
            return;
        }

        // Dispense
        this.selectedSlotCode = slotCode;
        dispense();
    }

    private void dispense() {
        state = VendingMachineState.DISPENSING;
        Slot slot = slots.get(selectedSlotCode);
        Product product = slot.getProduct();

        slot.dispense();
        double change = currentBalance - product.getPrice();

        System.out.println("✅ Dispensing: " + product.getName());
        if (change > 0.001) {
            System.out.println("💰 Change returned: $" + String.format("%.2f", change));
        }

        // Reset
        currentBalance = 0.0;
        selectedSlotCode = null;
        state = VendingMachineState.IDLE;
    }

    public double cancelAndRefund() {
        if (currentBalance <= 0) {
            System.out.println("Nothing to refund");
            return 0;
        }
        double refund = currentBalance;
        currentBalance = 0.0;
        state = VendingMachineState.IDLE;
        System.out.println("💰 Refunded: $" + String.format("%.2f", refund));
        return refund;
    }

    // ==================== DISPLAY ====================

    public void displayProducts() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         VENDING MACHINE              ║");
        System.out.println("╠══════════════════════════════════════╣");
        for (Slot slot : slots.values()) {
            System.out.println("║  " + slot + String.format("%" + (35 - slot.toString().length()) + "s", "║"));
        }
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("Balance: $" + String.format("%.2f", currentBalance) + " | State: " + state);
    }

    // ==================== HELPERS ====================

    private Slot getSlot(String code) {
        Slot slot = slots.get(code);
        if (slot == null) throw new RuntimeException("Invalid slot: " + code);
        return slot;
    }

    public VendingMachineState getState() { return state; }
    public double getCurrentBalance() { return currentBalance; }
}

