import models.AbstractProduct;
import models.Book;
import models.Clothes;
import models.Food;
import task.ProductList;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.Objects;

class ProductTableModel implements TableModel {

    private ProductList productList;
    private String[] columnNames = {"ID", "Название", "Цена", "Количество", "Жанр", "Цвет", "Вес"};

    ProductTableModel(ProductList productList) {
        this.productList = productList;
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
        String[] items = product.toString().split(" ");
        if (i1 < columnNames.length - 3)
            return items[i1];
        if (Objects.equals(columnNames[i1], "Жанр") && product instanceof Book ||
                Objects.equals(columnNames[i1], "Цвет") && product instanceof Clothes ||
                Objects.equals(columnNames[i1], "Вес") && product instanceof Food)
            return items[items.length - 1];
        return "-";
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
