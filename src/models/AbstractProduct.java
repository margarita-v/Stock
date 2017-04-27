package models;

public abstract class AbstractProduct {

    private int id;
    private String name;
    private int price;
    private int quantity;

    public AbstractProduct(int id, String name, int price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    //region Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() { return quantity; }
    //endregion

    @Override
    public String toString() {
        return id + "," + name + "," + price + "," + quantity;
    }
}
