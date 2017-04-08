package models;

public class Book extends AbstractProduct {

    private String genre;

    public Book(int id, String name, int price, int quantity, String genre) {
        super(id, name, price, quantity);
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return super.toString() + " " + genre;
    }
}
