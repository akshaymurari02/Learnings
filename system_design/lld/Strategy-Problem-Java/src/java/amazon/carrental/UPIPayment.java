package amazon.carrental;

public class UPIPayment implements IPaymentGateway {

    @Override
    public boolean processPayment(Customer customer, double amount) {
        System.out.println("📱 Processing UPI payment of $" + String.format("%.2f", amount)
                + " for " + customer.getName());
        return true;
    }

    @Override
    public boolean processRefund(Customer customer, double amount) {
        System.out.println("📱 Refunding $" + String.format("%.2f", amount) + " to " + customer.getName() + " via UPI");
        return true;
    }

    @Override
    public String getName() { return "UPI"; }
}

