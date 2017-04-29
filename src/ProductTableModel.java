import models.AbstractProduct;
import models.Book;
import models.Clothes;
import models.Food;
import task.ProductList;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

// This class is a layer between product list and JTable
class ProductTableModel extends ProductList implements TableModel {

    private String[] columnNames = {"ID", "Название", "Цена", "Количество", "Жанр", "Цвет", "Вес"};
    private List<AbstractProduct> productsBeforeFilter;
    private Predicate<Integer> filterCondition;

    void setProducts(ProductList products) {
        this.products.clear();
        for (AbstractProduct product: products) {
            this.products.add(product);
        }
    }

    AbstractProduct getProductById(int id) {
        if (this.productsBeforeFilter == null)
            return getById(id);
        for (AbstractProduct product: this.productsBeforeFilter) {
            if (product.getId() == id)
                return product;
        }
        return null;
    }

    private boolean foundInProductsBeforeFilter(int id) {
        if (this.productsBeforeFilter == null)
            return false;
        for (AbstractProduct product: this.productsBeforeFilter) {
            if (product.getId() == id)
                return true;
        }
        return false;
    }

    @Override
    protected int getIndex(int id) {
        if (this.productsBeforeFilter == null)
            return super.getIndex(id);
        AbstractProduct product = getProductById(id);
        if (product != null)
            return this.productsBeforeFilter.indexOf(product);
        return -1;
    }

    @Override
    public boolean add(AbstractProduct product) {
        if (this.productsBeforeFilter == null)
            return super.add(product);
        if (foundInProductsBeforeFilter(product.getId()))
            return false;
        // If a new product fits the condition, we should add it into the primary list
        testCondition(product);
        return true;
    }

    @Override
    public boolean delete(int id) {
        boolean del = super.delete(id);
        if (this.productsBeforeFilter == null)
            return del;
        AbstractProduct product = getProductById(id);
        if (product != null) {
            this.productsBeforeFilter.remove(product);
            return true;
        }
        return false;
    }

    @Override
    public boolean edit(int id, AbstractProduct newProduct) {
        if (this.productsBeforeFilter == null)
            return super.edit(id, newProduct);
        if (foundInProductsBeforeFilter(id)) {
            // ID wasn't changed, but other fields were changed
            if (id == newProduct.getId()) {
                editHelp(id, newProduct);
                return true;
            }
            else {
                // ID and other info were changed
                if (!foundInProductsBeforeFilter(newProduct.getId())) {
                    editHelp(id, newProduct);
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

    private void editHelp(int id, AbstractProduct newProduct) {
        int index = getIndex(id);
        this.productsBeforeFilter.remove(index);
        index = super.getIndex(id);
        if (index > -1)
            this.products.remove(index);
        // Check the filter condition
        testCondition(newProduct);
    }

    // Price filter for product list
    boolean filter(Predicate<Integer> condition) {
        // Save product list before filter apply
        this.filterCondition = condition;
        if (this.productsBeforeFilter == null) {
            this.productsBeforeFilter = new ArrayList<>();
            this.productsBeforeFilter.addAll(this.products);
        }
        // Remove products which don't fit the filter condition
        Iterator<AbstractProduct> iterator = this.products.iterator();
        while (iterator.hasNext()) {
            AbstractProduct product = iterator.next();
            if (!this.filterCondition.test(product.getPrice()))
                iterator.remove();
        }
        if (this.products.size() > 0)
            return true;
        // If product list became empty, we need to rollback filter and return false
        this.products.addAll(this.productsBeforeFilter);
        this.productsBeforeFilter = null;
        return false;
    }

    // Clear filter and return to initial product list
    void clearFilter() {
        if (this.productsBeforeFilter != null) {
            this.products.clear();
            this.products.addAll(this.productsBeforeFilter);
            this.productsBeforeFilter = null;
        }
    }

    private void testCondition(AbstractProduct product) {
        this.productsBeforeFilter.add(product);
        if (this.filterCondition.test(product.getPrice()))
            this.products.add(product);
    }

    @Override
    public int getRowCount() {
        return size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int i) {
        return columnNames[i];
    }

    @Override
    public Class<?> getColumnClass(int i) {
        if (size() == 0)
            return Object.class;
        return getValueAt(0, i).getClass();
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        AbstractProduct product = products.get(i);
        switch(i1) {
            case 0: return product.getId();
            case 1: return product.getName();
            case 2: return product.getPrice();
            case 3: return product.getQuantity();
            case 4:
                if (product instanceof Book)
                    return ((Book) product).getGenre();
                return "-";
            case 5:
                if (product instanceof Clothes)
                    return ((Clothes) product).getColor();
                return "-";
            case 6:
                if (product instanceof Food)
                    return Integer.toString(((Food) product).getWeight());
                return "-";
            default:
                return "-";
        }
    }

    @Override
    public void setValueAt(Object o, int i, int i1) {

    }

    @Override
    public void addTableModelListener(TableModelListener tableModelListener) {

    }

    @Override
    public void removeTableModelListener(TableModelListener tableModelListener) {

    }
}
