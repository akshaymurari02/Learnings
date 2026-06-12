package amazon.carrental;

/**
 * Payment Gateway Strategy interface.
 */
public interface IPaymentGateway {
    boolean processPayment(Customer customer, double amount);
    boolean processRefund(Customer customer, double amount);
    String getName();
}

