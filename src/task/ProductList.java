package task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductList {

    private List<Product> products;
    private DbProduct dbProducts;

    public int size() {
        return products.size();
    }

    public Product getProductByIndex(int index) {
        return products.get(index);
    }

    public ProductList() {
        products = new ArrayList<>();
        dbProducts = new DbProduct();
    }

    //region Helpful functions
    // Find product by id
    private boolean find(int id) {
        for (Product product: products) {
            if (product.getId() == id)
                return true;
        }
        return false;
    }

    // Get product's index by id
    private int getIndex(int id) {
        Product product = getById(id);
        if (product != null)
            return products.indexOf(product);
        return -1;
    }

    // Get product by id
    public Product getById(int id) {
        for (Product product: products) {
            if (product.getId() == id)
                return product;
        }
        return null;
    }
    //endregion

    //region Edit functions
    // add product to the list of products
    public boolean add(Product product) {
        if (!find(product.getId())) {
            products.add(product);
            return true;
        }
        return false;
    }

    // Delete product from the list of products
    public boolean delete(int id) {
        Product product = getById(id);
        if (product != null) {
            products.remove(product);
            return true;
        }
        return false;
    }

    // Edit product by id and replace them with newProduct
    public boolean edit(int id, Product newProduct) {
        if (find(id)) {
            // ID wasn't changed, but other fields were changed
            if (id == newProduct.getId()) {
                // get index of product which we should edit,
                // remove it and insert new product to the same position
                int index = getIndex(id);
                products.remove(index);
                products.add(index, newProduct);
                return true;
            }
            else {
                // ID and other info were changed
                if (!find(newProduct.getId())) {
                    // get index of product which we should edit,
                    // remove it and insert new product to the same position
                    int index = getIndex(id);
                    products.remove(index);
                    products.add(index, newProduct);
                    return true;
                }
                else
                    // list contains product with ID which equals new product's ID
                    return false;
            }
        }
        else
            // product with this ID not found
            return false;
    }
    //endregion

    // Clear product list and drop database
    public void clear() {
        products.clear();
        try {
            dbProducts.dropDatabase();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Load info from text file
    public boolean loadFromFile(String fileName) {
        try {
            // read all lines in text file
            List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
            products.clear();

            for (String line: lines) {
                // get all words in current line
                String[] words = line.split(" ");
                if (line.length() < Product.REQUIRED_FIELDS)
                    return false; // not enough parameters
                // get info about product from current line
                Integer id = Integer.parseInt(words[0]);
                String name = words[1];
                Integer price = Integer.parseInt(words[2]);
                Integer quantity = Integer.parseInt(words[3]);
                String description = "";
                // if line contains description
                for (int i = Product.REQUIRED_FIELDS; i < words.length; i++)
                    description += words[i] + " ";

                Product product = new Product(id, name, price, quantity, description);
                products.add(product);
            }
            // all lines in file were correct
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        catch (NumberFormatException e) {
            // file is incorrect
            return false;
        }
    }

    // Load info from database
    public boolean loadFromDatabase() {
        try {
            products = dbProducts.getAll();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Save info to text file
    public void saveToFile(String fileName) {
        List<String> lines = new ArrayList<>();
        for (Product p: products) {
            lines.add(p.toString());
        }
        try {
            Files.write(Paths.get(fileName), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save info to database
    public void saveToDatabase() {
        try {
            dbProducts.createDatabase();
            for (Product p: products) {
                dbProducts.insert(p);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
