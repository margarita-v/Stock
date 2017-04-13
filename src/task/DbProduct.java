package task;

import models.AbstractProduct;
import models.Book;
import models.Clothes;
import models.Food;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbProduct {

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/ProductsDb";
    //private static final String DB_URL = "jdbc:postgresql://localhost/ProductsDb";

    private static final String DB_USER = "username";
    private static final String DB_PASSWORD = "password";

    // Connect with database
    private static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(DB_DRIVER);
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Execute simple query without result set
    private void executeQuery(String query) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();

        Statement statement = connection.createStatement();
        statement.execute(query);

        statement.close();
        connection.close();
    }

    // Create table
    /*
    public void createDatabase() throws SQLException, ClassNotFoundException {
        String SQL_CREATE_TABLE = "CREATE TABLE AbstractProduct("
                + "id_product INTEGER NOT NULL, "
                + "product_name VARCHAR(20) NOT NULL, "
                + "price INTEGER NOT NULL, "
                + "quantity INTEGER NOT NULL, "
                + "description VARCHAR(256), "
                + "PRIMARY KEY (id_product) "
                + ")";
        executeQuery(SQL_CREATE_TABLE);
    }*/

    // Clear table
    public void clearTable() throws SQLException, ClassNotFoundException {
        String SQL_TRUNCATE_TABLE = "TRUNCATE TABLE Product";
        executeQuery(SQL_TRUNCATE_TABLE);
    }

    // Insert product to database
    public void insert(AbstractProduct product) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        String insertTableSQL = "INSERT INTO Product("
                + "id, product_name, price, quantity) "
                + "VALUES(?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(insertTableSQL);

        statement.setInt(1, product.getId());
        statement.setString(2, product.getName());
        statement.setInt(3, product.getPrice());
        statement.setInt(4, product.getQuantity());
        statement.executeUpdate();

        if (product instanceof Book) {
            insertTableSQL = "INSERT INTO Books("
                    + "id, genre) "
                    + "VALUES(?,?)";
            statement = connection.prepareStatement(insertTableSQL);
            statement.setInt(1, product.getId());
            statement.setString(2, ((Book) product).getGenre());
        }
        else if (product instanceof Clothes) {
            insertTableSQL = "INSERT INTO Clothes("
                    + "id, color) "
                    + "VALUES(?,?)";
            statement = connection.prepareStatement(insertTableSQL);
            statement.setInt(1, product.getId());
            statement.setString(2, ((Clothes) product).getColor());
        }
        else if (product instanceof Food) {
            insertTableSQL = "INSERT INTO Food("
                    + "id, weight) "
                    + "VALUES(?,?)";
            statement = connection.prepareStatement(insertTableSQL);
            statement.setInt(1, product.getId());
            statement.setInt(2, ((Food) product).getWeight());
        }
        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    // Delete product from database
    public void delete(AbstractProduct product) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        String deleteTableSQL = "DELETE FROM Product WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(deleteTableSQL);

        statement.setInt(1, product.getId());
        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    //region Get all products

    // Get all records in database
    public List<AbstractProduct> getAll() throws SQLException, ClassNotFoundException {
        List<AbstractProduct> products = new ArrayList<>();
        Connection connection = getConnection();

        products.addAll(getAllBooks(connection));
        products.addAll(getAllClothes(connection));
        products.addAll(getAllFood(connection));

        connection.close();
        return products;
    }

    private List<Book> getAllBooks(Connection connection) throws SQLException {
        List<Book> result = new ArrayList<>();
        String selectSQL = "SELECT * FROM Product JOIN Books ON Product.id = Books.id";
        PreparedStatement statement = connection.prepareStatement(selectSQL);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("product_name");
            int price = rs.getInt("price");
            int quantity = rs.getInt("quantity");
            String genre = rs.getString("genre");

            Book book = new Book(id, name, price, quantity, genre);
            result.add(book);
        }
        statement.close();
        return result;
    }

    private List<Clothes> getAllClothes(Connection connection) throws SQLException {
        List<Clothes> result = new ArrayList<>();
        String selectSQL = "SELECT * FROM Product JOIN Clothes ON Product.id = Clothes.id";
        PreparedStatement statement = connection.prepareStatement(selectSQL);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("product_name");
            int price = rs.getInt("price");
            int quantity = rs.getInt("quantity");
            String color = rs.getString("color");

            Clothes clothes = new Clothes(id, name, price, quantity, color);
            result.add(clothes);
        }
        statement.close();
        return result;
    }

    private List<Food> getAllFood(Connection connection) throws SQLException {
        List<Food> result = new ArrayList<>();
        String selectSQL = "SELECT * FROM Product JOIN Food ON Product.id = Food.id";
        PreparedStatement statement = connection.prepareStatement(selectSQL);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("product_name");
            int price = rs.getInt("price");
            int quantity = rs.getInt("quantity");
            int weight = rs.getInt("weight");

            Food food = new Food(id, name, price, quantity, weight);
            result.add(food);
        }
        statement.close();
        return result;
    }
    //endregion
}
