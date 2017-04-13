package task;

import models.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ProductList {

    private List<AbstractProduct> products;
    private DbProduct dbProducts;

    public ProductList() {
        products = new ArrayList<>();
        dbProducts = new DbProduct();
    }

    public int size() {
        return products.size();
    }

    public ProductList filter(Predicate<Integer> condition) {
        ProductList result = new ProductList();
        for (AbstractProduct product: products) {
            if (condition.test(product.getPrice()))
                result.add(product);
        }
        return result;
    }

    //region Helpful functions
    // Get product by index in product list; need for product table model
    public AbstractProduct getProductByIndex(int index) {
        return products.get(index);
    }

    // Get ids of all products; need for delete a set of products
    public List<Integer> getProductsIDs() {
        List<Integer> result = new ArrayList<>();
        for (AbstractProduct product: products) {
            result.add(product.getId());
        }
        return result;
    }

    // Find product by id
    private boolean find(int id) {
        for (AbstractProduct product: products) {
            if (product.getId() == id)
                return true;
        }
        return false;
    }

    // Get product's index by id
    private int getIndex(int id) {
        AbstractProduct product = getById(id);
        if (product != null)
            return products.indexOf(product);
        return -1;
    }

    // Get product by id
    public AbstractProduct getById(int id) {
        for (AbstractProduct product: products) {
            if (product.getId() == id)
                return product;
        }
        return null;
    }
    //endregion

    //region Edit functions
    // Add product to the list of products
    public boolean add(AbstractProduct product) {
        if (!find(product.getId())) {
            products.add(product);
            return true;
        }
        return false;
    }

    // Delete product from the list of products
    public boolean delete(int id) {
        AbstractProduct product = getById(id);
        if (product != null) {
            products.remove(product);
            return true;
        }
        return false;
    }

    // Edit product by id and replace them with newProduct
    public boolean edit(int id, AbstractProduct newProduct) {
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

    // Clear product list
    public void clear() {
        products.clear();
    }
    //endregion

    //region Load and save functions
    // Load info from text file
    public boolean loadFromFile(String fileName) {
        clear();
        try {
            // read all lines in text file
            List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);

            for (String line: lines) {
                // get all words in current line
                String[] words = line.split(" ");
                // get info about product from current line
                ProductType productType = ProductType.valueOf(words[0]);
                Integer id = Integer.parseInt(words[1]);
                String name = words[2];
                Integer price = Integer.parseInt(words[3]);
                Integer quantity = Integer.parseInt(words[4]);
                switch (productType) {
                    case BOOK:
                        String genre = words[5];
                        Book book = new Book(id, name, price, quantity, genre);
                        products.add(book);
                        break;
                    case CLOTHES:
                        String color = words[5];
                        Clothes clothes = new Clothes(id, name, price, quantity, color);
                        products.add(clothes);
                        break;
                    case FOOD:
                        int weight = Integer.parseInt(words[5]);
                        Food food = new Food(id, name, price, quantity, weight);
                        products.add(food);
                        break;
                }
            }
            // all lines in file were correct
            return true;

        } catch (IOException | NumberFormatException | IndexOutOfBoundsException e) {
            // file is incorrect
            e.printStackTrace();
            return false;
        }
    }

    // Load info from database
    public boolean loadFromDatabase() {
        clear();
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
        for (AbstractProduct p: products) {
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
            dbProducts.clearTable();
            for (AbstractProduct p: products) {
                dbProducts.insert(p);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //endregion
}
