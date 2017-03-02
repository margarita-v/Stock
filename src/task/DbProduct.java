package task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbProduct {

    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost/ProductsDb";

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
    public void createDatabase() throws SQLException, ClassNotFoundException {
        String SQL_CREATE_TABLE = "CREATE TABLE Product("
                + "id_product INTEGER NOT NULL, "
                + "product_name VARCHAR(20) NOT NULL, "
                + "price INTEGER NOT NULL, "
                + "quantity INTEGER NOT NULL, "
                + "description VARCHAR(256), "
                + "PRIMARY KEY (id_product) "
                + ")";
        executeQuery(SQL_CREATE_TABLE);
    }

    // Drop table
    public void dropDatabase() throws SQLException, ClassNotFoundException {
        String SQL_DROP_TABLE = "DROP TABLE Product";
        executeQuery(SQL_DROP_TABLE);
    }

    // Clear table
    public void clearTable() throws SQLException, ClassNotFoundException {
        String SQL_TRUNCATE_TABLE = "TRUNCATE TABLE Product";
        executeQuery(SQL_TRUNCATE_TABLE);
    }

    // Get all records in database
    public List<Product> getAll() throws SQLException, ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        Connection connection = getConnection();

        String selectSQL = "SELECT * FROM Product";
        PreparedStatement statement = connection.prepareStatement(selectSQL);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id_product");
            String name = rs.getString("product_name");
            int price = rs.getInt("price");
            int quantity = rs.getInt("quantity");
            String description = rs.getString("description");

            Product product = new Product(id, name, price, quantity, description);
            products.add(product);
        }

        statement.close();
        connection.close();
        return products;
    }

    // Get product by id
    public static Product getById(int id) throws SQLException, ClassNotFoundException {
        Product student = null;
        Connection connection = getConnection();

        String selectSQL = "SELECT * FROM Product WHERE id_product = ?";
        PreparedStatement statement = connection.prepareStatement(selectSQL);
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            String name = rs.getString("product_name");
            int price = rs.getInt("price");
            int quantity = rs.getInt("quantity");
            String description = rs.getString("description");
            student = new Product(id, name, price, quantity, description);
        }

        statement.close();
        connection.close();
        return student;
    }

    // Insert product to database
    public void insert(Product product) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
            String insertTableSQL = "INSERT INTO Product("
                + "id_product, product_name, price, quantity, description) "
                + "VALUES(?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(insertTableSQL);

        statement.setInt(1, product.getId());
        statement.setString(2, product.getName());
        statement.setInt(3, product.getPrice());
        statement.setInt(4, product.getQuantity());
        statement.setString(5, product.getDescription());
        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    // Update record in database by id
    public void update(Product product) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        String updateTableSQL = "UPDATE Product SET "
                + "product_name = ?, "
                + "price = ?, "
                + "quantity = ?, "
                + "description = ? "
                + "WHERE id_product = ?";
        PreparedStatement statement = connection.prepareStatement(updateTableSQL);

        statement.setString(1, product.getName());
        statement.setInt(2, product.getPrice());
        statement.setInt(3, product.getQuantity());
        statement.setString(4, product.getDescription());
        statement.setInt(5, product.getId());
        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    // Delete product from database
    public void delete(Product product) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection();
        String deleteTableSQL = "DELETE FROM Product "
                + "WHERE id_product = ?";
        PreparedStatement statement = connection.prepareStatement(deleteTableSQL);

        statement.setInt(1, product.getId());
        statement.executeUpdate();

        statement.close();
        connection.close();
    }
}
