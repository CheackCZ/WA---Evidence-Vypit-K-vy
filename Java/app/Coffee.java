package Java.app;

/**
 * Represents a coffee with its type and amount consumed.
 */
public class Coffee {

    private String type;
    private int amount;

    /**
     * Constructs a Coffee with the given type and amount.
     * @param type - type of coffee
     * @param amount - amount of coffee consum
     */
    public Coffee(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    /**
     * Returns the type of coffee.
     * @return the type of coffee.
     */
    public String getType() {
        return type;
    }
    
    /**
     * Returns the amount of coffee consumed.
     * @return the amount of coffee consumed.
     */
    public int getAmount() {
        return amount;
    }
}
