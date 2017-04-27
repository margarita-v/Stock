package task;

import models.*;

import java.util.*;

public class ProductList implements Iterable<AbstractProduct> {

    protected List<AbstractProduct> products;

    protected ProductList() {
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

    @Override
    public Iterator<AbstractProduct> iterator() {
        return new ProductIterator();
    }

    private final class ProductIterator implements Iterator<AbstractProduct> {
        private int index;

        ProductIterator() {
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return this.index < size();
        }

        @Override
        public AbstractProduct next() {
            if (this.hasNext())
                return products.get(index++);
            throw new NoSuchElementException();
        }
    }
}
