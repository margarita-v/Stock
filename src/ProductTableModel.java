import models.AbstractProduct;
import models.Book;
import models.Clothes;
import models.Food;
import task.ProductList;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

class ProductTableModel implements TableModel {

    private ProductList productList;
    private String[] columnNames = {"ID", "Название", "Цена", "Количество", "Жанр", "Цвет", "Вес"};

    ProductTableModel(ProductList productList) {
        this.productList = productList;
    }

    boolean add(AbstractProduct product) {
        return productList.add(product);
    }

    boolean edit(int id, AbstractProduct product) {
        return productList.edit(id, product);
    }

    boolean delete(int id) {
        return productList.delete(id);
    }

    void clear() {
        productList.clear();
    }

    @Override
    public int getRowCount() {
        return productList.size();
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
        if (productList.size() == 0)
            return Object.class;
        return getValueAt(0, i).getClass();
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        AbstractProduct product = productList.getProductByIndex(i);
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
