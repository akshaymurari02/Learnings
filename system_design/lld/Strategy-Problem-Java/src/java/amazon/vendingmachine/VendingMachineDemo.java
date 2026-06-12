package amazon.vendingmachine;

public class VendingMachineDemo {

    public static void main(String[] args) {
        // Create 3x3 vending machine
        VendingMachine machine = new VendingMachine(3, 3);

        // Load products
        machine.loadProduct("A1", new Product("P1", "Coca Cola", 1.50), 5);
        machine.loadProduct("A2", new Product("P2", "Pepsi", 1.50), 3);
        machine.loadProduct("A3", new Product("P3", "Water", 1.00), 10);
        machine.loadProduct("B1", new Product("P4", "Chips", 2.00), 4);
        machine.loadProduct("B2", new Product("P5", "Snickers", 1.75), 6);
        machine.loadProduct("B3", new Product("P6", "Gum", 0.75), 8);
        machine.loadProduct("C1", new Product("P7", "Coffee", 2.50), 2);

        // Display
        machine.displayProducts();

        // Customer 1: Buy Coca Cola
        System.out.println("\n=== Customer 1: Buying Coca Cola ===");
        machine.insertCoin(Coin.DOLLAR);
        machine.insertCoin(Coin.QUARTER);
        machine.insertCoin(Coin.QUARTER);
        machine.selectProduct("A1");

        // Customer 2: Insufficient funds then add more
        System.out.println("\n=== Customer 2: Buying Coffee ===");
        machine.insertCoin(Coin.DOLLAR);
        machine.insertCoin(Coin.DOLLAR);
        machine.selectProduct("C1"); // Need $2.50, have $2.00
        machine.insertCoin(Coin.QUARTER);
        machine.insertCoin(Coin.QUARTER);
        machine.selectProduct("C1"); // Now have $2.50

        // Customer 3: Cancel and refund
        System.out.println("\n=== Customer 3: Cancel ===");
        machine.insertCoin(Coin.DOLLAR);
        machine.insertCoin(Coin.QUARTER);
        machine.cancelAndRefund();

        // Final state
        machine.displayProducts();
    }
}

