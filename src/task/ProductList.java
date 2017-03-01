package task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProductList {

    private ArrayList<Product> products;

    public ProductList() { products = new ArrayList<>(); }

    // Add product to the list of products
    public void Add(Product product) {
        for (Product p: products) {
            // product was found in list
            // then increase product's count
            if (p.equals(product)) {
                p.setCount(p.getCount() + 1);
                return;
            }
        }
        // add new product to list
        products.add(product);
    }

    // Delete product from the list of products
    public boolean Delete(String name) {
        for (Product p: products) {
            if (p.getName().equals(name)) {
                products.remove(p);
                return true;
            }
        }
        return false;
    }

    // Edit product which name equals to name parameter and replace them with newProduct
    public boolean Edit(String name, Product newProduct) {
        for (Product p: products) {
            // find product with this name
            if (p.getName().equals(name)) {
                // get index of product which we should edit,
                // remove it and insert new product to the same position
                int index = products.indexOf(p);
                products.remove(index);
                products.add(index, newProduct);
                return true;
            }
        }
        return false;
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
                if (line.length() < 3)
                    return false; // not enough parameters
                // get info about product from current line
                String name = words[0];
                Integer price = Integer.parseInt(words[1]);
                Integer count = Integer.parseInt(words[2]);
                String description = "";
                // if line contains description
                for (int i = 3; i < words.length; i++)
                    description += words[i] + " ";

                Product product = new Product(name, price, count, description);
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
}
