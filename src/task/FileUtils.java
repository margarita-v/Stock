package task;

import models.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileUtils {

    // Load info from text file
    public static ProductList loadFromFile(String fileName) throws IOException {
        ProductList products = new ProductList();
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
                    throw new IOException();
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
                    products.add(book);
                    break;
                case CLOTHES:
                    Clothes clothes = new Clothes(id, name, price, quantity, getStringField(words, i));
                    products.add(clothes);
                    break;
                case FOOD:
                    int weight = Integer.parseInt(words[i]);
                    Food food = new Food(id, name, price, quantity, weight);
                    products.add(food);
                    break;
            }
        }
        // all lines in file were correct
        return products;
    }

    // Save info to text file
    public static void saveToFile(String fileName, ProductList products) throws IOException {
        List<String> lines = new ArrayList<>();
        for (AbstractProduct p: products) {
            String info = p.toString();
            if (p instanceof Book)
                lines.add("BOOK," + info);
            else if (p instanceof Clothes)
                lines.add("CLOTHES," + info);
            else
                lines.add("FOOD," + info);
        }
        Files.write(Paths.get(fileName), lines, StandardCharsets.UTF_8);
    }

    // Get last string field which is optional for different products
    private static String getStringField(String[] words, int index) {
        String result = words[index++];
        while (index < words.length)
            result += " " + words[index++];
        return result;
    }
}
