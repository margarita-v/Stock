package task;

import models.AbstractProduct;
import models.Book;
import models.Clothes;
import models.Food;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Class for working with database
class DbUtils {

    private static final String DB_URL = "jdbc:h2:" + System.getProperty("user.dir") + "/src/database/DbProducts";

    //region Queries
    private static final String insertProduct   = "INSERT INTO Product(id, product_name, price, quantity) VALUES(?,?,?,?)";
    private static final String insertBook      = "INSERT INTO Books(book_id, genre) VALUES(?,?)";
    private static final String insertClothes   = "INSERT INTO Clothes(clothes_id, color) VALUES(?,?)";
    private static final String insertFood      = "INSERT INTO Food(food_id, weight) VALUES(?,?)";

    private static final String deleteProduct   = "DELETE FROM Product WHERE id = ?";
    private static final String clearTable      = "DELETE FROM Product";

    private static final String getAllBooks     = "SELECT * FROM Product JOIN Books ON Product.id = Books.book_id";
    private static final String getAllClothes   = "SELECT * FROM Product JOIN Clothes ON Product.id = Clothes.clothes_id";
    private static final String getAllFood      = "SELECT * FROM Product JOIN Food ON Product.id = Food.food_id";
    //endregion

    // Connect with database
    private static Connection getConnection() throws SQLException, ClassNotFoundException {
        return DriverManager.getConnection(DB_URL);
    }

    // Execute simple query without result set
    private static void executeQuery(String query) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();

        Statement statement = connection.createStatement();
        statement.execute(query);

        statement.close();
        connection.close();
    }

    // Clear table
    static void clearTable() throws SQLException, ClassNotFoundException {
        executeQuery(clearTable);
    }

    // Insert product to database
    static void insert(AbstractProduct product) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(insertProduct);

        statement.setInt(1, product.getId());
        statement.setString(2, product.getName());
        statement.setInt(3, product.getPrice());
        statement.setInt(4, product.getQuantity());
        statement.executeUpdate();

        if (product instanceof Book) {
            statement = connection.prepareStatement(insertBook);
            statement.setInt(1, product.getId());
            statement.setString(2, ((Book) product).getGenre());
        }
        else if (product instanceof Clothes) {
            statement = connection.prepareStatement(insertClothes);
            statement.setInt(1, product.getId());
            statement.setString(2, ((Clothes) product).getColor());
        }
        else if (product instanceof Food) {
            statement = connection.prepareStatement(insertFood);
            statement.setInt(1, product.getId());
            statement.setInt(2, ((Food) product).getWeight());
        }
        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    // Delete product from database
    static void delete(AbstractProduct product) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(deleteProduct);

        statement.setInt(1, product.getId());
        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    //region Get all products

    // Get all records in database
    static List<AbstractProduct> getAll() throws SQLException, ClassNotFoundException {
        List<AbstractProduct> products = new ArrayList<>();
        Connection connection = getConnection();

        products.addAll(getAllBooks(connection));
        products.addAll(getAllClothes(connection));
        products.addAll(getAllFood(connection));

        connection.close();
        return products;
    }

    private static List<Book> getAllBooks(Connection connection) throws SQLException {
        List<Book> result = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(getAllBooks);
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

    private static List<Clothes> getAllClothes(Connection connection) throws SQLException {
        List<Clothes> result = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(getAllClothes);
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

    private static List<Food> getAllFood(Connection connection) throws SQLException {
        List<Food> result = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(getAllFood);
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
