package task;

public class Product {

    private String name;
    private int price;
    private int count;
    private String description;

    public Product(String name, int price, int count, String description) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.description = description;
    }

    //region Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    //endregion

    @Override
    public String toString() {
        return name + " " + price + " " + count + " " + description;
    }
}
