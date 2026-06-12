package amazon.carrental;

public class CreditCardPayment implements IPaymentGateway {

    @Override
    public boolean processPayment(Customer customer, double amount) {
        System.out.println("💳 Processing credit card payment of $" + String.format("%.2f", amount)
                + " for " + customer.getName());
        // Simulate payment processing
        return true;
    }

    @Override
    public boolean processRefund(Customer customer, double amount) {
        System.out.println("💳 Refunding $" + String.format("%.2f", amount) + " to " + customer.getName() + "'s credit card");
        return true;
    }

    @Override
    public String getName() { return "CreditCard"; }
}

