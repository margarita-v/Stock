import task.Product;
import task.ProductList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class MainFrame extends JFrame implements ActionListener {

    private JPanel rootPanel;
    private JTable table;
    private JMenuItem dbItem;
    private Font font;
    private JPopupMenu popupMenu;

    private ProductList productList;
    private int selectedId;

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        font = new Font("Arial", Font.PLAIN, 12);

        // Main items of menu bar
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(font);
        JMenu viewMenu = new JMenu("View");
        viewMenu.setFont(font);
        JMenu editMenu = new JMenu("Edit");
        editMenu.setFont(font);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(editMenu);

        // Configure file menu
        JMenu openMenu = new JMenu("Open");
        openMenu.setFont(font);
        // Open from text file
        JMenuItem txtFileItem = new JMenuItem("From text file");
        txtFileItem.setAccelerator(KeyStroke.getKeyStroke('O',
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        txtFileItem.setFont(font);
        txtFileItem.addActionListener(this);
        // Open from database
        dbItem = new JMenuItem("From database");
        dbItem.setFont(font);
        dbItem.addActionListener(this);

        openMenu.add(txtFileItem);
        openMenu.add(dbItem);

        JMenu saveMenu = new JMenu("Save");
        saveMenu.setFont(font);
        // Save as text file
        JMenuItem saveToTxtItem = new JMenuItem("To text file");
        saveToTxtItem.setAccelerator(KeyStroke.getKeyStroke('S',
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        saveToTxtItem.setFont(font);
        saveToTxtItem.addActionListener(this);
        // Save as database
        JMenuItem saveToDbItem = new JMenuItem("To database");
        saveToDbItem.setFont(font);
        saveToDbItem.addActionListener(this);

        saveMenu.add(saveToTxtItem);
        saveMenu.add(saveToDbItem);

        // Exit item
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_ESCAPE);
        exitItem.addActionListener(this);
        exitItem.setFont(font);

        fileMenu.add(openMenu);
        fileMenu.add(saveMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Configure view menu
        JMenu filterMenu = new JMenu("Filter");
        filterMenu.setFont(font);
        // Items of filter menu
        JMenuItem moreFilterItem = new JMenuItem("Price more than value");
        JMenuItem lessFilterItem = new JMenuItem("Price less than value");
        JMenuItem rangeFilterItem = new JMenuItem("Set price range");
        JMenuItem clearFilterItem = new JMenuItem("Clear filter");
        // Set action listener
        moreFilterItem.addActionListener(this);
        lessFilterItem.addActionListener(this);
        rangeFilterItem.addActionListener(this);
        clearFilterItem.addActionListener(this);
        // Set font
        moreFilterItem.setFont(font);
        lessFilterItem.setFont(font);
        rangeFilterItem.setFont(font);
        clearFilterItem.setFont(font);

        filterMenu.add(moreFilterItem);
        filterMenu.add(lessFilterItem);
        filterMenu.add(rangeFilterItem);
        filterMenu.addSeparator();
        filterMenu.add(clearFilterItem);
        viewMenu.add(filterMenu);

        // Configure edit menu
        JMenuItem addItem = new JMenuItem("Add");
        JMenuItem editItem = new JMenuItem("Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem deleteManyItem = new JMenuItem("Delete many");
        JMenuItem clearItem = new JMenuItem("Clear");
        // Set action listener
        addItem.addActionListener(this);
        editItem.addActionListener(this);
        deleteItem.addActionListener(this);
        deleteManyItem.addActionListener(this);
        clearItem.addActionListener(this);
        // Set font
        addItem.setFont(font);
        editItem.setFont(font);
        deleteItem.setFont(font);
        deleteManyItem.setFont(font);
        clearItem.setFont(font);

        editMenu.add(addItem);
        editMenu.add(editItem);
        editMenu.add(deleteItem);
        editMenu.add(deleteManyItem);
        editMenu.addSeparator();
        editMenu.add(clearItem);

        setJMenuBar(menuBar);
    }

    private void createPopupMenu() {
        popupMenu = new JPopupMenu();
        JMenuItem popupEditItem = new JMenuItem("Edit product");
        popupEditItem.setFont(font);
        popupEditItem.addActionListener(this);
        JMenuItem popupDeleteItem = new JMenuItem("Delete product");
        popupDeleteItem.setFont(font);
        popupDeleteItem.addActionListener(this);

        popupMenu.add(popupEditItem);
        popupMenu.add(popupDeleteItem);
    }

    private void createGui() {
        setContentPane(rootPanel);
        setTitle("Информация о товарах");
        createMenuBar();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600, 500));

        productList = new ProductList();
        ProductTableModel tableModel = new ProductTableModel(productList);
        table = new JTable(tableModel);
        getContentPane().add(new JScrollPane(table));

        /*
        TableRowSorter sorter = new TableRowSorter(tableModel);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        table.setRowSorter(sorter);
        */

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Integer.class, centerRenderer);

        createPopupMenu();
        table.setComponentPopupMenu(popupMenu);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                Point point = mouseEvent.getPoint();
                int currentRow = table.rowAtPoint(point);
                // get ID of chosen product
                selectedId = (Integer) table.getValueAt(currentRow, 0);
            }
        });
        pack();
        setVisible(true);
    }

    private MainFrame() {
        createGui();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(MainFrame::new);
    }

    // User selection of menu item
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            // File menu
            case "From text file":
                openFromTextFile();
                break;
            case "From database":
                openFromDatabase();
                break;
            case "To text file":
                saveToTextFile();
                break;
            case "To database":
                saveToDatabase();
                break;
            case "Exit":
                exit();
                break;
            // View menu
            case "Price more than value":
                priceMoreFilter();
                break;
            case "Price less than filter":
                priceLessFilter();
                break;
            case "Set price range":
                priceRangeFilter();
                break;
            case "Clear filter":
                clearFilter();
                break;
            // Edit menu
            case "Add":
                add();
                break;
            case "Edit":
                edit();
                break;
            case "Delete":
                delete();
                break;
            case "Delete many":
                deleteMany();
                break;
            case "Clear":
                clear();
                break;
            // Popup menu
            case "Edit product":
                edit(selectedId, productList.getById(selectedId));
                break;
            case "Delete product":
                productList.delete(selectedId);
                table.updateUI();
                JOptionPane.showMessageDialog(this, "Товар был удален.");
                break;
            default:
                break;
        }
    }

    //region Menu management
    // File menu
    private void openFromTextFile() {
        File workingDirectory = new File(System.getProperty("user.dir"));
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(workingDirectory);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        int res = fileChooser.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (productList.loadFromFile(file.getName())) {
                table.setAutoCreateRowSorter(true);
                table.updateUI();
                JOptionPane.showMessageDialog(this, "Данные из файла были загружены.");
            }
            else
                JOptionPane.showMessageDialog(this,
                        "Файл содержит неверные данные.",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void openFromDatabase() {
        if (productList.loadFromDatabase()) {
            table.setAutoCreateRowSorter(true);
            table.updateUI();
            JOptionPane.showMessageDialog(this, "Данные из базы данных были загружены.");
        }
    }

    private void saveToTextFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        int res = fileChooser.showSaveDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            productList.saveToFile(file.getName());
            JOptionPane.showMessageDialog(this, "Данные были сохранены в файл.");
        }
    }

    private void saveToDatabase() {
        productList.saveToDatabase();
        JOptionPane.showMessageDialog(this, "Данные были сохранены в базу данных.");
        dbItem.setEnabled(true);
    }

    private void exit() {
        int dialogResult = JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите выйти?", "Подтверждение", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            productList.clear();
            System.exit(0);
        }
    }

    // View menu
    private void priceMoreFilter() {
        if (productList.size() > 0) {
            String result = JOptionPane.showInputDialog(this,
                    "Введите минимальное значение цены.",
                    "Фильтр по цене", JOptionPane.DEFAULT_OPTION);
            if (result != null) {
                try {

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                            "Введено неверное значение цены.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else
            JOptionPane.showMessageDialog(this, "Список товаров пуст.");
    }

    private void priceLessFilter() {
        if (productList.size() > 0) {
            String result = JOptionPane.showInputDialog(this,
                    "Введите максимальное значение цены.",
                    "Фильтр по цене", JOptionPane.DEFAULT_OPTION);
            if (result != null) {
                try {

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                            "Введено неверное значение цены.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else
            JOptionPane.showMessageDialog(this, "Список товаров пуст.");
    }

    private void priceRangeFilter() {

    }

    private void clearFilter() {

    }

    // Edit menu
    private void add() {
        DialogFrame dialog = new DialogFrame("Добавление товара", null);
        dialog.setVisible(true);
        Product product = dialog.getProduct();
        if (product != null) {
            if (productList.add(product)) {
                table.setAutoCreateRowSorter(true);
                table.updateUI();
                JOptionPane.showMessageDialog(this, "Товар был добавлен.");
            }
            else
                JOptionPane.showMessageDialog(this,
                        "Товар с данным ID уже существует.",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void edit() {
        if (productList.size() > 0) {
            String result = JOptionPane.showInputDialog(this,
                    "Введите ID товара, который требуется отредактировать.",
                    "Редактирование", JOptionPane.DEFAULT_OPTION);
            if (result != null) {
                try {
                    int id = Integer.parseInt(result);
                    Product productForEdit = productList.getById(id);
                    if (productForEdit == null)
                        JOptionPane.showMessageDialog(this,
                                "Товар с данным ID не найден.",
                                "Ошибка", JOptionPane.WARNING_MESSAGE);
                    else
                        edit(id, productForEdit);

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                            "Введено неверное значение ID.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else
            JOptionPane.showMessageDialog(this, "Список товаров пуст.");
    }

    private void edit(int id, Product productForEdit) {
        DialogFrame dialog = new DialogFrame("Редактирование товара", productForEdit);
        dialog.setVisible(true);
        Product newProduct = dialog.getProduct();
        // if user didn't canceled dialog
        if (newProduct != null) {
            if (productList.edit(id, newProduct)) {
                table.updateUI();
                JOptionPane.showMessageDialog(this, "Товар был отредактирован.");
                // ID of selected product was changed
                selectedId = newProduct.getId();
            } else
                JOptionPane.showMessageDialog(this,
                        "Товар с данным ID уже существует.",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void delete() {
        if (productList.size() > 0) {
            String result = JOptionPane.showInputDialog(this,
                    "Введите ID товара, который требуется удалить.",
                    "Удаление", JOptionPane.DEFAULT_OPTION);
            if (result != null) {
                try {
                    int id = Integer.parseInt(result);
                    if (!productList.delete(id))
                        JOptionPane.showMessageDialog(this,
                                "Товар с данным ID не найден.",
                                "Ошибка", JOptionPane.WARNING_MESSAGE);
                    else {
                        table.setAutoCreateRowSorter(true);
                        table.updateUI();
                        JOptionPane.showMessageDialog(this, "Товар был удален.");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                            "Введено неверное значение ID.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else
            JOptionPane.showMessageDialog(this, "Список товаров пуст.");
    }

    private void deleteMany() {
        if (productList.size() > 0) {
            DeleteManyFrame dialog = new DeleteManyFrame(productList.getProductsIDs());
            dialog.setVisible(true);
            List<Integer> chosenItems = dialog.getChosenIDs();
            if (chosenItems.size() > 0) {
                for (Integer id : chosenItems) {
                    productList.delete(id);
                }
                table.setAutoCreateRowSorter(true);
                table.updateUI();
                JOptionPane.showMessageDialog(this, "Выбранные товары были удалены.");
            }
            else
                JOptionPane.showMessageDialog(this,
                        "Ни одного товара для удаления не выбрано",
                        "Информация", JOptionPane.INFORMATION_MESSAGE);
        }
        else
            JOptionPane.showMessageDialog(this, "Список товаров пуст.");
    }

    private void clear() {
        if (productList.size() > 0) {
            int dialogResult = JOptionPane.showConfirmDialog(this,
                    "Очистить список товаров?", "Подтверждение", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                productList.clear();
                table.updateUI();
                JOptionPane.showMessageDialog(this, "Товары были удалены.");
            }
        }
        else
            JOptionPane.showMessageDialog(this, "Список товаров пуст.");
    }
    //endregion
}

