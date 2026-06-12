package amazon.atm;

public enum Denomination {
    HUNDRED(100),
    FIFTY(50),
    TWENTY(20),
    TEN(10),
    FIVE(5);

    private final int value;

    Denomination(int value) { this.value = value; }

    public int getValue() { return value; }
}

