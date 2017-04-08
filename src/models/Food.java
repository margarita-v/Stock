package models;

public class Food extends AbstractProduct {

    private int weight;

    public Food(int id, String name, int price, int quantity, int weight) {
        super(id, name, price, quantity);
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return super.toString() + " " + weight;
    }
}
