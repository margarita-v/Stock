package models;

public class Clothes extends AbstractProduct {

    private String color;

    public Clothes(int id, String name, int price, int quantity, String color) {
        super(id, name, price, quantity);
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return super.toString() + "," + color;
    }
}
