import task.Product;
import task.ProductList;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class ProductTableModel implements TableModel {

    private ProductList productList;

    public ProductTableModel(ProductList productList) {
        this.productList = productList;
    }

    @Override
    public int getRowCount() {
        return productList.size();
    }

    @Override
    public int getColumnCount() {
        return Product.REQUIRED_FIELDS + 1;
    }

    @Override
    public String getColumnName(int i) {
        switch (i) {
            case 0: return "ID";
            case 1: return "Название";
            case 2: return "Цена";
            case 3: return "Количество";
            case 4: return "Описание";
            default: return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Product product = productList.getProductByIndex(i);
        switch (i1) {
            case 0: return product.getId();
            case 1: return product.getName();
            case 2: return product.getPrice();
            case 3: return product.getQuantity();
            case 4: return product.getDescription();
            default: return "";
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
