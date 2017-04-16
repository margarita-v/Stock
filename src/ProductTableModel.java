import models.AbstractProduct;
import models.Book;
import models.Clothes;
import models.Food;
import task.ProductList;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.function.Predicate;

// This class is a layer between product list and JTable
class ProductTableModel extends ProductList implements TableModel {

    private String[] columnNames = {"ID", "Название", "Цена", "Количество", "Жанр", "Цвет", "Вес"};

    // Price filter for product list
    ProductTableModel filter(Predicate<Integer> condition) {
        ProductTableModel result = new ProductTableModel();
        for (AbstractProduct product: products) {
            if (condition.test(product.getPrice()))
                result.add(product);
        }
        return result;
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
