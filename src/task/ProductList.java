package task;

import models.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductList {

    protected List<AbstractProduct> products;

    public ProductList() {
        products = new ArrayList<>();
    }

    public int size() {
        return products.size();
    }

    //region Helpful functions

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
            lines.removeAll(Collections.singleton(""));

            for (String line: lines) {
                // get all words in current line
                String[] words = line.split(",");
                for (int i = 0; i < words.length; i++)
                    words[i] = words[i].trim();
                // get info about product from current line
                ProductType productType = ProductType.valueOf(words[0]);
                Integer id = Integer.parseInt(words[1]);
                // if name consists of several words divided by comma, then name would be enclosed in quotes
                String name = words[2];
                int i = 3;
                if (name.charAt(0) == '"' && name.charAt(name.length() - 1) != '"') {
                    while (i < words.length && words[i].charAt(words[i].length() - 1) != '"')
                        name += " " + words[i++];
                    // closing quote not found
                    if (i == words.length)
                        return false;
                    name += " " + words[i++];
                }
                // delete quotes
                if (name.charAt(0) == '"' && name.charAt(name.length() - 1) == '"')
                    name = name.substring(1, name.length() - 2);

                Integer price = Integer.parseInt(words[i]);
                Integer quantity = Integer.parseInt(words[i+1]);
                i+=2;
                // Add product with unique id only
                switch (productType) {
                    case BOOK:
                        Book book = new Book(id, name, price, quantity, getStringField(words, i));
                        add(book);
                        break;
                    case CLOTHES:
                        Clothes clothes = new Clothes(id, name, price, quantity, getStringField(words, i));
                        add(clothes);
                        break;
                    case FOOD:
                        int weight = Integer.parseInt(words[i]);
                        Food food = new Food(id, name, price, quantity, weight);
                        add(food);
                        break;
                }
            }
            // all lines in file were correct
            return true;

        } catch (IOException | IllegalArgumentException | IndexOutOfBoundsException e) {
            // file is incorrect
            return false;
        }
    }

    // Get last string field which is optional for different products
    private String getStringField(String[] words, int index) {
        String result = words[index++];
        while (index < words.length)
            result += " " + words[index++];
        return result;
    }

    // Load info from database
    public boolean loadFromDatabase() {
        clear();
        try {
            products = DbUtils.getAll();
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
            String info = p.toString();
            if (p instanceof Book)
                lines.add("BOOK " + info);
            else if (p instanceof Clothes)
                lines.add("CLOTHES " + info);
            else
                lines.add("FOOD " + info);
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
            DbUtils.clearTable();
            for (AbstractProduct p: products) {
                DbUtils.insert(p);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //endregion
}
