package amazon.inventory;

public class Product {
    private final String id;
    private final String name;
    private final double price;
    private final Category category;

    public Product(String id, String name, double price, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public Category getCategory() { return category; }

    @Override
    public String toString() {
        return name + " [$" + price + "] (" + category + ")";
    }
}

