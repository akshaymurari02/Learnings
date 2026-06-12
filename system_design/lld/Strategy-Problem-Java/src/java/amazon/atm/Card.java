package amazon.atm;

public class Card {
    private final String cardNumber;
    private final String accountNumber;
    private final String holderName;
    private final String pin;
    private final CardType cardType;

    public Card(String cardNumber, String accountNumber, String holderName, String pin, CardType cardType) {
        this.cardNumber = cardNumber;
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.pin = pin;
        this.cardType = cardType;
    }

    public boolean validatePin(String enteredPin) {
        return this.pin.equals(enteredPin);
    }

    public String getCardNumber() { return cardNumber; }
    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public CardType getCardType() { return cardType; }

    @Override
    public String toString() {
        return holderName + " [****" + cardNumber.substring(cardNumber.length() - 4) + "] " + cardType;
    }
}

